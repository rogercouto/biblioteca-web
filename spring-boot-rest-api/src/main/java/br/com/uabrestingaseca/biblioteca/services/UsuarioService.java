package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Permissao;
import br.com.uabrestingaseca.biblioteca.model.Usuario;
import br.com.uabrestingaseca.biblioteca.repositories.UsuarioRepository;
import br.com.uabrestingaseca.biblioteca.util.ModelUtil;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Usuario> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Page<Usuario> find(String text, Pageable pageable){
        return repository.findPage(text, pageable);
    }

    public Usuario findById(Integer id){
        return repository.findById(id).orElse(null);
    }

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
        if (usuario.isGerente()){
            usuario.getPermissoes().add(new Permissao(2));
        }
        usuario.getPermissoes().add(new Permissao(3));
        if (repository.findByUsername(usuario.getEmail()) != null)
            throw new ModelValidationException("Erro de validação nos dados","E-mail já cadastrado");
        usuario.setAtivo(true);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return repository.save(usuario);
    }

    public Usuario save(Usuario usuario){
        if (usuario.getId() == null){
            throw new ModelValidationException("Erro de validação nos dados","Usuário não encontrado");
        }
        Usuario sUsuario = findById(usuario.getId());
        sUsuario.setPermissoes(new LinkedList<Permissao>());
        if (usuario.isGerente()){
            sUsuario.getPermissoes().add(new Permissao(2));
        }
        sUsuario.getPermissoes().add(new Permissao(3));
        if (sUsuario != null){
            if (usuario.getNome() != null){
                sUsuario.setNome(usuario.getNome());
            }
            if (usuario.getEmail() != null && sUsuario.getEmail().compareTo(usuario.getEmail()) != 0){
                if (repository.findByUsername(usuario.getEmail()) != null)
                    throw new ModelValidationException("Erro de validação nos dados","E-mail já cadastrado");
                sUsuario.setEmail(usuario.getEmail());
            }
            if (usuario.getSenha() != null){
                sUsuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            }
            if (usuario.getNumTel() != null){
                sUsuario.setNumTel(usuario.getNumTel());
            }
            if (usuario.isAtivo() != null){
                sUsuario.setAtivo(usuario.isAtivo());
            }
            if (usuario.isGerente() != null){
                sUsuario.setGerente(usuario.isGerente());
            }
            return repository.save(sUsuario);
        }
        throw new ModelValidationException("Erro interno","Usuário não encontrado!");
    }

    @Transactional
    public boolean delete(int id){
        return repository.delete(id) > 0;
    }
}
