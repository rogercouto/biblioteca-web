package br.com.uabrestingaseca.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

}
