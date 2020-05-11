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
        return assunto.orElse(null);
    }

    public Assunto findFirstByDescricao(String descricao){
       return repository.findByDescricao(descricao.trim())
               .stream()
               .findFirst()
               .orElse(null);
    }

    public Assunto save(Assunto assunto){
        return repository.save(assunto);
    }

}
