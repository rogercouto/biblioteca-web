package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Usuario;
import br.com.uabrestingaseca.biblioteca.security.jwt.JwtTokenProvider;
import br.com.uabrestingaseca.biblioteca.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

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
        authenticationManager.authenticate(authentication);
        Usuario usuario = service.findUserByEmail(authentication.getName());
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

}
