package br.com.uabrestingaseca.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "baixa")
public class Baixa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "num_registro", referencedColumnName = "num_registro")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Exemplar exemplar;

    @Transient
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer exemplarNumRegistro = null;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @NotBlank(message = "Causa da baixa dever ser informada")
    private String causa;

    public Baixa() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public void setExemplar(Exemplar exemplar) {
        this.exemplar = exemplar;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public Integer getExemplarNumRegistro() {
        return exemplarNumRegistro;
    }

    public void setExemplarNumRegistro(Integer exemplarNumRegistro) {
        this.exemplarNumRegistro = exemplarNumRegistro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Baixa baixa = (Baixa) o;
        return Objects.equals(id, baixa.id) &&
                Objects.equals(exemplar, baixa.exemplar) &&
                Objects.equals(dataHora, baixa.dataHora) &&
                Objects.equals(causa, baixa.causa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, exemplar, dataHora, causa);
    }

}
