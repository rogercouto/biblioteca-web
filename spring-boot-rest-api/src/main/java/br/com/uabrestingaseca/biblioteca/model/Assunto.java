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
@Entity
@Table(name = "assunto")
@Builder
public class Assunto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Descrição do assunto deve ser informada")
    private String descricao;

    private String cores;

    private String cdu;

    @Deprecated
    public Assunto(Integer id) {
        this.id = id;
    }

    @Deprecated
    public Assunto(String descricao) {
        this.descricao = descricao;
    }

    public boolean onlyIdSet(){
        return id != null
                && (descricao == null || descricao.isBlank())
                && (cores == null || cores.isBlank())
                && (cdu == null || cdu.isBlank());
    }
}
