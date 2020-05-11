package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.model.Categoria;
import br.com.uabrestingaseca.biblioteca.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public Categoria findById(Integer id){
        return repository.findById(id).orElse(null);
    }

    public Categoria findFirstByDescricao(String descricao){
        return repository.findByDescricao(descricao.trim())
                .stream()
                .findFirst()
                .orElse(null);
    }

    public Categoria save(Categoria categoria){
        return repository.save(categoria);
    }

}
