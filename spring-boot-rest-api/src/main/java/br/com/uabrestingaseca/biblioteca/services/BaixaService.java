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
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

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
        return repository.findBaixaFromExemplar(exemplar)
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Custom model validator
     * @param baixa a ser validada
     * throws ModelValidation if not valid
     */
    public Exemplar validateAndGetExemplar(Baixa baixa){
        Set<String> errors = new TreeSet<>();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Baixa>> violations = validator.validate(baixa);
        violations.forEach(v ->{
            errors.add(v.getMessage());
        });
        if (baixa.getExemplar() == null){
            errors.add("Exemplar deve ser iformado");
        }
        if (baixa.getId() != null){
            errors.add("Id da baixa é gerada pela API");
        }
        if (baixa.getExemplar().getNumRegistro() != null && !baixa.getExemplar().onlyNumRegistroSet()){
            errors.add("Somente o campo numRegistro deve ser infomado para o exemplar");
        }
        Exemplar exemplar = exemplarService.findByNumRegistro(baixa.getExemplar().getNumRegistro());
        if (exemplar == null){
            errors.add(String.format(
                    "Exemplar com número de registro = %d não existe",
                            baixa.getExemplar().getNumRegistro()));
        }else if (!exemplar.isDisponivel()){
            errors.add("Baixa já lançada para o exemplar");
        }
        if (errors.size()  > 0){
            throw new ModelValidationException("Erro na inclusão da baixa", new LinkedList<>(errors));
        }
        return exemplar;
    }

    @Transactional
    public Baixa save(Baixa baixa){
        if (baixa.getExemplar() == null && baixa.getExemplarNumRegistro() != null){
            baixa.setExemplar(new Exemplar(baixa.getExemplarNumRegistro()));
        }
        Exemplar exemplar = validateAndGetExemplar(baixa);
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
        exemplarService.save(exemplar);
    }


}
