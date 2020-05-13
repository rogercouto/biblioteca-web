package br.com.uabrestingaseca.biblioteca.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

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

    public Autor() {
    }

    public Autor(Integer id) {
        this.id = id;
    }

    public Autor(String nome, String sobrenome) {
        this.nome = nome;
        this.sobrenome = sobrenome;
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

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autor autor = (Autor) o;
        return Objects.equals(id, autor.id) &&
                Objects.equals(nome, autor.nome) &&
                Objects.equals(sobrenome, autor.sobrenome) &&
                Objects.equals(info, autor.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, sobrenome, info);
    }

    public boolean onlyIdSet(){
        return id != null && nome == null && sobrenome == null && info == null;
    }
}
