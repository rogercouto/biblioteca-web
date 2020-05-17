package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Reserva;
import br.com.uabrestingaseca.biblioteca.services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Reserva>> index(
    @RequestParam(value="page", defaultValue = "0") int page,
    @RequestParam(value="limit", defaultValue = "10") int limit,
    @RequestParam(value="filter", defaultValue = "active") String filter){
        Pageable pageable = PageRequest.of(page, limit);
        Page<Reserva> reservas;
        if (filter.toLowerCase().equals("inactive")||filter.toLowerCase().equals("inativas")){
            reservas = service.findFiltered(false, pageable);
        }else if (filter.toLowerCase().equals("all")||filter.toLowerCase().equals("todas")){
            reservas = service.findAll(pageable);
        }else{
            reservas = service.findFiltered(true, pageable);
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Total-Count", String.valueOf(reservas.getTotalElements()));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(reservas.toList());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Reserva findById(@PathVariable("id") int id){
        return service.findById(id);
    }

    @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public Reserva create(@RequestBody Reserva reserva){
        return service.create(reserva);
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Reserva update(@PathVariable("id")int id, @RequestBody Reserva reserva){
        if (reserva.getId() != null && !reserva.getId().equals(id)){
            throw new ModelValidationException("Erro ao atualizar reserva",
                    "Id da reserva não pode ser alterado no corpo da requisição");
        }
        reserva.setId(id);
        return service.update(reserva);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }


}
