package br.com.lutadeclasses.usuarioservice.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lutadeclasses.usuarioservice.model.RequestLoginDto;
import br.com.lutadeclasses.usuarioservice.model.ResponseUsuarioLogadoDto;
import br.com.lutadeclasses.usuarioservice.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/login")
@Api(value = "Login")
public class LoginController extends BaseController {
    
    private UsuarioService service;

    public LoginController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping()
    @ApiOperation(value = "Realiza Login na Aplicação")
    @ApiResponses(
        {
            @ApiResponse(code = 200, message = "Login efetuado com Sucesso"),
            @ApiResponse(code = 401, message = "Não Autorizado"),
            @ApiResponse(code = 500, message = "Erro interno")
        }
    )
    public ResponseEntity<ResponseUsuarioLogadoDto> login(@Valid @RequestBody RequestLoginDto login) {       
        return this.ok(service.login(login.getEmail(), login.getSenha()));
    }

}
