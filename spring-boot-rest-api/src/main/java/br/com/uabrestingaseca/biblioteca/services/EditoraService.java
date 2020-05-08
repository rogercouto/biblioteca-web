package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.model.Editora;
import br.com.uabrestingaseca.biblioteca.repositories.EditoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EditoraService {

    @Autowired
    private EditoraRepository repository;

    public Editora findById(Integer id){
        Optional<Editora> editora = repository.findById(id);
        if (editora.isPresent())
            return editora.get();
        return null;
    }

    public Editora findByNome(String nome){
        return  repository.findByNome(nome.trim());
    }

    public Editora save(Editora editora){
        return repository.save(editora);
    }

}
