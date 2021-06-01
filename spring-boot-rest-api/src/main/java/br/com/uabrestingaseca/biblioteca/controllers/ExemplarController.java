package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Exemplar;
import br.com.uabrestingaseca.biblioteca.services.ExemplarService;
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
@RequestMapping("/exemplares")
public class ExemplarController {

    @Autowired
    private ExemplarService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Exemplar>> index(
        @RequestParam(value="livroId", defaultValue = "0") int livroId,
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="limit", defaultValue = "10") int limit,
        @RequestParam(value="all", defaultValue = "false")boolean all
    ){
        HttpHeaders responseHeaders = new HttpHeaders();
        List<Exemplar> list;
        if (livroId > 0){
            list = service.findByLivroId(livroId);
            responseHeaders.set("Access-Control-Expose-Headers", "X-Total-Count");
            responseHeaders.set("X-Total-Count", String.valueOf(list.size()));
            return ResponseEntity.ok().headers(responseHeaders).body(list);
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<Exemplar> exemplares = all ?
                service.findAll(pageable) :
                service.findDisponiveis(pageable);
        responseHeaders.set("Access-Control-Expose-Headers", "X-Total-Count");
        responseHeaders.set("X-Total-Count", String.valueOf(exemplares.getTotalElements()));
        list = exemplares.toList();
        list.forEach(e ->{
            if (e.getLivro() != null)
                e.setLivroId(e.getLivro().getId());
        });
        return ResponseEntity.ok().headers(responseHeaders).body(list);
    }

    @GetMapping(value = "/{numRegistro}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Exemplar> findByNumRegistro(@PathVariable("numRegistro")int numRegistro){
        return ResponseEntity.ok(service.findByNumRegistro(numRegistro));
    }

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Exemplar> create(@Valid @RequestBody Exemplar exemplar){
        Exemplar existent = service.findByNumRegistro(exemplar.getNumRegistro());
        if (existent != null){
            throw new ModelValidationException("Erro na criação do exemplar",
                    "Já existe um exemplar com esse número de registro");
        }
        return ResponseEntity.ok(service.save(exemplar));
    }

    @PutMapping(value="/{numRegistro}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Exemplar> update(
    @PathVariable("numRegistro")int numRegistro,
    @Valid @RequestBody Exemplar exemplar){
        if (exemplar.getNumRegistro() != null && !exemplar.getNumRegistro().equals(numRegistro)){
            throw new ModelValidationException("Erro na atualização do exemplar",
                    "Número de registro não pode ser alterado");
        }
        return ResponseEntity.ok(service.save(exemplar));
    }

    @DeleteMapping(value = "/{numRegistro}")
    public ResponseEntity<?> delete(@PathVariable("numRegistro")int numRegistro){
        if (!service.delete(numRegistro)){
            throw new ModelValidationException("Erro na exclusão do exemplar",
                    String.format("Nenhum exemplar com o número de registro = %d",numRegistro));
        }
        return ResponseEntity.ok().build();
    }

}
