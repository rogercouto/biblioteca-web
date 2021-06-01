package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Emprestimo;
import br.com.uabrestingaseca.biblioteca.model.Exemplar;
import br.com.uabrestingaseca.biblioteca.model.Reserva;
import br.com.uabrestingaseca.biblioteca.model.Usuario;
import br.com.uabrestingaseca.biblioteca.services.EmprestimoService;
import br.com.uabrestingaseca.biblioteca.services.ExemplarService;
import br.com.uabrestingaseca.biblioteca.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {
    
    @Autowired
    private EmprestimoService service;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ExemplarService exemplarService;

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
    @RequestParam(value="limit", defaultValue = "10") int limit
    ){
        Usuario usuario = usuarioService.findById(usuarioId);
        if (usuario == null){
            return ResponseEntity.notFound().build();
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<Emprestimo> emprestimos = service.findFromUsuario(usuario, pageable);
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

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Emprestimo> delete(@PathVariable("id") int id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
