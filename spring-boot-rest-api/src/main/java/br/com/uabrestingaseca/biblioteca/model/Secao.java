package br.com.uabrestingaseca.biblioteca.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "secao")
public class Secao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Descrição da seção deve ser informada")
    private String nome;

    public Secao() {
    }

    public Secao(Integer id) {
        this.id = id;
    }

    public Secao(String nome) {
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
        Secao secao = (Secao) o;
        return Objects.equals(id, secao.id) &&
                Objects.equals(nome, secao.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

}
