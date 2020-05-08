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
        Optional<Categoria> categoria = repository.findById(id);
        if (categoria.isPresent())
            return categoria.get();
        return null;
    }

    public Categoria findByDescricao(String descricao){
        return repository.findByDescricao(descricao.trim());
    }

    public Categoria save(Categoria categoria){
        return repository.save(categoria);
    }

}
