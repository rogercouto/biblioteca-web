package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.model.Emprestimo;
import br.com.uabrestingaseca.biblioteca.services.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {
    
    @Autowired
    private EmprestimoService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Emprestimo>> index(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "10") int limit){
        Pageable pageable = PageRequest.of(page, limit);
        Page<Emprestimo> reservas = service.findAll(pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Total-Count", String.valueOf(reservas.getTotalElements()));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(reservas.toList());
    }
    
}
