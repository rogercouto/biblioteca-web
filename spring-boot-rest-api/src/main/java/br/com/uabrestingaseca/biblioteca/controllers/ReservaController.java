package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Emprestimo;
import br.com.uabrestingaseca.biblioteca.model.Exemplar;
import br.com.uabrestingaseca.biblioteca.model.Reserva;
import br.com.uabrestingaseca.biblioteca.model.Usuario;
import br.com.uabrestingaseca.biblioteca.services.ExemplarService;
import br.com.uabrestingaseca.biblioteca.services.ReservaService;
import br.com.uabrestingaseca.biblioteca.services.UsuarioService;
import br.com.uabrestingaseca.biblioteca.util.ModelUtil;
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
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService service;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ExemplarService exemplarService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Reserva>> index(
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="limit", defaultValue = "10") int limit,
        @RequestParam(value="filter", defaultValue = "") String filter
    ){
        Pageable pageable = PageRequest.of(page, limit);
        Page<Reserva> reservas;
        if (filter.toLowerCase().equals("inactive")||filter.toLowerCase().equals("inativas")){
            reservas = service.findFiltered(false, pageable);
        }else if (filter.toLowerCase().equals("active")||filter.toLowerCase().equals("ativas")){
            reservas = service.findFiltered(true, pageable);
        }else{
            reservas = service.findAll(pageable);
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "X-Total-Count");
        responseHeaders.set("X-Total-Count", String.valueOf(reservas.getTotalElements()));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(reservas.toList());
    }

    @GetMapping(value = "/usuario/{id}")
    public ResponseEntity<List<Reserva>> findByUsuario(
            @PathVariable("id") int usuarioId,
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "10") int limit
    ){
        Usuario usuario = usuarioService.findById(usuarioId);
        if (usuario == null){
            return ResponseEntity.notFound().build();
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<Reserva> reservas = service.findFromUsuario(usuario, pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "X-Total-Count");
        responseHeaders.set("X-Total-Count", String.valueOf(reservas.getTotalElements()));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(reservas.toList());
    }

    @GetMapping(value = "/exemplar/{id}")
    public ResponseEntity<List<Reserva>> findByExemplar(
            @PathVariable("id") int numRegistro,
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "10") int limit
    ){
        Exemplar exemplar = exemplarService.findByNumRegistro(numRegistro);
        if (exemplar == null){
            return ResponseEntity.notFound().build();
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<Reserva> reservas = service.findFromExemplar(exemplar, pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "X-Total-Count");
        responseHeaders.set("X-Total-Count", String.valueOf(reservas.getTotalElements()));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(reservas.toList());
    }
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reserva> findById(@PathVariable("id") int id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reserva> create(@RequestBody Reserva reserva){
        return ResponseEntity.ok(service.create(reserva));
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reserva> update(@PathVariable("id")int id, @RequestBody Reserva reserva){
        if (reserva.getId() != null && !reserva.getId().equals(id)){
            throw new ModelValidationException("Erro ao atualizar reserva",
                    "Id da reserva não pode ser alterado no corpo da requisição");
        }
        reserva.setId(id);
        return ResponseEntity.ok(service.update(reserva));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }


}
