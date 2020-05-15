package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Baixa;
import br.com.uabrestingaseca.biblioteca.model.Exemplar;
import br.com.uabrestingaseca.biblioteca.model.Livro;
import br.com.uabrestingaseca.biblioteca.repositories.ExemplarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExemplarService {

    @Autowired
    private ExemplarRepository repository;

    @Autowired
    private BaixaService baixaService;

    private Exemplar setLivroIdAndBaixa(Exemplar e){
        if (e.getLivro() != null)
            e.setLivroId(e.getLivro().getId());
        if (!e.getDisponivel()){
            Baixa b = baixaService.findBaixaFromExemplar(e);
            b.setExemplar(null);
            e.setBaixa(b);
        }
        return e;
    }

    public Page<Exemplar> findAll(Pageable pageable){
        Page<Exemplar> page =  repository.findAll(pageable);
        page.forEach(this::setLivroIdAndBaixa);
        return page;
    }

    public Exemplar findByNumRegistro(Integer numRegistro){
        return repository.findByNumRegistro(numRegistro)
                .stream()
                .map(this::setLivroIdAndBaixa)
                .findFirst()
                .orElse(null);
    }

    public Exemplar save(Exemplar exemplar){
        if (exemplar.getLivro() == null && exemplar.getLivroId() != null){
            exemplar.setLivro(new Livro(exemplar.getLivroId()));
        }
        return repository.save(exemplar);
    }

    public void saveExemplares(List<Exemplar> exemplares, final Livro livro, String messageIfError){
        livro.setExemplares(
                exemplares.stream()
                .map(e -> {
                    if (e.onlyNumRegistroSet()){
                        Exemplar exemplar = findByNumRegistro(e.getNumRegistro());
                        if (exemplar == null){
                            throw new ModelValidationException(messageIfError,
                                    String.format("Nenhum exemplar com o número de registro = %d",e.getNumRegistro()));
                        }else if (!exemplar.getLivro().getId().equals(livro.getId())) {
                            throw new ModelValidationException(messageIfError,
                                    String.format("Exemplar já cadastrado com o número de registro = %d",e.getNumRegistro()));
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

    @Transactional
    public boolean delete(int numRegistro){
        return repository.delete(numRegistro) > 0;
    }

}
