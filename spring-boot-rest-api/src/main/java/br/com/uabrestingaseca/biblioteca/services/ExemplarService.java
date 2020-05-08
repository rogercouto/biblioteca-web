package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.model.Exemplar;
import br.com.uabrestingaseca.biblioteca.model.Livro;
import br.com.uabrestingaseca.biblioteca.model.Origem;
import br.com.uabrestingaseca.biblioteca.model.Secao;
import br.com.uabrestingaseca.biblioteca.repositories.ExemplarRepository;
import br.com.uabrestingaseca.biblioteca.repositories.LivroRepository;
import br.com.uabrestingaseca.biblioteca.repositories.OrigemRepository;
import br.com.uabrestingaseca.biblioteca.repositories.SecaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExemplarService {

    @Autowired
    private ExemplarRepository repository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private SecaoRepository secaoRepository;

    @Autowired
    private OrigemRepository origemRepository;

    public List<Exemplar> findAll(){
        return repository.findAll();
    }

    public Exemplar save(Exemplar exemplar){
        Optional<Livro> livro = livroRepository.findById(exemplar.getLivro().getId());
        livro.ifPresent(exemplar::setLivro);
        Optional<Secao> secao = secaoRepository.findById(exemplar.getSecao().getId());
        secao.ifPresent(exemplar::setSecao);
        Optional<Origem> origem = origemRepository.findById(exemplar.getOrigem().getId());
        origem.ifPresent(exemplar::setOrigem);
        return repository.save(exemplar);
    }

}
