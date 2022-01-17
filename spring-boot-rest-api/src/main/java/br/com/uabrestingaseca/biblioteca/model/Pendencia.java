package br.com.uabrestingaseca.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pendencia")
public class Pendencia {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @NotNull(message = "Usuário deve ser informado")
    private Usuario usuario;

    @NotNull(message = "Valor deve ser informado")
    private BigDecimal valor;

    private String descricao;

    @ManyToOne
    private Emprestimo emprestimo;

    @Column(name = "data_hora_lancamento")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraLancamento;

    @Column(name = "data_hora_pagamento")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraPagamento;

}
