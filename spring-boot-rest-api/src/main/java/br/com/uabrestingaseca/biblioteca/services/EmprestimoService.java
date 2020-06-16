package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.model.Emprestimo;
import br.com.uabrestingaseca.biblioteca.model.Exemplar;
import br.com.uabrestingaseca.biblioteca.model.Usuario;
import br.com.uabrestingaseca.biblioteca.repositories.EmprestimoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository repository;

    @Autowired
    private ExemplarService exemplarService;

    @Autowired
    private UsuarioService usuarioService;

    public Emprestimo findById(int id){
        return repository.findById(id).orElse(null);
    }

    public Page<Emprestimo> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Page<Emprestimo> find(boolean ativos, Pageable pageable){
        return ativos ?
                repository.findAtivos(pageable) :
                repository.findDevolvidos(pageable);
    }

    public Page<Emprestimo> findFromExemplar(Exemplar exemplar, Pageable pageable){
        return repository.findFromExemplar(exemplar, pageable);
    }

    public Page<Emprestimo> findFromUsuario(Usuario usuario, Pageable pageable){
        return repository.findFromUsuario(usuario, pageable);
    }





}
