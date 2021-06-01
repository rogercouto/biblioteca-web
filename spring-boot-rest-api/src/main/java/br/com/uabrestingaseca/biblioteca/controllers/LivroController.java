package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Livro;
import br.com.uabrestingaseca.biblioteca.services.LivroService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/livros")
@Api("Endpoint para operações com livros")
public class LivroController {

    @Autowired
    private LivroService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Recupera uma lista de livros")
    public ResponseEntity<List<Livro>> index(
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="limit", defaultValue = "10") int limit,
        @RequestParam(value="find", defaultValue = "") String find
    ){
        Pageable pageable = PageRequest.of(page, limit);
        Page<Livro> livros = (find.isBlank()) ?
                service.findPage(pageable) :
                service.findPage(find, pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "X-Total-Count");
        responseHeaders.set("X-Total-Count", String.valueOf(livros.getTotalElements()));
        return ResponseEntity.ok().headers(responseHeaders).body(livros.toList());
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Recupera um livro com o id informado")
    public ResponseEntity<Livro> findById(@PathVariable("id")int id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Inclui um livro")
    public ResponseEntity<Livro> create(@Valid @RequestBody Livro livro){
        if (livro.getId() != null && livro.getId() != 0)
            throw new ModelValidationException("Erro na criação do livro","Id do livro é gerado pela API");
        return ResponseEntity.ok(service.save(livro));
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Atualiza o registro de um livro")
    public ResponseEntity<Livro> update(@PathVariable("id")int id,@Valid @RequestBody Livro livro){
        if (livro.getId() != null && !livro.getId().equals(id))
            throw new ModelValidationException("Erro na atualização do livro",
                    "Id do livro não pode ser alterado no corpo da requisição");
        livro.setId(id);
        return ResponseEntity.ok(service.save(livro));
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation("Exclui um livro (Somente admins)")
    public ResponseEntity<?> delete(@PathVariable("id")int id){
        if (!service.delete(id)) {
            throw new ModelValidationException("Erro na exclusão",
                    String.format("Livro com id=%d não encontrado",id));
        }
        return ResponseEntity.ok().build();
    }

}
