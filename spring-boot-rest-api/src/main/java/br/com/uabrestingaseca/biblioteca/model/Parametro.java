package br.com.uabrestingaseca.biblioteca.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "parametro")
public class Parametro {

    @Id
    @NotBlank(message = "Chave dever ser informada")
    private String chave;

    @Column(nullable = false)
    @NotBlank(message = "Valor dever ser informado")
    private String valor;

    private Boolean editavel;

}
