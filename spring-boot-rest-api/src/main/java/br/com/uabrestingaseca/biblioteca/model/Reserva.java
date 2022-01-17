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

    @Transient
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer usuarioId = null;

    @Column(name = "data_hora")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHora;

    @Column(name = "data_limite")
    private LocalDate dataLimite;

    @Column(name = "data_hora_retirada")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraRetirada;

    private boolean ativa = true;

    @Deprecated
    public Reserva(Integer id) {
        this.id = id;
    }
}
