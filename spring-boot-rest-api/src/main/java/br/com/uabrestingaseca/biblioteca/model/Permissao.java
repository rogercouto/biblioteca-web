package br.com.uabrestingaseca.biblioteca.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="permissao")
public class Permissao implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    public Permissao(int id){
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String descricao;

    @Override
    public String getAuthority() {
        return descricao;
    }

}
