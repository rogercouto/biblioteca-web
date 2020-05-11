package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.model.Autor;
import br.com.uabrestingaseca.biblioteca.repositories.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorService {
    
    @Autowired
    private AutorRepository repository;

    public Autor findById(Integer id){
        return repository.findById(id).orElse(null);
    }

    /**
     * Busca por um autor pela combinação de nome e sobrenome
     * @param nome do autor
     * @param sobrenome do autor
     * @return Autor resultante ou null
     */
    public Autor findFirstByNomes(String nome, String sobrenome){
        return repository.findByNames(nome.trim(), sobrenome.trim())
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
            return repository.findByNames(autor.getNome().trim(),autor.getSobrenome().trim())
                    .stream()
                    .findFirst()
                    .orElse(repository.save(autor));
        }else if (!autor.onlyIdSet()){
            return repository.save(autor);
        }
        return autor;
    }
    
}
