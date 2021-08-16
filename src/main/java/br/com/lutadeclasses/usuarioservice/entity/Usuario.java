package br.com.lutadeclasses.usuarioservice.entity;

import java.util.function.Consumer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usuario", schema = "luta-de-classe-db")
public class Usuario {
    
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "username", nullable = false)
    private String username;
    
    @Column(name = "nome", nullable = false)
    private String nome;
    
    @Column(name = "sobrenome", nullable = false)
    private String sobrenome;
    
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "senha", nullable = false)
    private String senha;

    public Usuario() {
    }
    
    public Usuario(Consumer<Usuario> usuario) {
        usuario.accept(this);
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return this.sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return this.senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Usuario id(Integer id) {
        setId(id);
        return this;
    }

    public Usuario username(String username) {
        setUsername(username);
        return this;
    }

    public Usuario nome(String nome) {
        setNome(nome);
        return this;
    }

    public Usuario sobrenome(String sobrenome) {
        setSobrenome(sobrenome);
        return this;
    }

    public Usuario email(String email) {
        setEmail(email);
        return this;
    }

    public Usuario senha(String senha) {
        setSenha(senha);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", username='" + getUsername() + "'" +
            ", nome='" + getNome() + "'" +
            ", sobrenome='" + getSobrenome() + "'" +
            ", email='" + getEmail() + "'" +
            ", senha='" + getSenha() + "'" +
            "}";
    }
}
