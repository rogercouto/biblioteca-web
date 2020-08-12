package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Assunto;
import br.com.uabrestingaseca.biblioteca.services.AssuntoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/assuntos")
public class AssuntoController {

    @Autowired
    private AssuntoService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> index(
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="limit", defaultValue = "10") int limit,
        @RequestParam(value="find", defaultValue = "") String find
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Assunto> assuntos = (find.isBlank()) ?
                service.findAll(pageable):
                service.find(find, pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Total-Count", String.valueOf(assuntos.getTotalElements()));
        return ResponseEntity.ok().headers(responseHeaders).body(assuntos.toList());
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Assunto> findById(@PathVariable("id")int id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Assunto> create(@Valid @RequestBody Assunto assunto){
        if (assunto.getId() != null) {
            throw new ModelValidationException("Erro na criação do assunto",
                    "Id do assunto é gerada pela API");
        }
        if (service.findFirstByDescricao(assunto.getDescricao()) != null){
            throw new ModelValidationException("Erro na criação do assunto",
                    "Já existe um assunto com essa descrição");
        }
        return ResponseEntity.ok(service.save(assunto));
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Assunto> update(@PathVariable("id")int id, @Valid @RequestBody Assunto assunto){
        if (assunto.getId() != id && !assunto.getId().equals(id)) {
            throw new ModelValidationException("Erro na atualização do assunto",
                    "Id do assunto não pode ser alterada no corpo da requisição");
        } else {
            assunto.setId(id);
        }
        return ResponseEntity.ok(service.save(assunto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")int id){
        if (!service.delete(id)) {
            throw new ModelValidationException("Erro na exclusão",
                    String.format("Assunto com id=%d não encontrado",id));
        }
        return ResponseEntity.ok().build();
    }

}
