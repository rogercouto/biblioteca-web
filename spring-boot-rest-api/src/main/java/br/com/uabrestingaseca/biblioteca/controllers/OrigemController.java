package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Origem;
import br.com.uabrestingaseca.biblioteca.services.OrigemService;
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
@RequestMapping("/origens")
public class OrigemController {

    @Autowired
    private OrigemService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Origem>> index(
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="limit", defaultValue = "10") int limit,
        @RequestParam(value="find", defaultValue = "") String find
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Origem> origens = (find.isBlank()) ?
                service.findAll(pageable):
                service.find(find, pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "X-Total-Count");
        responseHeaders.set("X-Total-Count", String.valueOf(origens.getTotalElements()));
        return ResponseEntity.ok().headers(responseHeaders).body(origens.toList());
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Origem> findById(@PathVariable("id")int id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Origem> create(@Valid @RequestBody Origem origem){
        if (origem.getId() != null) {
            throw new ModelValidationException("Erro na criação do origem",
                    "Id do origem é gerada pela API");
        }
        if (service.findFirstByDescricao(origem.getDescricao()) != null){
            throw new ModelValidationException("Erro na criação do origem",
                    "Já existe um origem com essa descrição");
        }
        return ResponseEntity.ok(service.save(origem));
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Origem> update(@PathVariable("id")int id, @Valid @RequestBody Origem origem){
        if (origem.getId() != null && !origem.getId().equals(id)) {
            throw new ModelValidationException("Erro na atualização do origem",
                    "Id do origem não pode ser alterada no corpo da requisição");
        } else {
            origem.setId(id);
        }
        return ResponseEntity.ok(service.save(origem));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")int id){
        if (!service.delete(id)) {
            throw new ModelValidationException("Erro na exclusão",
                    String.format("Origem com id=%d não encontrado",id));
        }
        return ResponseEntity.ok().build();
    }

}
