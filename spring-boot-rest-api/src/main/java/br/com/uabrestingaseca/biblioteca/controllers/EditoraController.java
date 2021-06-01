package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Editora;
import br.com.uabrestingaseca.biblioteca.services.EditoraService;
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
@RequestMapping("/editoras")
public class EditoraController {

    @Autowired
    private EditoraService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Editora>> index(
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="limit", defaultValue = "10") int limit,
        @RequestParam(value="find", defaultValue = "") String find
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Editora> editoras = (find.isBlank()) ?
                service.findAll(pageable):
                service.find(find, pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "X-Total-Count");
        responseHeaders.set("X-Total-Count", String.valueOf(editoras.getTotalElements()));
        return ResponseEntity.ok().headers(responseHeaders).body(editoras.toList());
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Editora> findById(@PathVariable("id")int id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Editora> create(@Valid @RequestBody Editora editora){
        if (editora.getId() != null) {
            throw new ModelValidationException("Erro na criação do editora",
                    "Id do editora é gerada pela API");
        }
        if (service.findFirstByNome(editora.getNome()) != null){
            throw new ModelValidationException("Erro na criação da editora",
                    "Já existe um editora com esse nome");
        }
        return ResponseEntity.ok(service.save(editora));
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Editora> update(@PathVariable("id")int id, @Valid @RequestBody Editora editora){
        if (editora.getId() != null && !editora.getId().equals(id)) {
            throw new ModelValidationException("Erro na atualização da editora",
                    "Id da editora não pode ser alterada no corpo da requisição");
        } else {
            editora.setId(id);
        }
        return ResponseEntity.ok(service.save(editora));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")int id){
        if (!service.delete(id)) {
            throw new ModelValidationException("Erro na exclusão",
                    String.format("Editora com id=%d não encontrada",id));
        }
        return ResponseEntity.ok().build();
    }

}
