package br.com.lutadeclasses.usuarioservice.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

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
    private String email;
    
    @NotBlank(message = "O campo senha não pode ser vazio")
    private String senha;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}