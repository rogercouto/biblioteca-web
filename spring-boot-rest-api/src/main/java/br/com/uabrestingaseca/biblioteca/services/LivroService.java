package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.exceptions.ValidationException;
import br.com.uabrestingaseca.biblioteca.model.*;
import br.com.uabrestingaseca.biblioteca.repositories.LivroRepository;
import br.com.uabrestingaseca.biblioteca.util.ModelUtil;
import io.swagger.models.auth.In;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

import static br.com.uabrestingaseca.biblioteca.util.ModelUtil.isNullOrEmpty;
import static br.com.uabrestingaseca.biblioteca.util.ModelUtil.isSomeNullOrEmpty;

@Service
public class LivroService {

    @Autowired
    private LivroRepository repository;

    @Autowired
    private EditoraService editoraService;

    @Autowired
    private AssuntoService assuntoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private AutorService autorService;

    @Autowired
    private ExemplarService exemplarService;

    public List<Livro> findAll(){
        return repository.findAll();
    }

    public Livro findById(Integer id){
        Optional<Livro> livro = repository.findById(id);
        if (livro.isPresent())
            return livro.get();
        return null;
    }
    /**
     * Validação customizada de livro
     * @param livro para validação
     */
    public void validate(Livro livro){
        Set<String> errors = new LinkedHashSet<>();
        if (isNullOrEmpty(livro.getTitulo()))
            errors.add("Título do livro deve ser informado");
        if (livro.getEditora() != null
        && livro.getEditora().getId() == null
        && isNullOrEmpty(livro.getEditora().getNome())){
            errors.add("Nome ou id da editora deve ser informado");
        }
        if (livro.getAssunto() != null
        && livro.getAssunto().getId() == null
        && isNullOrEmpty(livro.getAssunto().getDescricao())){
            errors.add("Descrição ou id do assunto deve ser informado");
        }
        if (livro.getAutores() != null){
            long count = livro.getAutores()
                    .stream()
                    .filter(a -> a.getId() == null && isSomeNullOrEmpty(a.getNome(), a.getSobrenome()))
                    .count();
            if (count > 0)
                errors.add("Nome e Sobrenome ou id do(s) autor(es) deve(m) ser informado(s)");
        }
        if (livro.getCategorias() != null){
            long count = livro.getCategorias()
                    .stream()
                    .filter(c -> c.getId() == null && isNullOrEmpty(c.getDescricao()))
                    .count();
            if (count > 0)
                errors.add("Descrição ou id da(s) categoria(s) deve(m) ser informado(s)");
        }
        /*
        if (livro.getExemplares() != null){
            long count = livro.getExemplares()
                    .stream()
                    .filter(e -> e.getNumRegistro() == null)
                    .count();
            if (count > 0)
                errors.add("Número(s) de registro do(s) exemplar(es) deve(m) ser informado(s)");
        }
        */
        if (errors.size() > 0)
            throw new ValidationException("Erro na inclusão do livro", new LinkedList<>(errors));
    }

    private void findOrSaveEditora(Livro livro){
        if (livro.getEditora() != null){
            Editora editora;
            if (livro.getEditora().getId() != null){
                editora = editoraService.findById(livro.getEditora().getId());
                if (editora == null){
                    throw new ValidationException("Erro na inclusão do livro",
                            String.format("Nenhuma editora com o id = %d", livro.getEditora().getId()));
                }else{
                    if (!isNullOrEmpty(livro.getEditora().getNome()))
                        editora = editoraService.save(livro.getEditora());//atualiza editora existente
                }
            }else{
                editora = editoraService.findByNome(livro.getEditora().getNome());
                if (editora == null)
                    editora = editoraService.save(livro.getEditora());//cria nova editora
            }
            livro.setEditora(editora);
        }
    }

    private void findOrSaveAssunto(Livro livro){
        if (livro.getAssunto() != null){
            Assunto assunto;
            if (livro.getAssunto().getId() != null){
                assunto = assuntoService.findById(livro.getAssunto().getId());
                if (assunto == null){
                    throw new ValidationException("Erro na inclusão do livro",
                            String.format("Nenhuma assunto com o id = %d", livro.getAssunto().getId()));
                }else{
                    if (!isNullOrEmpty(livro.getAssunto().getDescricao()))
                        assunto = assuntoService.save(livro.getAssunto());//atualiza assunto existente
                }
            }else{
                assunto = assuntoService.findByDescricao(livro.getAssunto().getDescricao());
                if (assunto == null)
                    assunto = assuntoService.save(livro.getAssunto());//cria novo assunto
            }
            livro.setAssunto(assunto);
        }
    }

    private Autor findOrSaveAutor(Autor a){
        if (a != null){
            Autor autor;
            if (a.getId() != null){
                autor = autorService.findById(a.getId());
                if (autor == null){
                    throw new ValidationException("Erro na inclusão do livro",
                            String.format("Nenhuma autor com o id = %d", a.getId()));
                }else{
                    if (!isNullOrEmpty(a.getNome()))
                        autor = autorService.save(a);//atualiza autor existente
                }
            }else{
                autor = autorService.findByNomes(a.getNome(), a.getSobrenome());
                if (autor == null)
                    autor = autorService.save(a);//cria novo autor
            }
            return autor;
        }
        return null;
    }
    
    private void findOrSaveAutores(Livro livro){
        List<Autor> autores = new LinkedList<>();
        livro.getAutores().forEach(a -> {
            Autor autor = findOrSaveAutor(a);
            if (autor != null)
                autores.add(autor);
        });
        livro.setAutores(autores);
    }
    
    private Categoria findOrSaveCategoria(Categoria c){
        if (c != null){
            Categoria categoria;
            if (c.getId() != null){
                categoria = categoriaService.findById(c.getId());
                if (categoria == null){
                    throw new ValidationException("Erro na inclusão do livro",
                            String.format("Nenhuma categoria com o id = %d", c.getId()));
                }else{
                    if (!isNullOrEmpty(c.getDescricao()))
                        categoria = categoriaService.save(c);//atualiza categoria existente
                }
            }else{
                categoria = categoriaService.findByDescricao(c.getDescricao());
                if (categoria == null)
                    categoria = categoriaService.save(c);//cria novo categoria
            }
            return categoria;
        }
        return null;
    }

    private void findOrSaveCategorias(Livro livro){
        List<Categoria> categorias = new LinkedList<>();
        livro.getCategorias().forEach(c -> {
            Categoria categoria = findOrSaveCategoria(c);
            if (categoria != null)
                categorias.add(categoria);
        });
        livro.setCategorias(categorias);
    }

    @Transactional
    public Livro save(Livro livro){
        validate(livro);
        findOrSaveEditora(livro);
        findOrSaveAssunto(livro);
        findOrSaveCategorias(livro);
        findOrSaveAutores(livro);
        return repository.save(livro);
    }
}
