package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.model.Editora;
import br.com.uabrestingaseca.biblioteca.model.Livro;
import br.com.uabrestingaseca.biblioteca.repositories.EditoraRepository;
import br.com.uabrestingaseca.biblioteca.util.ModelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class EditoraService {

    @Autowired
    private EditoraRepository repository;

    public Editora findById(Integer id){
        return repository.findById(id).orElse(null);
    }

    public Editora findFirstByNome(String nome){
        return repository.findByNome(nome)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public Page<Editora> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Page<Editora> find(String text, Pageable pageable){
        return repository.find(text, pageable);
    }
    /**
     * Inclui ou atualiza uma editora no banco se já não existe uma com o mesmo nome
     * @param editora pra salvar
     * @return Editora salva
     */
    public Editora save(Editora editora){
        if (editora.getId() == null){
            return repository.findByNome(editora.getNome())
                    .stream()
                    .findFirst()
                    .orElse(repository.save(editora));
        }else if (!editora.onlyIdSet()){
            return repository.save(editora);
        }
        return editora;
    }

    @Transactional
    public boolean delete(int id){
        return repository.delete(id) > 0;
    }

}
