package br.com.uabrestingaseca.biblioteca.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "exemplar")
public class Exemplar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "num_registro")
    private Integer numRegistro;

    @ManyToOne
    @NotNull(message = "Livro deve ser selecionado")
    private Livro livro;

    @ManyToOne
    @NotNull(message = "Seção deve ser informda")
    private Secao secao;

    @Column(name = "data_aquisicao")
    @NotNull(message = "Data de aquisição deve ser informda")
    private LocalDate dataAquisicao;

    @ManyToOne
    @NotNull(message = "Origem deve ser informada")
    private Origem origem;

    private Boolean fixo;

    private Boolean disponivel;

    private Boolean reservado;

    private Boolean emprestado;

    public Exemplar() {
    }

    public Integer getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(Integer numRegistro) {
        this.numRegistro = numRegistro;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Secao getSecao() {
        return secao;
    }

    public void setSecao(Secao secao) {
        this.secao = secao;
    }

    public LocalDate getDataAquisicao() {
        return dataAquisicao;
    }

    public void setDataAquisicao(LocalDate dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }

    public Origem getOrigem() {
        return origem;
    }

    public void setOrigem(Origem origem) {
        this.origem = origem;
    }

    public Boolean getFixo() {
        return fixo;
    }

    public void setFixo(Boolean fixo) {
        this.fixo = fixo;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Boolean getReservado() {
        return reservado;
    }

    public void setReservado(Boolean reservado) {
        this.reservado = reservado;
    }

    public Boolean getEmprestado() {
        return emprestado;
    }

    public void setEmprestado(Boolean emprestado) {
        this.emprestado = emprestado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exemplar exemplar = (Exemplar) o;
        return Objects.equals(numRegistro, exemplar.numRegistro) &&
                Objects.equals(livro, exemplar.livro) &&
                Objects.equals(secao, exemplar.secao) &&
                Objects.equals(dataAquisicao, exemplar.dataAquisicao) &&
                Objects.equals(origem, exemplar.origem) &&
                Objects.equals(fixo, exemplar.fixo) &&
                Objects.equals(disponivel, exemplar.disponivel) &&
                Objects.equals(reservado, exemplar.reservado) &&
                Objects.equals(emprestado, exemplar.emprestado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numRegistro, livro, secao, dataAquisicao, origem, fixo, disponivel, reservado, emprestado);
    }
}
