package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.model.Categoria;
import br.com.uabrestingaseca.biblioteca.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public Categoria findById(Integer id){
        return repository.findById(id).orElse(null);
    }

    public Categoria findFirstByDescricao(String descricao){
        return repository.findByDescricao(descricao)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public Page<Categoria> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Page<Categoria> find(String text, Pageable pageable){
        return repository.find(text, pageable);
    }

    public Categoria save(Categoria categoria){
        return repository.save(categoria);
    }

    @Transactional
    public boolean delete(int id){
        return repository.delete(id) > 0;
    }

}
