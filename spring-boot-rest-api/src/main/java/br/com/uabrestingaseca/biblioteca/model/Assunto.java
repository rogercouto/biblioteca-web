package br.com.uabrestingaseca.biblioteca.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "assunto")
public class Assunto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Descrição do assunto deve ser informada")
    private String descricao;
    private String cores;
    private String cdu;

    public Assunto() {
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

    public String getCores() {
        return cores;
    }

    public void setCores(String cores) {
        this.cores = cores;
    }

    public String getCdu() {
        return cdu;
    }

    public void setCdu(String cdu) {
        this.cdu = cdu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assunto assunto = (Assunto) o;
        return Objects.equals(id, assunto.id) &&
                Objects.equals(descricao, assunto.descricao) &&
                Objects.equals(cores, assunto.cores) &&
                Objects.equals(cdu, assunto.cdu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descricao, cores, cdu);
    }
}
