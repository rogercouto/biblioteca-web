package br.com.uabrestingaseca.biblioteca.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "editora")
public class Editora implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nome da editora deve ser informado")
    private String nome;

    @Deprecated
    public Editora(Integer id) {
        this.id = id;
    }

    @Deprecated
    public Editora(String nome) {
        this.nome = nome;
    }

    public boolean onlyIdSet(){
        return (id != null && (nome == null || nome.isBlank()));
    }
}
