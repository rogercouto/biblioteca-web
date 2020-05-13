package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.model.Assunto;
import br.com.uabrestingaseca.biblioteca.repositories.AssuntoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AssuntoService {

    @Autowired
    private AssuntoRepository repository;

    public Assunto findById(Integer id){
        Optional<Assunto> assunto = repository.findById(id);
        return assunto.orElse(null);
    }

    public Page<Assunto> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Page<Assunto> find(String text, Pageable pageable){
        return repository.find(text, pageable);
    }

    /**
     * Recupera o primeiro assunto encontrado com a descrição passada
     * @param descricao do assunto a ser recuperado
     * @return Assunto
     */
    public Assunto findFirstByDescricao(String descricao){
       return repository.findByDescricao(descricao)
               .stream()
               .findFirst()
               .orElse(null);
    }

    public Assunto save(Assunto assunto){
        return repository.save(assunto);
    }

    @Transactional
    public boolean delete(int id){
        return repository.delete(id) > 0;
    }

}
