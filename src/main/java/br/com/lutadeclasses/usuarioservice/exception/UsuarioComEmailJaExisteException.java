package br.com.lutadeclasses.usuarioservice.exception;

public class UsuarioComEmailJaExisteException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public UsuarioComEmailJaExisteException(String email) {
        super(String.format("Usuario com o email '%s' já existe", email));
    }

}
