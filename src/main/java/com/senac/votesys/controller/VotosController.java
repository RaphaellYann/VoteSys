package com.senac.votesys.controller;

import com.senac.votesys.dto.VotosRequestDTO;
import com.senac.votesys.model.Usuarios;
import com.senac.votesys.service.VotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votos")
@Tag(name = "Controle de Votos", description = "Gerencia o registro e listagem de votos")
public class VotosController {

    @Autowired
    private VotacaoService votacaoService;

    @PostMapping
    @Operation(summary = "Registrar voto", description = "Registra o voto do usuário logado em uma campanha")
    public ResponseEntity<?> registrarVoto(
            @AuthenticationPrincipal Usuarios usuario, //dto especido pra rule id e menus do usuario
            @RequestBody VotosRequestDTO dto) {

        return votacaoService.registrarVoto(dto, usuario);
    }

    @GetMapping("/campanha/{campanhaId}")
    @Operation(summary = "Listar votos de uma campanha", description = "Lista todos os votos de uma campanha")
    public ResponseEntity<?> listarVotosPorCampanha(@PathVariable Long campanhaId) {
        return votacaoService.listarVotosPorCampanha(campanhaId);
    }
}
