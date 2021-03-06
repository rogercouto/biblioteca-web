package br.com.uabrestingaseca.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "num_registro", referencedColumnName = "num_registro")
    private Exemplar exemplar;

    @Transient
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer exemplarNumRegistro = null;

    @ManyToOne
    @NotNull(message = "Usuário deve ser informado")
    private Usuario usuario;

    @Transient
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer usuarioId = null;

    @Column(name = "renovacoes")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private int numRenovacoes;

    @Column(name = "data_hora_devolucao")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraDevolucao;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate prazo;

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

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setPrazo(LocalDate prazo){
        this.prazo = prazo;
    }

    public LocalDate getPrazo(){
        return prazo;
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
                Objects.equals(prazo, that.prazo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataHora, exemplar, exemplarNumRegistro, usuario, numRenovacoes, dataHoraDevolucao, prazo);
    }
}
