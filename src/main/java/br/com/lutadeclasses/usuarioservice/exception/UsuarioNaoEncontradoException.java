package br.com.lutadeclasses.usuarioservice.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public UsuarioNaoEncontradoException(Integer id) {
        super(String.format("Usuario com o Id '%s' nao foi encontrado", id));
    }
}
