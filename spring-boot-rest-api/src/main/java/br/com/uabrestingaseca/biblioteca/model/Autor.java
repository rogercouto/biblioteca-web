package br.com.uabrestingaseca.biblioteca.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "autor")
public class Autor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nome(s) do(s) autor(es) deve(m) ser informado(s)")
    private String nome;

    @NotBlank(message = "Sobrenome(s) do(s) autor(es) deve(m) ser informado(s)")
    private String sobrenome;

    private String info;

    @Deprecated
    public Autor(Integer id) {
        this.id = id;
    }

    @Deprecated
    public Autor(String nome, String sobrenome) {
        this.nome = nome;
        this.sobrenome = sobrenome;
    }

    public boolean onlyIdSet(){
        return id != null && nome == null && sobrenome == null && info == null;
    }
}


