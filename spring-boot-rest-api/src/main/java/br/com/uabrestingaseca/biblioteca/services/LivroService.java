package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.exceptions.ValidationException;
import br.com.uabrestingaseca.biblioteca.model.Autor;
import br.com.uabrestingaseca.biblioteca.model.Editora;
import br.com.uabrestingaseca.biblioteca.model.Exemplar;
import br.com.uabrestingaseca.biblioteca.model.Livro;
import br.com.uabrestingaseca.biblioteca.repositories.LivroRepository;
import br.com.uabrestingaseca.biblioteca.util.ModelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LivroService {

    @Autowired
    private LivroRepository repository;

    @Autowired
    private EditoraService editoraService;

    @Autowired
    private AutorService autorService;

    @Autowired
    private ExemplarService exemplarService;

    public Page<Livro> findPage(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Page<Livro> findPage(String text, Pageable pageable){
        return repository.findByText(text.trim().toLowerCase(), pageable);
    }

    public Livro findById(Integer id){
        Optional<Livro> livro = repository.findById(id);
        return livro.orElse(null);
    }

    private List<Autor> saveAutores(List<Autor> autores){
        return autores.stream()
                .map(a -> autorService.save(a))
                .collect(Collectors.toList());
    }

    /**
     * Validador customizado para livro, valida livro e seus exemplares
     * @param livro a ser validado
     */
    private void validate(Livro livro, String messageIfError){
        Set<String> errors = new LinkedHashSet<>();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Livro>> violations = validator.validate(livro);
        violations.forEach(v ->{
            errors.add(v.getMessage());
        });
        livro.getExemplares().forEach(e -> {
            Exemplar exemplar = e.copy();
            exemplar.setLivro(new Livro(livro.getId()));
            if (!exemplar.onlyLivroAndNumRegistro()){
                Set<ConstraintViolation<Exemplar>> eViolations = validator.validate(exemplar);
                eViolations.forEach(v ->{
                    errors.add(v.getMessage());
                });
            }
        });
        if (errors.size()  > 0){
            throw new ValidationException(messageIfError, new LinkedList<>(errors));
        }
    }

    @Transactional
    public Livro save(Livro livro){
        String messageIfError = livro.getId() != null ?
                "Erro na atualização do livro" :
                "Erro na inclusão do livro";
        validate(livro, messageIfError);
        Editora editora = editoraService.save(livro.getEditora());
        livro.setEditora(editora);
        List<Autor> autores = saveAutores(livro.getAutores());
        livro.setAutores(autores);
        List<Exemplar> exemplares = livro.getExemplares();
        livro.setExemplares(null);
        livro = repository.save(livro);
        exemplarService.saveExemplares(exemplares, livro, messageIfError);
        return livro;
    }
}
