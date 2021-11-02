package br.com.lutadeclasses.usuarioservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.lutadeclasses.usuarioservice.entity.Usuario;
import br.com.lutadeclasses.usuarioservice.exception.LoginEmailOuSenhaIncorretoException;
import br.com.lutadeclasses.usuarioservice.exception.LoginUsuarioNaoEncontradoComEmailException;
import br.com.lutadeclasses.usuarioservice.exception.UsuarioComEmailJaExisteException;
import br.com.lutadeclasses.usuarioservice.exception.UsuarioComUsernameJaExisteException;
import br.com.lutadeclasses.usuarioservice.exception.UsuarioNaoEncontradoException;
import br.com.lutadeclasses.usuarioservice.model.RequestNovoUsuarioDto;
import br.com.lutadeclasses.usuarioservice.model.ResponseUsuarioDto;
import br.com.lutadeclasses.usuarioservice.model.ResponseUsuarioLogadoDto;
import br.com.lutadeclasses.usuarioservice.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class UsuarioServiceTest {
    
    private UsuarioService service;
    private PasswordEncoder encoder;
    private ObjectMapper mapper;
    
    @Mock
    private UsuarioRepository repository;

    @BeforeEach
    public void prepararTestes() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        encoder = new BCryptPasswordEncoder();
        service = new UsuarioService(encoder, mapper, repository);
    }

    @Test
    void test_buscarUsuarioPorId_ComSucesso() {
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(criarUsuario_OK()));

        var expected = criarResponseUsuarioDto_OK();
        var actual = service.buscarUsuarioPorId(id);

        assertEquals(expected, actual);
    }

    @Test
    void test_buscarUsuarioPorId_UsuarioNaoEncontrado() {
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(UsuarioNaoEncontradoException.class, () -> {
            service.buscarUsuarioPorId(id);
        });

        assertNotNull(exception);
    }

    @Test
    void test_listarUsuarios_ComSucesso() {
        List<Usuario> listaDeUsuarios = new ArrayList<>();
        listaDeUsuarios.add(criarUsuario_OK());        
        when(repository.findAll()).thenReturn(listaDeUsuarios);

        List<ResponseUsuarioDto> expected = new ArrayList<>();
        expected.add(criarResponseUsuarioDto_OK());
        var actual = service.listarUsuarios();

        assertTrue(actual.size() > 0);
        assertEquals(expected.get(0), actual.get(0));
    }

    @Test
    void test_criarUsuario_ComSucesso() {
        var novoUsuario = criarRequestNovoUsuarioDto_OK();
        var usuario = criarUsuario_OK();

        when(repository.findFirstByEmail(anyString())).thenReturn(Optional.ofNullable(null));
        when(repository.findFirstByUsername(anyString())).thenReturn(Optional.ofNullable(null));
        when(repository.save(any())).thenReturn(usuario);

        var expected = criarResponseUsuarioDto_OK();
        var actual = service.criarUsuario(novoUsuario);

        assertEquals(expected, actual);
    }

    @Test
    void test_criarUsuario_JaExisteUsuarioComEsteEmail() {
        var novoUsuario = criarRequestNovoUsuarioDto_OK();
        var usuario = criarUsuario_OK();
        
        when(repository.findFirstByUsername(anyString())).thenReturn(Optional.ofNullable(null));
        when(repository.findFirstByEmail(anyString())).thenReturn(Optional.ofNullable(usuario));

        Exception exception = assertThrows(UsuarioComEmailJaExisteException.class, () -> {
            service.criarUsuario(novoUsuario);
        });

        assertNotNull(exception);
    }

    @Test
    void test_criarUsuario_JaExisteUsuarioComEsteUsername() {
        var novoUsuario = criarRequestNovoUsuarioDto_OK();
        var usuario = criarUsuario_OK();

        when(repository.findFirstByUsername(anyString())).thenReturn(Optional.ofNullable(usuario));

        Exception exception = assertThrows(UsuarioComUsernameJaExisteException.class, () -> {
            service.criarUsuario(novoUsuario);
        });

        assertNotNull(exception);
    }

    @Test
    void test_atualizarUsuario_ComSucesso() throws JsonProcessingException, JsonPatchException {
        Integer id = 1;
        JsonPatch patch = criarJsonPatchDeAtualizacao_OK();
        
        when(repository.findById(id)).thenReturn(Optional.of(criarUsuario_OK()));
        
        var expected = criarResponseUsuarioDtoAtualizado_OK();
        var actual = service.atualizarUsuario(id, patch);

        assertEquals(expected, actual);
    }

    @Test
    void test_atualizarUsuario_UsuarioNaoEncontrado() throws JsonMappingException, JsonProcessingException {
        Integer id = 1;
        JsonPatch patch = criarJsonPatchDeAtualizacao_OK();

        when(repository.findById(id)).thenReturn(Optional.ofNullable(null));
        
        Exception exception = assertThrows(UsuarioNaoEncontradoException.class, () -> {
            service.atualizarUsuario(id, patch);
        });

        assertNotNull(exception);
    }

    @Test
    void test_deletarUsuario_UsuarioNaoEncontrado() {
        Integer id = 1;

        when(repository.findById(id)).thenReturn(Optional.ofNullable(null));
        
        Exception exception = assertThrows(UsuarioNaoEncontradoException.class, () -> {
            service.deletarUsuario(id);
        });

        assertNotNull(exception);
    }

    @Test
    void test_login_ComSucesso() {
        String email = "nomedeusuario@email.com";
        String password = "senha";

        when(repository.findFirstByEmail(email)).thenReturn(Optional.of(criarUsuario_OK()));

        var expected = criarResponseUsuarioLogadoDto_OK();
        var actual = service.login(email, password);

        assertEquals(expected, actual);
    }

    @Test
    void test_login_UsuarioComEmailNaoEncontrado() {
        String email = "nomedeusuario@email.com";
        String password = "senha";

        when(repository.findFirstByEmail(email)).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(LoginUsuarioNaoEncontradoComEmailException.class, () -> {
            service.login(email, password);
        });

        assertNotNull(exception);
    }

    @Test
    void test_login_UsuarioComEmailOuSenhaInvalido() {
        String email = "nomedeusuario@email.com";
        String password = "senhaerrada";

        when(repository.findFirstByEmail(email)).thenReturn(Optional.of(criarUsuario_OK()));

        Exception exception = assertThrows(LoginEmailOuSenhaIncorretoException.class, () -> {
            service.login(email, password);
        });

        assertNotNull(exception);        
    }

    private ResponseUsuarioDto criarResponseUsuarioDto_OK() {
        return ResponseUsuarioDto.builder()
                                 .id(1)
                                 .email("nomedeusuario@email.com")
                                 .nome("Nome")
                                 .sobrenome("sobrenome")
                                 .username("username")
                                 .build();
    }

    private RequestNovoUsuarioDto criarRequestNovoUsuarioDto_OK() {
        return RequestNovoUsuarioDto.builder()
                                    .email("nomedeusuario@email.com")
                                    .nome("Nome")
                                    .sobrenome("sobrenome")
                                    .username("username")
                                    .senha("senha")
                                    .build();
    }

    private ResponseUsuarioDto criarResponseUsuarioDtoAtualizado_OK() {
        return ResponseUsuarioDto.builder()
                                 .id(1)
                                 .email("emailatualizado@email.com")
                                 .nome("Nomeatualizado")
                                 .sobrenome("sobrenomeatualizado")
                                 .username("usernameatualizado")
                                 .build();
    }

    private JsonPatch criarJsonPatchDeAtualizacao_OK() throws JsonMappingException, JsonProcessingException {
        String updateJson = "[" 
                          + "{\"op\":\"replace\",\"path\":\"/email\",\"value\":\"emailatualizado@email.com\"},"
                          + "{\"op\":\"replace\",\"path\":\"/nome\",\"value\":\"Nomeatualizado\"},"
                          + "{\"op\":\"replace\",\"path\":\"/sobrenome\",\"value\":\"sobrenomeatualizado\"},"
                          + "{\"op\":\"replace\",\"path\":\"/username\",\"value\":\"usernameatualizado\"}"
                          + "]";

       
        return mapper.readValue(updateJson, JsonPatch.class);
    }

    private ResponseUsuarioLogadoDto criarResponseUsuarioLogadoDto_OK() {
        return ResponseUsuarioLogadoDto.builder()
                                       .email("nomedeusuario@email.com")
                                       .username("username")
                                       .token("")
                                       .build();
    }

    private Usuario criarUsuario_OK() {
        return new Usuario(usuario -> {
            usuario.setId(1);
            usuario.setEmail("nomedeusuario@email.com");
            usuario.setNome("Nome");
            usuario.setSobrenome("sobrenome");
            usuario.setUsername("username");
            usuario.setSenha(encoder.encode("senha"));
        });
    }

}
