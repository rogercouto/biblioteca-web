package br.com.uabrestingaseca.biblioteca.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "livro")
public class Livro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Título dever ser informado")
    private String titulo;

    private String resumo;

    private Long isbn;

    private String cutter;

    @ManyToOne
    private Editora editora;

    private String edicao;

    private String volume;

    @Column(name = "num_paginas")
    private Integer numPaginas;

    @ManyToOne
    private Assunto assunto;

    @Min(value = 0, message = "Ano inválido")
    @Max(value = 2100, message = "Ano inválido")
    @Column(name = "ano_publicacao")
    private Short anoPublicacao;

    @ManyToMany
    @JoinTable(name = "autor_livro",
            joinColumns = { @JoinColumn( name = "livro_id") },
            inverseJoinColumns = { @JoinColumn( name = "autor_id") }
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Autor> autores;

    @ManyToMany
    @JoinTable(name = "categoria_livro",
            joinColumns = { @JoinColumn( name = "livro_id") },
            inverseJoinColumns = { @JoinColumn( name = "categoria_id") }
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Categoria> categorias;

    public Livro() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getCutter() {
        return cutter;
    }

    public void setCutter(String cutter) {
        this.cutter = cutter;
    }

    public Editora getEditora() {
        return editora;
    }

    public void setEditora(Editora editora) {
        this.editora = editora;
    }

    public String getEdicao() {
        return edicao;
    }

    public void setEdicao(String edicao) {
        this.edicao = edicao;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Integer getNumPaginas() {
        return numPaginas;
    }

    public void setNumPaginas(Integer numPaginas) {
        this.numPaginas = numPaginas;
    }

    public Assunto getAssunto() {
        return assunto;
    }

    public void setAssunto(Assunto assunto) {
        this.assunto = assunto;
    }

    public Short getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Short anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livro livro = (Livro) o;
        return Objects.equals(id, livro.id) &&
                Objects.equals(titulo, livro.titulo) &&
                Objects.equals(resumo, livro.resumo) &&
                Objects.equals(isbn, livro.isbn) &&
                Objects.equals(cutter, livro.cutter) &&
                Objects.equals(editora, livro.editora) &&
                Objects.equals(edicao, livro.edicao) &&
                Objects.equals(volume, livro.volume) &&
                Objects.equals(numPaginas, livro.numPaginas) &&
                Objects.equals(assunto, livro.assunto) &&
                Objects.equals(anoPublicacao, livro.anoPublicacao) &&
                Objects.equals(autores, livro.autores) &&
                Objects.equals(categorias, livro.categorias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, resumo, isbn, cutter, editora, edicao, volume, numPaginas, assunto, anoPublicacao, autores, categorias);
    }
}
