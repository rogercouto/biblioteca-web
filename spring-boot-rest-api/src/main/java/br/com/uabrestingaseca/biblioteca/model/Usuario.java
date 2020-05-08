package br.com.uabrestingaseca.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name="usuario")
public class Usuario implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    @NotBlank(message = "Campo nome é requerido")
    private String nome;

    @NotBlank(message = "Campo e-mail é requerido")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "Campo senha é requerido")
    @JsonProperty( access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @Column(name = "num_tel",nullable = true)
    private String numTel;

    @JsonIgnore
    private Boolean ativo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="permissao_usuario",
            joinColumns = { @JoinColumn( name= "usuario_id") },
            inverseJoinColumns = { @JoinColumn( name="permissao_id") }
    )
    @JsonIgnore
    public List<Permissao> permissoes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<Permissao> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<Permissao> permissoes) {
        this.permissoes = permissoes;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissoes;
    }

    @JsonProperty( access = JsonProperty.Access.READ_ONLY)
    public List<String> getRoles(){
        return permissoes.stream()
                .map(Permissao::getDescricao)
                .collect(Collectors.toList());
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return senha;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) &&
                Objects.equals(nome, usuario.nome) &&
                Objects.equals(email, usuario.email) &&
                Objects.equals(senha, usuario.senha) &&
                Objects.equals(ativo, usuario.ativo) &&
                Objects.equals(permissoes, usuario.permissoes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, email, senha, ativo, permissoes);
    }
}
