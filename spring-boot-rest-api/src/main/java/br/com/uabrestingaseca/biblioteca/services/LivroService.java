package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.model.Livro;
import br.com.uabrestingaseca.biblioteca.repositories.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {

    @Autowired
    private LivroRepository repository;

    public List<Livro> findAll(){
        return repository.findAll();
    }
}
