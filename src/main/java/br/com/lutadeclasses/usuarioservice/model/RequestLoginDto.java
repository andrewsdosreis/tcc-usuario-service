package br.com.lutadeclasses.usuarioservice.model;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestLoginDto {
    
    @NotBlank(message = "Email não pode ser vazio")
    private String email;
    
    @NotBlank(message = "Senha não pode ser vazio")
    private String senha;

}
