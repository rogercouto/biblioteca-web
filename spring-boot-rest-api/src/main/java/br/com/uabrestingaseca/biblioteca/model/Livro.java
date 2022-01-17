package br.com.uabrestingaseca.biblioteca.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @OneToMany
    @JoinColumn(name = "livro_id", insertable = false, updatable = false)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Exemplar> exemplares;

    private String nomesAutores;

    public String getNomesAutores(){
        if (nomesAutores == null && autores != null){
            StringBuilder builder = new StringBuilder();
            autores.forEach(a->{
                if (builder.length() > 0){
                    builder.append("; ");
                }
                builder.append(a.getSobrenome().toUpperCase());
                builder.append(", ");
                builder.append(a.getNome());
            });
            nomesAutores = builder.toString();
        }
        return nomesAutores;
    }

    @Deprecated
    public Livro(Integer id){
        this.id = id;
    }

    public List<Autor> getAutores() {
        if (autores == null)
            autores = new LinkedList<>();
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
        getNomesAutores();
    }

    public List<Categoria> getCategorias() {
        if (categorias == null)
            categorias = new LinkedList<>();
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<Exemplar> getExemplares() {
        if (exemplares == null)
            exemplares = new LinkedList<>();
        return exemplares;
    }

    public void setExemplares(List<Exemplar> exemplares) {
        this.exemplares = exemplares;
    }

}
