package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Emprestimo;
import br.com.uabrestingaseca.biblioteca.model.Pendencia;
import br.com.uabrestingaseca.biblioteca.model.Usuario;
import br.com.uabrestingaseca.biblioteca.services.PendenciaService;
import br.com.uabrestingaseca.biblioteca.services.UsuarioService;
import br.com.uabrestingaseca.biblioteca.util.ModelUtil;
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
@RequestMapping("/pendencias")
public class PendenciasController {

    @Autowired
    private PendenciaService service;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping(value = "/adm", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Pendencia>> index(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "10") int limit,
            @RequestParam(value="filter", defaultValue = "") String filter){
        Pageable pageable = PageRequest.of(page, limit);
        Page<Pendencia> emprestimos;
        if (filter.toLowerCase().equals("inactive")||filter.toLowerCase().equals("inativas")){
            emprestimos = service.findPage(pageable, false);
        }else if (filter.toLowerCase().equals("active")||filter.toLowerCase().equals("ativas")){
            emprestimos = service.findPage(pageable, false);
        }else{
            emprestimos = service.findPage(pageable);
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "X-Total-Count");
        responseHeaders.set("X-Total-Count", String.valueOf(emprestimos.getTotalElements()));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(emprestimos.toList());
    }

    @GetMapping(value = "/usuario/{id}")
    public ResponseEntity<List<Pendencia>> findByUsuario(
            @PathVariable("id") int usuarioId,
            @RequestParam(value="filter", defaultValue = "") String filter
    ){
        Usuario usuario = usuarioService.findById(usuarioId);
        if (usuario == null){
            return ResponseEntity.notFound().build();
        }
        List<Pendencia> pendencias;
        if (filter.trim().length() > 0){
            if (filter.toLowerCase().compareTo("active") == 0 || filter.toLowerCase().compareTo("ativas") == 0){
                pendencias = service.findFromUsuario(usuario, true) ;
            }else if (filter.toLowerCase().compareTo("inactive") == 0 || filter.toLowerCase().compareTo("inativas") == 0){
                pendencias = service.findFromUsuario(usuario, false) ;
            }else{
                pendencias = service.findFromUsuario(usuario, null) ;
            }
        }else{
            pendencias = service.findFromUsuario(usuario, null) ;
        }
        return ResponseEntity.ok().body(pendencias);
    }

    @PutMapping(value = "/adm/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pendencia> update(@PathVariable("id") int id, @RequestBody Pendencia pendencia){
        System.out.println(id);
        if (pendencia.getId() != null && !pendencia.getId().equals(id)){
            throw new ModelValidationException("Erro ao atualizar pendencia",
                    "Id do empréstimo não pode ser alterado no corpo da requisição");
        }
        pendencia.setId(id);
        ModelUtil.printJson(pendencia);
        try {
            pendencia = service.save(pendencia);
        }catch (Exception e){
            ModelUtil.printJson(e);
        }
        return ResponseEntity.ok(pendencia);
    }

    @DeleteMapping(value = "/adm/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
