package br.com.uabrestingaseca.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Deprecated
    public Emprestimo(Integer id) {
        this.id = id;
    }

}
