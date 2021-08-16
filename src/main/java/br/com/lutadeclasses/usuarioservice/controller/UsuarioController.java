package br.com.lutadeclasses.usuarioservice.controller;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lutadeclasses.usuarioservice.model.RequestNovoUsuarioDto;
import br.com.lutadeclasses.usuarioservice.model.ResponseUsuarioDto;
import br.com.lutadeclasses.usuarioservice.service.UsuarioService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController extends BaseController {

    private UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping()
    @ApiOperation(value = "Listar todos os usuários")
    @ApiResponses(
        {
            @ApiResponse(code = 200, message = "Sucesso"),
            @ApiResponse(code = 500, message = "Erro interno")
        }
    )
    public List<ResponseUsuarioDto> listarUsuarios() {
        return service.listarUsuarios();
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Buscar usuário por id")
    @ApiResponses(
        {
            @ApiResponse(code = 200, message = "Sucesso"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Erro interno")
        }
    )
    public ResponseEntity<ResponseUsuarioDto> buscarUsuario(@PathVariable Integer id) {
        return this.ok(service.buscarUsuarioPorId(id));
    }

    @PostMapping()
    @ApiOperation(value = "Criar novo usuário")
    @ApiResponses(
        {
            @ApiResponse(code = 201, message = "Sucesso"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Erro interno")
        }
    )
    public ResponseEntity<ResponseUsuarioDto> criarUsuario(@Valid @RequestBody RequestNovoUsuarioDto novoUsuario) {
        return this.created(service.criarUsuario(novoUsuario));
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    @ApiOperation(value = "Atualizar registro de usuário")
    @ApiResponses(
        {
            @ApiResponse(code = 200, message = "Sucesso"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Erro interno")
        }
    )
    public ResponseEntity<ResponseUsuarioDto> atualizarUsuario(@PathVariable Integer id, @RequestBody JsonPatch patch) throws JsonProcessingException, JsonPatchException {
        return ResponseEntity.ok(service.atualizarUsuario(id, patch));
    }

    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Deletar registro de usuário")
    @ApiResponses(
        {
            @ApiResponse(code = 200, message = "Sucesso"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Erro interno")
        }
    )
    public void deletarUsuario(@PathVariable Integer id) {
        service.deletarUsuario(id);
    }
    
}
