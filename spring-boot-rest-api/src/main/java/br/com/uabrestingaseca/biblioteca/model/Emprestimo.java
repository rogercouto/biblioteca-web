package br.com.uabrestingaseca.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "emprestimo")
public class Emprestimo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "num_registro", referencedColumnName = "num_registro")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Exemplar exemplar;

    @Transient
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer exemplarNumRegistro = null;

    @ManyToOne
    @NotNull(message = "Usuário deve ser informado")
    private Usuario usuario;

    @Column(name = "renovacoes")
    private int numRenovacoes;

    @Column(name = "data_hora_devolucao")
    private LocalDateTime dataHoraDevolucao;

    private BigDecimal multa;

    public Emprestimo() {
    }

    public Emprestimo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public void setExemplar(Exemplar exemplar) {
        this.exemplar = exemplar;
    }

    public Integer getExemplarNumRegistro() {
        return exemplarNumRegistro;
    }

    public void setExemplarNumRegistro(Integer exemplarNumRegistro) {
        this.exemplarNumRegistro = exemplarNumRegistro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getNumRenovacoes() {
        return numRenovacoes;
    }

    public void setNumRenovacoes(int numRenovacoes) {
        this.numRenovacoes = numRenovacoes;
    }

    public LocalDateTime getDataHoraDevolucao() {
        return dataHoraDevolucao;
    }

    public void setDataHoraDevolucao(LocalDateTime dataHoraDevolucao) {
        this.dataHoraDevolucao = dataHoraDevolucao;
    }

    public BigDecimal getMulta() {
        return multa;
    }

    public void setMulta(BigDecimal multa) {
        this.multa = multa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Emprestimo that = (Emprestimo) o;
        return numRenovacoes == that.numRenovacoes &&
                Objects.equals(id, that.id) &&
                Objects.equals(dataHora, that.dataHora) &&
                Objects.equals(exemplar, that.exemplar) &&
                Objects.equals(exemplarNumRegistro, that.exemplarNumRegistro) &&
                Objects.equals(usuario, that.usuario) &&
                Objects.equals(dataHoraDevolucao, that.dataHoraDevolucao) &&
                Objects.equals(multa, that.multa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataHora, exemplar, exemplarNumRegistro, usuario, numRenovacoes, dataHoraDevolucao, multa);
    }
}
