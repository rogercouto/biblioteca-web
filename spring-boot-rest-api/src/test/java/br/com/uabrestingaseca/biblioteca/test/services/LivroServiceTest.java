package br.com.uabrestingaseca.biblioteca.test.services;

import br.com.uabrestingaseca.biblioteca.BibliotecaApplicationTests;
import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.*;
import br.com.uabrestingaseca.biblioteca.services.*;
import br.com.uabrestingaseca.biblioteca.util.ModelUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LivroServiceTest extends BibliotecaApplicationTests {

    @Autowired
    private LivroService livroService;

    @Autowired
    private AssuntoService assuntoService;

    @Autowired
    private SecaoService secaoService;

    @Autowired
    private OrigemService origemService;

    @Autowired
    private EditoraService editoraService;

    @Autowired
    private AutorService autorService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ExemplarService exemplarService;

    @Autowired
    private BaixaService baixaService;

    @Before
    public void setUp(){
    }

    public Map<String, Integer> saveAux() {
        Map<String, Integer> map = new HashMap<>();
        //Assunto
        Assunto assunto = new Assunto("Assunto 1");
        assunto = assuntoService.save(assunto);
        map.put("assuntoId", assunto.getId());
        Assert.assertNotNull("assuntoService.save() deveria retornar um assunto não nulo", assunto);
        Assert.assertNotNull("assuntoService.save() deveria gerar um id pra o assunto", assunto.getId());
        //Secao
        Secao secao = new Secao("Seção 1");
        secao = secaoService.save(secao);
        map.put("secaoId", secao.getId());
        Assert.assertNotNull("secaoService.save() deveria retornar uma seção não nula", assunto);
        Assert.assertNotNull("secaoService.save() deveria gerar um id pra a seção", secao.getId());
        //Origem
        Origem origem = new Origem("Origem 1");
        origem = origemService.save(origem);
        map.put("origemId", origem.getId());
        Assert.assertNotNull("origemService.save() deveria retornar uma origem não nula", origem);
        Assert.assertNotNull("origemService.save() deveria gerar um id pra a origem", origem.getId());
        return map;
    }

    public void findAux(Map<String, Integer> map) {
        //assunto
        Assunto assunto = assuntoService.findById(map.get("assuntoId"));
        Assert.assertNotNull("assuntoSerice.finById(1) não deveria retornar null", assunto);
        //secao
        Secao secao = secaoService.findById(map.get("secaoId"));
        Assert.assertNotNull("secaoSerice.finById(1) não deveria retornar null", secao);
        //origem
        Origem origem = origemService.findById(map.get("origemId"));
        Assert.assertNotNull("origemSerice.finById(1) não deveria retornar null", origem);

    }

    @Test
    public void createLivroWithExemplares(){
        Map<String, Integer> map = saveAux();
        findAux(map);
        Livro livro = new Livro();
        livro.setTitulo("Livro teste 1");
        livro.setResumo("Resumo teste do livro 1");
        livro.setIsbn((long) 123456);
        livro.setCutter("Cutter Teste");
        livro.setEditora(new Editora("Editora Teste 1"));
        livro.setEdicao("1a edição");
        livro.setVolume("único");
        livro.setNumPaginas(300);
        livro.setAssunto(new Assunto(map.get("assuntoId")));
        livro.setAnoPublicacao((short) 2009);
        livro.getAutores().add(new Autor("Fulano","de Tal"));
        livro.getAutores().add(new Autor("Ciclano","de Tal"));
        livro.getCategorias().add(new Categoria("Categoria Teste 1"));
        livro.getCategorias().add(new Categoria("Categoria Teste 2"));
        livro.getExemplares().add(new Exemplar(1001, new Secao(map.get("secaoId")), LocalDate.now(), new Origem(map.get("origemId"))));
        livro.getExemplares().add(new Exemplar(1002, new Secao(map.get("secaoId")), LocalDate.now(), new Origem(map.get("origemId"))));
        livro.getExemplares().add(new Exemplar(1003, new Secao(map.get("secaoId")), LocalDate.now(), new Origem(map.get("origemId"))));
        try{
            livro = livroService.save(livro);
            ModelUtil.printJson(livro);
        }catch (ModelValidationException e){
            livro = null;
            System.err.println(e.getMessage());
            e.getErrors().forEach(System.out::println);
        }
        Assert.assertNotNull("livroService.save() não deveria retornar null", livro);
    }

    @Test
    public void createLivroWithErrors(){
        Livro livro = new Livro();
        livro.setEditora(new Editora());
        livro.setAssunto(new Assunto());
        livro.getCategorias().add(new Categoria());
        livro.getAutores().add(new Autor());
        livro.getExemplares().add(new Exemplar());
        List<String> errors = null;
        try{
            ModelUtil.printJson(livro);
            livroService.save(livro);
        }catch (ModelValidationException e){
            errors = e.getErrors();
            //errors.forEach(System.err::println);
        }
        Assert.assertNotNull("Deve ser lançado uma exeção e a variável errors não pode ser nula", errors);
        //Expected fields with error:
        //titulo, editora.nome, assunto.id, autor.nome, autor.sobrenome, categoria.descricao
        //exemplar.secao, exempalr.dataAquisicao, exemplar.origem
        Assert.assertEquals("Deve ser lançado uma exeção e a variável errors deve ter 9 campos com erro",
                9, errors.size());
    }

    private Map<String, Integer> saveAllAux() {
        Map<String, Integer> map = new HashMap<>();
        //Assunto
        Assunto assunto = assuntoService.save(new Assunto("Assunto 1"));
        map.put("assuntoId", assunto.getId());
        //Secao
        Secao secao = secaoService.save(new Secao("Seção 1"));
        map.put("secaoId", secao.getId());
        //Origem
        Origem origem = origemService.save(new Origem("Origem 1"));
        map.put("origemId", origem.getId());
        //Editora
        Editora editora = editoraService.save(new Editora("Editora 1"));
        map.put("editoraId", origem.getId());
        Assert.assertNotNull("editoraService.save() deveria retornar uma editora não nula", editora);
        Assert.assertNotNull("editoraService.save() deveria gerar um id pra a editora", editora.getId());
        //Autores
        List<Autor> autores = new LinkedList<>();
        autores.add(autorService.save(new Autor("Fulano", "de Tal")));
        Assert.assertNotNull("autorSerivce.save() deveria retornar um autor não nulo", autores.get(0));
        Assert.assertNotNull("autorSerivce.save() deveria gerar um id pra o autor", autores.get(0).getId());
        autores.add(autorService.save(new Autor("Ciclano", "de Tal")));
        map.put("autorId0", autores.get(0).getId());
        map.put("autorId1", autores.get(1).getId());
        //Categorias
        List<Categoria> categorias = new LinkedList<>();
        categorias.add(categoriaService.save(new Categoria("Categoria 1")));
        Assert.assertNotNull("categoriaService.save() deveria retornar uma categoria não nula", categorias.get(0));
        Assert.assertNotNull("categoriaService.save() deveria gerar um id pra a categoria", categorias.get(0).getId());
        categorias.add(categoriaService.save(new Categoria("Categoria 2")));
        map.put("categoriaId0", categorias.get(0).getId());
        map.put("categoriaId1", categorias.get(1).getId());
        return map;
    }

    @Test
    public void createLivroLaterExemplar(){
        Map<String, Integer> map = saveAllAux();
        Livro livro = new Livro();
        livro.setTitulo("Livro teste 2");
        livro.setResumo("Resumo teste do livro 2");
        livro.setIsbn((long) 12345678);
        livro.setCutter("Cutter Teste 2");
        livro.setEditora(new Editora(map.get("editoraId")));
        livro.setEdicao("1a edição");
        livro.setVolume("único");
        livro.setNumPaginas(300);
        livro.setAssunto(new Assunto(map.get("assuntoId")));
        livro.setAnoPublicacao((short) 2012);
        livro.getAutores().add(new Autor(map.get("autorId0")));
        livro.getAutores().add(new Autor(map.get("autorId1")));
        livro.getCategorias().add(new Categoria(map.get("categoriaId0")));
        livro.getCategorias().add(new Categoria("categoriaId1"));
        Livro l = null;
        try{
            l = livroService.save(livro);
            ModelUtil.printJson(livro);
        }catch (ModelValidationException err){
            System.err.println(err.getMessage());
            err.getErrors().forEach(System.out::println);
        }
        Assert.assertNotNull("livroService.save() não deveria retornar null", l);
        Assert.assertEquals("Número de exemplares do livro antes de adicionar exemplar deve ser 0",
                0, l.getExemplares().size());
        Exemplar exemplar = new Exemplar();
        exemplar.setNumRegistro(20001);
        exemplar.setLivro(l);
        exemplar.setSecao(new Secao(map.get("secaoId")));
        exemplar.setOrigem(new Origem(map.get("origemId")));
        exemplar.setDataAquisicao(LocalDate.of(2020, 1, 1));
        Exemplar e = null;
        try {
            e = exemplarService.save(exemplar);
            ModelUtil.printJson(exemplar);
        }catch (ModelValidationException err){
            System.err.println(err.getMessage());
            err.getErrors().forEach(System.out::println);
        }
        Assert.assertNotNull("exemplarService.save() não deveria retornar null", e);
        Livro lt = livroService.findById(l.getId());
        ModelUtil.printJson(lt);
        Assert.assertEquals("Número de exemplares do livro após adicionar exemplar deve ser 1",
                1, lt.getExemplares().size());
    }

    @Test
    public void updateLivroAndFindAll(){
        Map<String, Integer> map = saveAux();
        findAux(map);
        Livro livro = new Livro();
        livro.setTitulo("Livro teste 1");
        livro.setResumo("Resumo teste do livro 1");
        livro.setIsbn((long) 123456);
        livro.setCutter("Cutter Teste");
        livro.setEditora(new Editora("Editora Teste 1"));
        livro.setEdicao("1a edição");
        livro.setVolume("único");
        livro.setNumPaginas(300);
        livro.setAssunto(new Assunto(map.get("assuntoId")));
        livro.setAnoPublicacao((short) 2009);
        livro.getAutores().add(new Autor("Fulano","de Tal"));
        livro.getAutores().add(new Autor("Ciclano","de Tal"));
        livro.getCategorias().add(new Categoria("Categoria Teste 1"));
        livro.getCategorias().add(new Categoria("Categoria Teste 2"));
        livro.getExemplares().add(new Exemplar(1004, new Secao(map.get("secaoId")), LocalDate.now(), new Origem(map.get("origemId"))));
        livro.getExemplares().add(new Exemplar(1005, new Secao(map.get("secaoId")), LocalDate.now(), new Origem(map.get("origemId"))));
        livro.getExemplares().add(new Exemplar(1006, new Secao(map.get("secaoId")), LocalDate.now(), new Origem(map.get("origemId"))));
        try{
            livro = livroService.save(livro);
        }catch (ModelValidationException err){
            System.err.println(err.getMessage());
            err.getErrors().forEach(System.out::println);
        }
        livro.setTitulo(livro.getTitulo()+" (Editado)");
        Livro livroEditado = livroService.save(livro);
        Assert.assertNotNull("livroService.save() não deveria retornar null em update", livroEditado);
        Pageable pageable = PageRequest.of(0, 10);
        List<Livro> livros = livroService.findPage(pageable).toList();
        Assert.assertTrue("livroService.getPage() deve retornar uma lista não vazia", livros.size() > 0);
    }

    public Map<String, Integer> createLivroWithExemplaresAgain(){
        Map<String, Integer> map = saveAux();
        findAux(map);
        Livro livro = new Livro();
        livro.setTitulo("Livro teste 4");
        livro.setResumo("Resumo teste do livro 1");
        livro.setIsbn((long) 123456);
        livro.setCutter("Cutter Teste");
        livro.setEditora(new Editora("Editora Teste 1"));
        livro.setEdicao("1a edição");
        livro.setVolume("único");
        livro.setNumPaginas(300);
        livro.setAssunto(new Assunto(map.get("assuntoId")));
        livro.setAnoPublicacao((short) 2009);
        livro.getAutores().add(new Autor("Fulano","de Tal"));
        livro.getAutores().add(new Autor("Ciclano","de Tal"));
        livro.getCategorias().add(new Categoria("Categoria Teste 1"));
        livro.getCategorias().add(new Categoria("Categoria Teste 2"));
        livro.getExemplares().add(new Exemplar(1007, new Secao(map.get("secaoId")), LocalDate.now(), new Origem(map.get("origemId"))));
        livro.getExemplares().add(new Exemplar(1008, new Secao(map.get("secaoId")), LocalDate.now(), new Origem(map.get("origemId"))));
        livro.getExemplares().add(new Exemplar(1009, new Secao(map.get("secaoId")), LocalDate.now(), new Origem(map.get("origemId"))));
        try{
            livro = livroService.save(livro);
        }catch (ModelValidationException e){
            livro = null;
            System.err.println(e.getMessage());
            e.getErrors().forEach(System.out::println);
        }
        map.put("livroId", livro.getId());
        map.put("exemplarNumRegistro0", livro.getExemplares().get(0).getNumRegistro());
        map.put("exemplarNumRegistro1", livro.getExemplares().get(1).getNumRegistro());
        map.put("exemplarNumRegistro2", livro.getExemplares().get(2).getNumRegistro());
        return map;
    }

    @Test
    public void baixaTest(){
        Map<String, Integer> map = createLivroWithExemplaresAgain();
        Baixa baixa = new Baixa();
        baixa.setExemplar(new Exemplar(map.get("exemplarNumRegistro0")));
        baixa.setCausa("Pegoou fogo");
        baixa = baixaService.save(baixa);
        ModelUtil.printJson(baixa);
    }


}
