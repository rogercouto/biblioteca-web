package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Categoria;
import br.com.uabrestingaseca.biblioteca.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Categoria>> index(
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="limit", defaultValue = "10") int limit,
        @RequestParam(value="find", defaultValue = "") String text
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Categoria> categorias = (text.isBlank()) ?
                service.findAll(pageable):
                service.find(text, pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Total-Count", String.valueOf(categorias.getTotalElements()));
        return ResponseEntity.ok().headers(responseHeaders).body(categorias.toList());
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Categoria> findById(@PathVariable(value="id") int id) {
        return  ResponseEntity.ok(service.findById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Categoria> create(@Valid @RequestBody Categoria categoria){
        if (categoria.getId() != null) {
            throw new ModelValidationException("Erro na criação da categoria",
                    "Id da categoria é gerada pela API");
        }
        return ResponseEntity.ok(service.save(categoria));
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Categoria> update(@PathVariable("id")int id, @Valid @RequestBody Categoria categoria){
        if (categoria.getId() != null && !categoria.getId().equals(id)) {
            throw new ModelValidationException("Erro na atualização da categoria",
                    "Id da categoria não pode ser alterada no corpo da requisição");
        } else {
            categoria.setId(id);
        }
        return ResponseEntity.ok(service.save(categoria));
    }

    @DeleteMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable("id")int id){
        if (!service.delete(id)) {
            throw new ModelValidationException("Erro na exclusão",
                    String.format("Categoria com id=%d não encontrada",id));
        }
        return ResponseEntity.ok().build();
    }

}
