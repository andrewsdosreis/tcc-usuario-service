package br.com.lutadeclasses.usuarioservice.exception;

public class UsuarioComUsernameJaExisteException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public UsuarioComUsernameJaExisteException(String username) {
        super(String.format("Usuario com o username '%s' já existe", username));
    }

}
