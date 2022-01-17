package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Autor;
import br.com.uabrestingaseca.biblioteca.model.Permissao;
import br.com.uabrestingaseca.biblioteca.model.Usuario;
import br.com.uabrestingaseca.biblioteca.security.jwt.JwtTokenProvider;
import br.com.uabrestingaseca.biblioteca.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UsuarioService service;

    @PostMapping(value = "/signin",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signIn(@RequestBody Usuario credenciais){
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(credenciais.getEmail(), credenciais.getSenha());
        try {
            authenticationManager.authenticate(authentication);
        }catch(BadCredentialsException e){
            throw new ModelValidationException(e.getMessage());
        }
        Usuario usuario = service.findUserByEmail(authentication.getName());
        usuario.setGerente(usuario.getRoles().contains("GERENTE"));
        if (credenciais.getNome() != null && usuario.getEmail().compareTo(credenciais.getNome())!= 0)
            throw new ModelValidationException("Erro de validação", "Nome de usuário incorreto");
        String token = tokenProvider.createToken(usuario.getUsername(), usuario.getRoles());
        Map<Object, Object> model = new LinkedHashMap<>();
        model.put("usuario", usuario);
        model.put("token", token);
        return ResponseEntity.ok(model);
    }

    @PostMapping(value="/signup",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signUp(@Valid @RequestBody Usuario usuario) {
        Usuario newUser = service.create(usuario);
        Map<Object, Object> model = new LinkedHashMap<>();
        model.put("message", "Usario criado");
        model.put("id", newUser.getId());
        return ResponseEntity.ok(model);
    }

    @GetMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Usuario>> index(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "10") int limit,
            @RequestParam(value="find", defaultValue = "") String find,
            @RequestParam(value="includeAdmin", defaultValue = "false") boolean includeAdmin) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Usuario> usuarios = (find.isBlank()) ?
                service.findAll(pageable):
                service.find(find, pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "X-Total-Count");
        responseHeaders.set("X-Total-Count", String.valueOf(usuarios.getTotalElements()));
        List<Usuario> users;
        if (includeAdmin){
            users =  usuarios.toList();
        }else{
            users =  usuarios.toList().stream()
                    .filter(u->!u.getRoles().contains("ADMIN"))
                    .collect(Collectors.toList());
        }
        users.stream().forEach(u->{
            u.setGerente(u.getRoles().contains("GERENTE"));
        });
        return ResponseEntity.ok().headers(responseHeaders).body(users);
    }

    @GetMapping(value = "/users/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Usuario> findById(@PathVariable("id")int id){
        Usuario u = service.findById(id);
        if (u.getRoles().contains("ADMIN")){
            throw new ModelValidationException("Não autorizado!",
                    "Dados desse usuário não podem ser exibidos!");
        }
        u.setGerente(u.getRoles().contains("GERENTE"));
        return ResponseEntity.ok(u);
    }

    @PostMapping(value="/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> insert(@Valid @RequestBody Usuario usuario) {
        Usuario newUser = service.create(usuario);
        Map<Object, Object> model = new LinkedHashMap<>();
        model.put("message", "Usario criado");
        model.put("id", newUser.getId());
        return ResponseEntity.ok(model);
    }

    @PutMapping(value = "/users/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Usuario> update(@PathVariable("id")int id, @RequestBody Usuario usuario){
        if (usuario.getId() != null && !usuario.getId().equals(id)){
            throw new ModelValidationException("Erro na atualização do usuário",
                    "Id do usuário não pode ser alterada no corpo da requisição");
        }else {
            usuario.setId(id);
        }
        return ResponseEntity.ok(service.save(usuario));
    }

    @DeleteMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable("id")int id){
        Usuario u = service.findById(id);
        if (u.getRoles().contains("ADMIN")){
            throw new ModelValidationException("Erro na exclusão do usuário",
                    "Administrador do sistema não pode ser excluido!");
        }
        if (!service.delete(id)) {
            throw new ModelValidationException("Erro na exclusão",
                    String.format("Autor com id=%d não encontrado",id));
        }
        return ResponseEntity.ok().build();
    }

}
