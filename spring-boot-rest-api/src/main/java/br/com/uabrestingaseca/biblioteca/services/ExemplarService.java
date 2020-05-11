package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.exceptions.ValidationException;
import br.com.uabrestingaseca.biblioteca.model.Exemplar;
import br.com.uabrestingaseca.biblioteca.model.Livro;
import br.com.uabrestingaseca.biblioteca.repositories.ExemplarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExemplarService {

    @Autowired
    private ExemplarRepository repository;

    public List<Exemplar> findAll(){
        return repository.findAll();
    }

    public Exemplar findByNumRegistro(Integer numRegistro){
        return repository.findByNumRegistro(numRegistro)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public Exemplar save(Exemplar exemplar){
        return repository.save(exemplar);
    }

    public void saveExemplares(List<Exemplar> exemplares, final Livro livro, String messageIfError){
        livro.setExemplares(
                exemplares.stream()
                .map(e -> {
                    if (e.onlyNumRegistro()){
                        Exemplar exemplar = findByNumRegistro(e.getNumRegistro());
                        if (exemplar == null){
                            throw new ValidationException(messageIfError,
                                    "Nenhum exemplar com esse número de registro");
                        }else if (!exemplar.getLivro().getId().equals(livro.getId())) {
                            throw new ValidationException(messageIfError,
                                    "Número do(s) exemplar(es) já cadastrado(s) para outro livro");
                        }
                        return exemplar;
                    }else{
                        e.setLivro(livro);
                        return save(e);
                    }
                })
                .collect(Collectors.toList())
        );
    }

}
