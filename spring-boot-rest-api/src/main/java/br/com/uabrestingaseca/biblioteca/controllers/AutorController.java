package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Autor;
import br.com.uabrestingaseca.biblioteca.services.AutorService;
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
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> index(
    @RequestParam(value="page", defaultValue = "0") int page,
    @RequestParam(value="limit", defaultValue = "10") int limit,
    @RequestParam(value="find", defaultValue = "") String find) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Autor> autores = (find.isBlank()) ?
                service.findAll(pageable):
                service.find(find, pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Total-Count", String.valueOf(autores.getTotalElements()));
        return ResponseEntity.ok().headers(responseHeaders).body(autores.toList());
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Autor findById(@PathVariable("id")int id){
        return service.findById(id);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody Autor autor){
        if (autor.getId() != null) {
            throw new ModelValidationException("Erro na criação do autor",
                    "Id do autor é gerada pela API");
        }
        if (service.findFirstByNomes(autor.getNome(), autor.getSobrenome()) != null){
            throw new ModelValidationException("Erro na criação do autor",
                    "Já existe um autor com esse nome e sobrenome");
        }
        return ResponseEntity.ok(service.save(autor));
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable("id")int id, @Valid @RequestBody Autor autor){
        if (autor.getId() != null && !autor.getId().equals(id)){
            throw new ModelValidationException("Erro na atualização do autor",
                    "Id do autor não pode ser alterada no corpo da requisição");
        }else {
            autor.setId(id);
        }
        return ResponseEntity.ok(service.save(autor));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable("id")int id){
        if (!service.delete(id)) {
            throw new ModelValidationException("Erro na exclusão",
                    String.format("Autor com id=%d não encontrado",id));
        }
        return ResponseEntity.ok().build();
    }

}
