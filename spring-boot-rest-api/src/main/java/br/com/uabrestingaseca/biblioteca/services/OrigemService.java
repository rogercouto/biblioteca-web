package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.model.Assunto;
import br.com.uabrestingaseca.biblioteca.model.Origem;
import br.com.uabrestingaseca.biblioteca.repositories.OrigemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OrigemService {

    @Autowired
    private OrigemRepository repository;

    public Origem findById(Integer id){
        return repository.findById(id).orElse(null);
    }

    public Page<Origem> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Page<Origem> find(String text, Pageable pageable){
        return repository.find(text, pageable);
    }

    /**
     * Recupera a primeira origem encontrada com a descrição passada
     * @param descricao da origem a ser recuperado
     * @return Origem
     */
    public Origem findFirstByDescricao(String descricao){
        return repository.findByDescricao(descricao)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public Origem save(Origem origem){
        return repository.save(origem);
    }

    @Transactional
    public boolean delete(int id){
        return repository.delete(id) > 0;
    }

}
