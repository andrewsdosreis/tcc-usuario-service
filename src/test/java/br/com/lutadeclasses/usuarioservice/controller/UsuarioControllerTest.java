package br.com.lutadeclasses.usuarioservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.lutadeclasses.usuarioservice.entity.Usuario;
import br.com.lutadeclasses.usuarioservice.exception.handler.GlobalExceptionHandler;
import br.com.lutadeclasses.usuarioservice.model.RequestNovoUsuarioDto;
import br.com.lutadeclasses.usuarioservice.model.ResponseUsuarioDto;
import br.com.lutadeclasses.usuarioservice.model.ResponseUsuarioLogadoDto;
import br.com.lutadeclasses.usuarioservice.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {
    
    MockMvc mvc;
    ObjectMapper mapper;
    @InjectMocks UsuarioController controller;
    @InjectMocks GlobalExceptionHandler handler;
    @Mock UsuarioService service;

    @BeforeEach
    public void prepararTestes() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                             .setControllerAdvice(handler)
                             .build();
    }

    @Test
    void test_buscarUsuario_ComSucesso() throws Exception {
        when(service.buscarUsuarioPorId(any())).thenReturn(criarResponseUsuarioDto_OK());
        mvc.perform(get("/usuarios/1").contentType("application/json")).andExpect(status().isOk());
    }
   
    private ResponseUsuarioDto criarResponseUsuarioDto_OK() {
        return ResponseUsuarioDto.builder()
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
}
