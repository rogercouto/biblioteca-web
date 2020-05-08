package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.model.Assunto;
import br.com.uabrestingaseca.biblioteca.repositories.AssuntoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssuntoService {

    @Autowired
    private AssuntoRepository repository;

    public Assunto findById(Integer id){
        Optional<Assunto> assunto = repository.findById(id);
        if (assunto.isPresent())
            return assunto.get();
        return null;
    }

    public Assunto findByDescricao(String descricao){
        return repository.findByDescricao(descricao.trim());
    }

    public Assunto save(Assunto assunto){
        return repository.save(assunto);
    }

}
