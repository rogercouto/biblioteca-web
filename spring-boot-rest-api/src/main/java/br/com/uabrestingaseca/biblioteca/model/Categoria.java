package br.com.uabrestingaseca.biblioteca.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categoria")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Descrição da(s) categoria(s) deve(m) ser informada(s)")
    private String descricao;

    @Deprecated
    public Categoria(Integer id) {
        this.id = id;
    }

    @Deprecated
    public Categoria(String descricao) {
        this.descricao = descricao;
    }

    public boolean onlyIdSet(){
        return id != null && (descricao == null || descricao.isBlank());
    }
}
