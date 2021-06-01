package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.*;
import br.com.uabrestingaseca.biblioteca.repositories.LivroRepository;
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
    private AssuntoService assuntoService;

    @Autowired
    private AutorService autorService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ExemplarService exemplarService;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public Page<Livro> findPage(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Page<Livro> findPage(String text, Pageable pageable){
        return repository.findByText(text.toLowerCase(), pageable);
    }

    public Page<Livro> findByAssunto(Assunto assunto, Pageable pageable){
        return repository.findByAssunto(assunto, pageable);
    }

    public Livro findById(Integer id){
        Optional<Livro> livro = repository.findById(id);
        return livro.orElse(null);
    }

    private void saveEditora(Livro livro){
        if (!livro.getEditora().onlyIdSet()){
            Editora editora = editoraService.findFirstByNome(livro.getEditora().getNome());
            if (editora == null) {
                editora = editoraService.save(livro.getEditora());
            }
            livro.setEditora(editora);
        }
    }

    private void saveAutores(Livro livro){
        List<Autor> autores = livro.getAutores().stream()
                .map(a -> {
                    if (!a.onlyIdSet()){
                        if (a.getId() == null){
                           Autor autor = autorService.findFirstByNomes(a.getNome(), a.getSobrenome());
                           if (autor != null){
                               a.setId(autor.getId());
                           }
                        }
                        return autorService.save(a);
                    }
                    return a;
                })
                .collect(Collectors.toList());
        livro.setAutores(autores);
    }

    private void saveCategorias(Livro livro){
        List<Categoria> savedCategorias = livro.getCategorias()
                .stream()
                .map(c ->{
                    if (!c.onlyIdSet()){
                        Categoria cat = categoriaService.findFirstByDescricao(c.getDescricao());
                        if (cat == null){
                            return categoriaService.save(c);
                        }
                        return cat;
                    }
                    return c;
                })
                .collect(Collectors.toList());
        livro.setCategorias(savedCategorias);
    }

    private void validateEditora(Livro livro, Set<String> errors){
        if (livro.getEditora() != null){
            if (livro.getEditora().getId() != null){
                Editora editora = editoraService.findById(livro.getEditora().getId());
                if (editora == null){
                    errors.add(String.format("Nenhuma editora com o id = %d",livro.getEditora().getId()));
                }else if (livro.getEditora().onlyIdSet()){
                    livro.setEditora(editora);
                }
            }else{
                Set<ConstraintViolation<Editora>> violations = validator.validate(livro.getEditora());
                violations.forEach(v ->{
                    errors.add(v.getMessage());
                });
            }
        }
    }

    private void validateAssunto(Livro livro, Set<String> errors){
        if (livro.getAssunto() != null){
            if (livro.getAssunto().getId() != null){
                Assunto assunto = assuntoService.findById(livro.getAssunto().getId());
                if (assunto == null){
                    errors.add(String.format("Nenhum assunto com o id = %d",livro.getAssunto().getId()));
                }else if (livro.getAssunto().onlyIdSet()){
                    livro.setAssunto(assunto);
                }
            }else{
                errors.add(String.format("Id do assunto não informado",livro.getAssunto().getId()));
            }
        }
    }

    private void validateAutores(Livro livro, Set<String> errors){
        List<Autor> autores = livro.getAutores().stream()
            .map(a->{
                if (a.getId() != null){
                    Autor autor = autorService.findById(a.getId());
                    if (autor == null){
                        errors.add(String.format("Nenhum autor com o id = %d", a.getId()));
                    }else if (a.onlyIdSet()){
                        return autor;
                    }
                }else{
                    Set<ConstraintViolation<Autor>> violations = validator.validate(a);
                    violations.forEach(v ->{
                        errors.add(v.getMessage());
                    });
                }
                return a;
            })
            .collect(Collectors.toList());
        livro.setAutores(autores);
    }

    private void validateCategorias(Livro livro, Set<String> errors){
        List<Categoria> categorias = livro.getCategorias()
                .stream()
                .map(c -> {
                    if (c.getId() != null){
                        Categoria categoria = categoriaService.findById(c.getId());
                        if (categoria == null){
                            errors.add(String.format("Nenhuma categoria com o id = %d", c.getId()));
                        }else if (c.onlyIdSet()){
                            return categoria;
                        }
                    }else{
                        Set<ConstraintViolation<Categoria>> violations = validator.validate(c);
                        violations.forEach(v ->{
                            errors.add(v.getMessage());
                        });
                    }
                    return c;
                })
                .collect(Collectors.toList());
        livro.setCategorias(categorias);
    }

    private void validateExemplares(Livro livro, Set<String> errors){
        livro.getExemplares().forEach(e -> {
            Exemplar exemplar = e.copy();
            exemplar.setLivro(new Livro(livro.getId()));
            //Se estiver tentando criar um livro novo com um exemplar já existente ele deve retornar um erro
            Exemplar existent = exemplar.getNumRegistro() != null ?
                    exemplarService.findByNumRegistro(exemplar.getNumRegistro()) :
                    null;
            if ((livro.getId() == null && existent != null)
            || (livro.getId() != null && existent != null && !livro.getId().equals(existent.getLivro().getId()))){
                errors.add(String.format("Exemplar já cadastrado com o número de registro = %d", e.getNumRegistro()));
            }else if (!exemplar.onlyLivroAndNumRegistroSet()){
                Set<ConstraintViolation<Exemplar>> violations = validator.validate(exemplar);
                violations.forEach(v ->{
                    errors.add(v.getMessage());
                });
            }
        });
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
        validateEditora(livro, errors);
        validateAssunto(livro, errors);
        validateAutores(livro, errors);
        validateCategorias(livro, errors);
        validateExemplares(livro, errors);
        if (errors.size()  > 0){
            throw new ModelValidationException(messageIfError, new LinkedList<>(errors));
        }
    }

    @Transactional
    public Livro save(final Livro livro){
        String messageIfError = livro.getId() != null ?
                "Erro na atualização do livro" :
                "Erro na inclusão do livro";
        validate(livro, messageIfError);
        saveEditora(livro);
        saveAutores(livro);
        saveCategorias(livro);
        List<Exemplar> exemplares = livro.getExemplares();
        livro.setExemplares(new LinkedList<>());
        Livro savedLivro = repository.save(livro);
        exemplarService.saveExemplares(exemplares, savedLivro, messageIfError);
        return savedLivro;
    }

    @Transactional
    public boolean delete(int id){
        Livro livro = repository.findById(id).orElse(null);
        if (livro == null)
            return false;
        livro.getExemplares().forEach(e->exemplarService.delete(e.getNumRegistro()));
        repository.delete(livro);
        return true;
    }
}
