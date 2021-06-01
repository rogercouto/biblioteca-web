package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Secao;
import br.com.uabrestingaseca.biblioteca.services.SecaoService;
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
@RequestMapping("/secoes")
public class SecaoController {

    @Autowired
    private SecaoService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Secao>> index(
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="limit", defaultValue = "10") int limit,
        @RequestParam(value="find", defaultValue = "") String find
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Secao> secoes = (find.isBlank()) ?
                service.findAll(pageable):
                service.find(find, pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "X-Total-Count");
        responseHeaders.set("X-Total-Count", String.valueOf(secoes.getTotalElements()));
        return ResponseEntity.ok().headers(responseHeaders).body(secoes.toList());
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Secao> findById(@PathVariable("id")int id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Secao> create(@Valid @RequestBody Secao secao){
        if (secao.getId() != null) {
            throw new ModelValidationException("Erro na criação da secao",
                    "Id da secao é gerada pela API");
        }
        if (service.findFirstByNome(secao.getNome()) != null){
            throw new ModelValidationException("Erro na criação da secao",
                    "Já existe um secao com esse nome");
        }
        return ResponseEntity.ok(service.save(secao));
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Secao> update(@PathVariable("id")int id, @Valid @RequestBody Secao secao){
        if (secao.getId() != null && !secao.getId().equals(id)) {
            throw new ModelValidationException("Erro na atualização da secao",
                    "Id da secao não pode ser alterada no corpo da requisição");
        } else {
            secao.setId(id);
        }
        return ResponseEntity.ok(service.save(secao));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")int id){
        if (!service.delete(id)) {
            throw new ModelValidationException("Erro na exclusão",
                    String.format("Secao com id=%d não encontrada",id));
        }
        return ResponseEntity.ok().build();
    }

}
