package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Baixa;
import br.com.uabrestingaseca.biblioteca.model.Exemplar;
import br.com.uabrestingaseca.biblioteca.repositories.BaixaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BaixaService {

    @Autowired
    private BaixaRepository repository;

    @Autowired
    private ExemplarService exemplarService;

    public Baixa findById(int id){
        return repository.findById(id).orElse(null);
    }

    public Page<Baixa> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Baixa findBaixaFromExemplar(Exemplar exemplar){
        return repository.findBaixaFromExemplar(exemplar).orElse(null);
    }

    @Transactional
    public Baixa save(Baixa baixa){
        if (baixa.getExemplar() == null && baixa.getExemplarNumRegistro() != null){
            baixa.setExemplar(new Exemplar(baixa.getExemplarNumRegistro()));
        }
        if (baixa.getExemplar() == null){
            throw new ModelValidationException("Erro na inclusão da baixa",
                    "Exemplar deve ser informado");
        }
        if (baixa.getId() != null){
            throw new ModelValidationException("Erro na inclusão da baixa",
                    "Id da baixa é gerado pela API");
        }
        if (baixa.getExemplar().getNumRegistro() != null && !baixa.getExemplar().onlyNumRegistroSet()){
            throw new ModelValidationException("Erro na inclusão da baixa",
                    "Somente o campo numRegistro deve ser infomado para o exemplar");
        }
        Exemplar exemplar = exemplarService.findByNumRegistro(baixa.getExemplar().getNumRegistro());
        if (exemplar == null){
            throw new ModelValidationException("Erro na inclusão da baixa",
                    String.format("Exemplar com número de registro = %d não existe",
                            baixa.getExemplar().getNumRegistro()));
        }
        exemplar.setDisponivel(false);
        exemplar = exemplarService.save(exemplar);
        baixa.setExemplar(exemplar);
        return repository.save(baixa);
    }

    @Transactional
    public void delete(int id){
        Baixa baixa = repository.findById(id).orElse(null);
        if (baixa == null){
            throw new ModelValidationException("Erro na exclusão da baixa",
                    String.format("Baixa com Id=%d não existe", id));
        }
        Exemplar exemplar = baixa.getExemplar();
        repository.delete(baixa);
        exemplar.setDisponivel(true);
        baixa.setExemplar(exemplar);
    }


}
