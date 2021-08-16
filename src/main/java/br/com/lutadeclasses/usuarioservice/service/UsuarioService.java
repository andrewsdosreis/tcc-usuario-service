package br.com.lutadeclasses.usuarioservice.service;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.lutadeclasses.usuarioservice.entity.Usuario;
import br.com.lutadeclasses.usuarioservice.exception.LoginEmailOuSenhaIncorretoException;
import br.com.lutadeclasses.usuarioservice.exception.LoginUsuarioNaoEncontradoComEmailException;
import br.com.lutadeclasses.usuarioservice.exception.UsuarioNaoEncontradoException;
import br.com.lutadeclasses.usuarioservice.model.RequestNovoUsuarioDto;
import br.com.lutadeclasses.usuarioservice.model.ResponseUsuarioDto;
import br.com.lutadeclasses.usuarioservice.model.ResponseUsuarioLogadoDto;
import br.com.lutadeclasses.usuarioservice.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private PasswordEncoder encoder;
    private ObjectMapper mapper;
    private UsuarioRepository repository;

    public UsuarioService(PasswordEncoder encoder, ObjectMapper mapper, UsuarioRepository repository) {
        this.encoder = encoder;
        this.mapper = mapper;
        this.repository = repository;
    }

    public ResponseUsuarioDto buscarUsuarioPorId(Integer id) throws UsuarioNaoEncontradoException {
        return repository.findById(id)
                         .map(usuario -> mapper.convertValue(usuario, ResponseUsuarioDto.class))
                         .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }

    public List<ResponseUsuarioDto> listarUsuarios() {
        return repository.findAll()
                         .stream()
                         .map(usuario -> mapper.convertValue(usuario, ResponseUsuarioDto.class))
                         .collect(Collectors.toList());
    }

    public ResponseUsuarioDto criarUsuario(RequestNovoUsuarioDto novoUsuarioDto) {
        var usuario = mapper.convertValue(novoUsuarioDto, Usuario.class);
        usuario.setSenha(encoder.encode(novoUsuarioDto.getSenha()));
        repository.save(usuario);
        return mapper.convertValue(usuario, ResponseUsuarioDto.class);
    }

    public ResponseUsuarioDto atualizarUsuario(Integer id, JsonPatch patch) throws JsonProcessingException, JsonPatchException {
        var usuario = repository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        var usuarioAtualizado = aplicarPatchParaUsuario(patch, usuario);
        repository.save(usuarioAtualizado);        
        return mapper.convertValue(usuarioAtualizado, ResponseUsuarioDto.class);
    }

    public void deletarUsuario(Integer id) {
        var usuario = repository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        repository.delete(usuario);
    }

    public ResponseUsuarioLogadoDto login(String email, String password) {
        var usuario = repository.findFirstByEmail(email);
        if (usuario.isEmpty()) {
            throw new LoginUsuarioNaoEncontradoComEmailException(email);
        }

        if (!encoder.matches(password, usuario.get().getSenha())) {
            throw new LoginEmailOuSenhaIncorretoException();
        }

        return ResponseUsuarioLogadoDto.builder()
                                       .email(usuario.get().getEmail())
                                       .username(usuario.get().getUsername())
                                       .token("")
                                       .build();
    }

    private Usuario aplicarPatchParaUsuario(JsonPatch patch, Usuario usuario) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(mapper.convertValue(usuario, JsonNode.class));
        return mapper.treeToValue(patched, Usuario.class);
    }

}
