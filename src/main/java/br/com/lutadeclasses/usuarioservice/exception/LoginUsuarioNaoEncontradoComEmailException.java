package br.com.lutadeclasses.usuarioservice.exception;

public class LoginUsuarioNaoEncontradoComEmailException extends LoginException {

    private static final long serialVersionUID = 1L;

    public LoginUsuarioNaoEncontradoComEmailException(String email) {
        super(String.format("Usuario com o email '%s' nao foi encontrado", email));
    }
    
}
