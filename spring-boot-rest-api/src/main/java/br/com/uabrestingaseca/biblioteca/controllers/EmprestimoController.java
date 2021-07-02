package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.*;
import br.com.uabrestingaseca.biblioteca.services.*;
import br.com.uabrestingaseca.biblioteca.util.ModelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {
    
    @Autowired
    private EmprestimoService service;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ExemplarService exemplarService;

    @Autowired
    private ParametroService parametroService;

    @Autowired
    private PendenciaService pendenciaService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Emprestimo>> index(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "10") int limit,
            @RequestParam(value="filter", defaultValue = "") String filter){
        Pageable pageable = PageRequest.of(page, limit);
        Page<Emprestimo> emprestimos;
        if (filter.toLowerCase().equals("inactive")||filter.toLowerCase().equals("inativas")){
            emprestimos = service.findFiltered(false, pageable);
        }else if (filter.toLowerCase().equals("active")||filter.toLowerCase().equals("ativas")){
            emprestimos = service.findFiltered(true, pageable);
        }else{
            emprestimos = service.findAll(pageable);
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "X-Total-Count");
        responseHeaders.set("X-Total-Count", String.valueOf(emprestimos.getTotalElements()));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(emprestimos.toList());
    }

    @GetMapping(value = "/usuario/{id}")
    public ResponseEntity<List<Emprestimo>> findByUsuario(
        @PathVariable("id") int usuarioId,
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="limit", defaultValue = "10") int limit,
        @RequestParam(value="filter", defaultValue = "") String filter
    ){
        Usuario usuario = usuarioService.findById(usuarioId);
        if (usuario == null){
            return ResponseEntity.notFound().build();
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<Emprestimo> emprestimos;
        if (filter.toLowerCase().compareTo("active") == 0 || filter.toLowerCase().compareTo("ativas") == 0){
            emprestimos = service.findFromUsuario(usuario, true, pageable);
        }else if (filter.toLowerCase().compareTo("inactive") == 0 || filter.toLowerCase().compareTo("inativas") == 0){
            emprestimos = service.findFromUsuario(usuario, false, pageable);
        }else{
            emprestimos = service.findFromUsuario(usuario, pageable);
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "X-Total-Count");
        responseHeaders.set("X-Total-Count", String.valueOf(emprestimos.getTotalElements()));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(emprestimos.toList());
    }

    @GetMapping(value = "/exemplar/{id}")
    public ResponseEntity<List<Emprestimo>> findByExemplar(
            @PathVariable("id") int numRegistro,
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "10") int limit
    ){
        Exemplar exemplar = exemplarService.findByNumRegistro(numRegistro);
        if (exemplar == null){
            return ResponseEntity.notFound().build();
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<Emprestimo> emprestimos = service.findFromExemplar(exemplar, pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "X-Total-Count");
        responseHeaders.set("X-Total-Count", String.valueOf(emprestimos.getTotalElements()));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(emprestimos.toList());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Emprestimo> findById(@PathVariable("id") int id){
        Emprestimo emprestimo = service.findById(id);
        if (emprestimo == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Emprestimo> create(@RequestBody Emprestimo emprestimo){
        return ResponseEntity.ok(service.create(emprestimo));
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Emprestimo> update(@PathVariable("id") int id, @RequestBody Emprestimo emprestimo){
        if (emprestimo.getId() != null && !emprestimo.getId().equals(id)){
            throw new ModelValidationException("Erro ao atualizar reserva",
                    "Id do empréstimo não pode ser alterado no corpo da requisição");
        }
        emprestimo.setId(id);
        return ResponseEntity.ok(service.update(emprestimo));
    }

    @PutMapping(value = "/devolucao/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<?> devolucao(@PathVariable("id") int id){
        Emprestimo emprestimo = service.findById(id);
        if (emprestimo == null){
            return ResponseEntity.notFound().build();
        }
        Map<Object, Object> response = new LinkedHashMap<>();
        if (emprestimo.getDataHoraDevolucao() == null){
            LocalDateTime now = LocalDateTime.now();
            emprestimo.setDataHoraDevolucao(now);
            LocalDate today = now.toLocalDate();
            boolean passouPrazo = today.isAfter(emprestimo.getPrazo());
            if (passouPrazo){
                long diasAtraso = ChronoUnit.DAYS.between(emprestimo.getPrazo(), today);
                BigDecimal multaPorDias = parametroService.getMultaPorDiasAtrazo();
                BigDecimal multa = multaPorDias.multiply(new BigDecimal(diasAtraso));
                Pendencia pendencia = new Pendencia();
                pendencia.setValor(multa);
                pendencia.setUsuario(emprestimo.getUsuario());
                pendencia.setEmprestimo(emprestimo);
                pendencia.setDataHoraLancamento(LocalDateTime.now());
                Livro livro = emprestimo.getExemplar().getLivro();
                pendencia.setDescricao(String.format("Multa referente a empréstimo do livro: %s", livro.getTitulo()));
                pendenciaService.save(pendencia);
                DecimalFormat df = new DecimalFormat("#,##0.00");
                String message = String.format("Empréstimo devolvido após o prazo.\rMulta gerada no valor de R$: %s", df.format(multa));
                response.put("message", message);
            }else{
                response.put("message", "Empréstimo devolvido com sucesso!");
            }
            emprestimo = service.devolucao(emprestimo);
            response.put("emprestimo", emprestimo);
        }else{
            throw new ModelValidationException("Erro ao devolver exemplar","Exemplar já devolvido!");
        }
        return ResponseEntity.ok(response);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
