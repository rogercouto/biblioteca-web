package br.com.uabrestingaseca.biblioteca.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "parametro")
public class Parametro {

    @Id
    @NotBlank(message = "Chave dever ser informada")
    private String chave;

    @Column(nullable = false)
    @NotBlank(message = "Valor dever ser informado")
    private String valor;

    private Boolean editavel;

    public Parametro() {
    }

    public Parametro(String chave, String valor, Boolean editavel) {
        this.chave = chave;
        this.valor = valor;
        this.editavel = editavel;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Boolean getEditavel() {
        return editavel;
    }

    public void setEditavel(Boolean editavel) {
        this.editavel = editavel;
    }
}
