package br.com.lutadeclasses.usuarioservice.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestNovoUsuarioDto {
    
    @NotBlank(message = "O campo username não pode ser vazio")
    private String username;
    
    @NotBlank(message = "O campo nome não pode ser vazio")
    private String nome;
    
    @NotBlank(message = "O campo sobrenome não pode ser vazio")
    private String sobrenome;
    
    @NotBlank(message = "O campo email não pode ser vazio")
    @Email(message = "Email inválido")
    private String email;
    
    @NotBlank(message = "O campo senha não pode ser vazio")
    @Size(min = 5, max = 30, message = "Senha deve conter no mínimo 5 e no máximo 30 caracteres")
    private String senha;

}
