package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Usuario;
import br.com.uabrestingaseca.biblioteca.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByEmail(username);
    }

    public Usuario findUserByEmail(String email) throws  UsernameNotFoundException{
        Usuario usuario = repository.findByUsername(email);
        if (usuario == null)
            throw new UsernameNotFoundException(String.format("E-mail %s não cadastrado", email));
        return usuario;
    }

    public Usuario findById(int id){
        return repository.findById(id).orElse(null);
    }

    public Usuario create(Usuario usuario){
        if (repository.findByUsername(usuario.getEmail()) != null)
            throw new ModelValidationException("Erro de validação nos dados","E-mail já cadastrado");
        usuario.setAtivo(true);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return repository.save(usuario);
    }
}
