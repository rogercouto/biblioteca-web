package br.com.uabrestingaseca.biblioteca.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "editora")
public class Editora implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nome da editora deve ser informado")
    private String nome;

    public Editora() {
    }

    public Editora(Integer id) {
        this.id = id;
    }

    public Editora(String nome) {
        this.nome = nome;
    }

    public Editora(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Editora editora = (Editora) o;
        return Objects.equals(id, editora.id) &&
                Objects.equals(nome, editora.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    public boolean onlyIdSet(){
        return (id != null && (nome == null || nome.isBlank()));
    }
}
