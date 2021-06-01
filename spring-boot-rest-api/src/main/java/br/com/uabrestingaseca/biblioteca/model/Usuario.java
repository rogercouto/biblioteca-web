package br.com.uabrestingaseca.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name="usuario")
public class Usuario implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty( access = JsonProperty.Access.READ_ONLY)
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

    private Boolean ativo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="permissao_usuario",
            joinColumns = { @JoinColumn( name= "usuario_id") },
            inverseJoinColumns = { @JoinColumn( name="permissao_id") }
    )

    @JsonIgnore
    public List<Permissao> permissoes;


    @Transient
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Boolean gerente = false;

    @Transient
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Boolean admin = false;

    public Usuario() {
    }

    public Usuario(Integer id) {
        this.id = id;
    }

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

    public Boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<Permissao> getPermissoes() {
        if (permissoes == null)
            permissoes = new LinkedList<>();
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

    @JsonIgnore
    public List<String> getRoles(){
        if (permissoes == null){
            return new LinkedList<String>();
        }
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

    public Boolean isGerente() {
        return gerente;
    }

    public void setGerente(Boolean gerente) {
        this.gerente = gerente;
    }

    public Boolean isAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return gerente == usuario.gerente &&
                Objects.equals(id, usuario.id) &&
                Objects.equals(nome, usuario.nome) &&
                Objects.equals(email, usuario.email) &&
                Objects.equals(senha, usuario.senha) &&
                Objects.equals(numTel, usuario.numTel) &&
                Objects.equals(ativo, usuario.ativo) &&
                Objects.equals(permissoes, usuario.permissoes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, email, senha, ativo, permissoes);
    }

    public boolean onlyIdSet(){
        return id != null
                && nome == null
                && email == null
                && senha == null
                && numTel == null;
    }

    public static Usuario getUsuarioLogado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null && authentication.getPrincipal() instanceof Usuario){
            return (Usuario)authentication.getPrincipal();
        }
        return null;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        int count = 0;
        if (id != null){
            builder.append("id: ");
            builder.append(id);
            count++;
        }
        if (nome != null){
            if (count > 0)
                builder.append(", ");
            builder.append("nome: ");
            builder.append(nome);
            count++;
        }
        if (email != null){
            if (count > 0)
                builder.append(", ");
            builder.append("email: ");
            builder.append(email);
            count++;
        }
        if (senha != null){
            if (count > 0)
                builder.append(", ");
            builder.append("senha: ");
            builder.append(senha);
            count++;
        }
        builder.append("}");
        return builder.toString();
    }

}
