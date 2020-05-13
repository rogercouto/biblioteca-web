package br.com.uabrestingaseca.biblioteca.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "origem")
public class Origem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Descrição da origem deve ser informada")
    private String descricao;

    public Origem() {
    }

    public Origem(Integer id) {
        this.id = id;
    }

    public Origem(@NotBlank(message = "Descrição da origem deve ser informada") String descricao) {
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Origem origem = (Origem) o;
        return Objects.equals(id, origem.id) &&
                Objects.equals(descricao, origem.descricao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descricao);
    }
    
}
