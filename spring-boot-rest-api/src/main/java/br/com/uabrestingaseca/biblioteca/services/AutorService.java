package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.model.Autor;
import br.com.uabrestingaseca.biblioteca.repositories.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AutorService {
    
    @Autowired
    private AutorRepository repository;

    public Autor findById(Integer id){
        return repository.findById(id).orElse(null);
    }

    public Page<Autor> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Page<Autor> find(String text, Pageable pageable){
        return repository.findPage(text, pageable);
    }

    /**
     * Busca por um autor pela combinação de nome e sobrenome
     * @param nome do autor
     * @param sobrenome do autor
     * @return Autor resultante ou null
     */
    public Autor findFirstByNomes(String nome, String sobrenome){
        return repository.findByNames(nome, sobrenome)
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Inclui ou atualiza um autor no banco se já não existir um com o mesmo nome e sobrenome
     * @param autor a ser salvo
     * @return Autor salvo
     */
    public Autor save(Autor autor){
        if (autor.getId() == null){
            return repository.findByNames(autor.getNome(),autor.getSobrenome())
                    .stream()
                    .findFirst()
                    .orElse(repository.save(autor));
        }else if (!autor.onlyIdSet()){
            return repository.save(autor);
        }
        return autor;
    }

    @Transactional
    public boolean delete(int id){
        return repository.delete(id) > 0;
    }
    
}
