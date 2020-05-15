package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Baixa;
import br.com.uabrestingaseca.biblioteca.model.Exemplar;
import br.com.uabrestingaseca.biblioteca.services.BaixaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/baixas")
public class BaixaController {

    @Autowired
    private BaixaService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Baixa>> index(
    @RequestParam(value="page", defaultValue = "0") int page,
    @RequestParam(value="limit", defaultValue = "10") int limit){
        Pageable pageable = PageRequest.of(page, limit);
        Page<Baixa> baixas = service.findAll(pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Total-Count", String.valueOf(baixas.getTotalElements()));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(baixas.toList());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Baixa findById(@PathVariable("id")int id){
        return service.findById(id);
    }

    @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public Baixa create(@Valid @RequestBody Baixa baixa){
        return service.save(baixa);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }

}
