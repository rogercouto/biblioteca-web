package br.com.uabrestingaseca.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    @NotNull(message = "Livro do exemplar deve ser selecionado")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Livro livro;

    @ManyToOne
    @NotNull(message = "Seção do exemplar deve ser informda")
    private Secao secao;

    @Column(name = "data_aquisicao")
    @NotNull(message = "Data de aquisição do exemplar deve ser informda")
    private LocalDate dataAquisicao;

    @ManyToOne
    @NotNull(message = "Origem do exemplar deve ser informada")
    private Origem origem;

    private boolean fixo = false;

    private boolean disponivel = true;

    private boolean reservado = false;

    private boolean emprestado = false;

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

    public boolean getFixo() {
        return fixo;
    }

    public void setFixo(boolean fixo) {
        this.fixo = fixo;
    }

    public boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public boolean getReservado() {
        return reservado;
    }

    public void setReservado(boolean reservado) {
        this.reservado = reservado;
    }

    public boolean getEmprestado() {
        return emprestado;
    }

    public void setEmprestado(boolean emprestado) {
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

    public boolean onlyNumRegistro(){
        return numRegistro != null
                && livro == null
                &&  secao == null
                &&  dataAquisicao == null
                &&  origem == null
                &&  !fixo
                &&  disponivel
                &&  !reservado
                &&  !emprestado;
    }

    public boolean onlyLivroAndNumRegistro(){
        return numRegistro != null
                && livro != null
                &&  secao == null
                &&  dataAquisicao == null
                &&  origem == null
                &&  !fixo
                &&  disponivel
                &&  !reservado
                &&  !emprestado;
    }

    public Exemplar copy(){
        Exemplar e = new Exemplar();
        e.numRegistro = numRegistro;
        e.livro = livro;
        e.secao = secao;
        e.dataAquisicao = dataAquisicao;
        e.origem = origem;
        e.fixo = fixo;
        e.disponivel = disponivel;
        e.reservado = reservado;
        e.emprestado = emprestado;
        return e;
    }

}
