package br.com.uabrestingaseca.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "exemplar")
public class Exemplar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "num_registro")
    private Integer numRegistro;

    @ManyToOne
    @NotNull(message = "Livro do(s) exemplar(es) deve(m) ser selecionado(s)")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Livro livro;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer livroId;

    @ManyToOne
    @NotNull(message = "Seção do(s) exemplar(es) deve(m) ser selecionado(s)")
    private Secao secao;

    @Column(name = "data_aquisicao")
    @NotNull(message = "Data de aquisição do(s) exemplar(es) deve(m) ser selecionado(s)")
    private LocalDate dataAquisicao;

    @ManyToOne
    @NotNull(message = "Origem do exemplar do(s) exemplar(es) deve(m) ser selecionado(s)")
    private Origem origem;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean fixo = false;

    @JsonIgnore
    private boolean disponivel = true;

    @JsonIgnore
    private boolean reservado = false;

    @JsonIgnore
    private boolean emprestado = false;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Transient
    private String situacao;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private Baixa baixa;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @Transient
    private String tituloLivro;

    @Deprecated
    public Exemplar(Integer numRegistro) {
        this.numRegistro = numRegistro;
    }

    @Deprecated
    public Exemplar(Integer numRegistro, Secao secao,LocalDate dataAquisicao, Origem origem) {
        this.numRegistro = numRegistro;
        this.secao = secao;
        this.dataAquisicao = dataAquisicao;
        this.origem = origem;
    }

    public String getTituloLivro() {
        if (tituloLivro == null){
            tituloLivro = getLivro().getTitulo();
        }
        return tituloLivro;
    }

    public void setTituloLivro(String tituloLivro) {
        this.tituloLivro = tituloLivro;
    }

    public String getSituacao() {
        if (!disponivel){
            setSituacao("Indisponível");
        }else if (fixo){
            setSituacao("Fixo");
        }else if (!emprestado && !reservado){
            setSituacao("Disponível");
        }else if (emprestado && !reservado) {
            setSituacao("Emprestado");
        }else if (emprestado && reservado){
            setSituacao("Emprestado e Reservado");
        }else{
            setSituacao("Reservado");
        }
        return situacao;
    }

    private void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public boolean onlyNumRegistroSet(){
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

    public boolean onlyLivroAndNumRegistroSet(){
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
