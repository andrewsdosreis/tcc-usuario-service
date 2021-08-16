package br.com.lutadeclasses.usuarioservice.exception;

public class LoginEmailOuSenhaIncorretoException extends LoginException {

    private static final long serialVersionUID = 1L;

    public LoginEmailOuSenhaIncorretoException() {
        super("Email ou senha incorretos");
    }
    
}