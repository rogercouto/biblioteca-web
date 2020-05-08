package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.model.Assunto;
import br.com.uabrestingaseca.biblioteca.model.Autor;
import br.com.uabrestingaseca.biblioteca.repositories.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {
    
    @Autowired
    private AutorRepository repository;

    public Autor findById(Integer id){
        Optional<Autor> autor = repository.findById(id);
        if (autor.isPresent())
            return autor.get();
        return null;
    }

    public Autor findByNomes(String nome, String sobrenome){
        Optional<Autor> autor = repository.findByNames(nome.trim(), sobrenome.trim()).stream().findFirst();
        if (autor.isPresent())
            return autor.get();
        return null;
    }

    public Autor save(Autor autor){
        return repository.save(autor);
    }
    
}
