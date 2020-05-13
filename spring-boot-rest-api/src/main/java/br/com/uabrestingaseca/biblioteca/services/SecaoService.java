package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.model.Editora;
import br.com.uabrestingaseca.biblioteca.model.Secao;
import br.com.uabrestingaseca.biblioteca.repositories.SecaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SecaoService {

    @Autowired
    private SecaoRepository repository;

    public Page<Secao> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Page<Secao> find(String text, Pageable pageable){
        return repository.find(text, pageable);
    }

    public Secao findById(Integer id){
        return repository.findById(id).orElse(null);
    }

    public Secao findFirstByNome(String nome){
        return repository.findByNome(nome)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public Secao save(Secao secao){
        return repository.save(secao);
    }

    @Transactional
    public boolean delete(int id){
        return repository.delete(id) > 0;
    }
    
}
