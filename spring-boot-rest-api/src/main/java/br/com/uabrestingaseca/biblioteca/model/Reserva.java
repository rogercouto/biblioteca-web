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
@Table(name = "reserva")
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "num_registro", referencedColumnName = "num_registro")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Exemplar exemplar;

    @Transient
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer exemplarNumRegistro = null;

    @ManyToOne
    @NotNull(message = "Usuário deve ser informado")
    private Usuario usuario;

    @Column(name = "data_hora")
    private LocalDate dataHora;

    @Column(name = "data_limite")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate dataLimite;

    @Column(name = "data_hora_retirada")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraRetirada;

    private boolean ativa = true;

    public Reserva() {
    }

    public Reserva(Integer id) {
        this.id = id;
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

    public LocalDate getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDate dataHora) {
        this.dataHora = dataHora;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public LocalDateTime getDataHoraRetirada() {
        return dataHoraRetirada;
    }

    public void setDataHoraRetirada(LocalDateTime dataHoraRetirada) {
        this.dataHoraRetirada = dataHoraRetirada;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return ativa == reserva.ativa &&
                Objects.equals(id, reserva.id) &&
                Objects.equals(exemplar, reserva.exemplar) &&
                Objects.equals(exemplarNumRegistro, reserva.exemplarNumRegistro) &&
                Objects.equals(usuario, reserva.usuario) &&
                Objects.equals(dataHora, reserva.dataHora) &&
                Objects.equals(dataLimite, reserva.dataLimite) &&
                Objects.equals(dataHoraRetirada, reserva.dataHoraRetirada);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, exemplar, exemplarNumRegistro, usuario, dataHora, dataLimite, dataHoraRetirada, ativa);
    }
}
