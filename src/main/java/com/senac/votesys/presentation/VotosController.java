package com.senac.votesys.presentation;

import com.senac.votesys.application.dto.usuario.UsuarioPrincipalDTO;
import com.senac.votesys.application.dto.votos.VotosRequestDTO;
import com.senac.votesys.application.dto.votos.VotosResponseDTO;
import com.senac.votesys.application.service.VotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votos")
@Tag(name = "Controle de Votos", description = "Gerencia o registro e listagem de votos")
public class VotosController {

    @Autowired
    private VotacaoService votacaoService;

    @PostMapping
    @Operation(summary = "Registrar voto", description = "Registra o voto do usuário logado em uma campanha")
    public ResponseEntity<VotosResponseDTO> registrarVoto(
            @AuthenticationPrincipal UsuarioPrincipalDTO usuarioLogado,
            @RequestBody VotosRequestDTO dto) {
        try {
            var voto = votacaoService.registrarVoto(dto, usuarioLogado);
            return ResponseEntity.ok(voto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/campanha/{campanhaId}")
    @Operation(summary = "Listar votos de uma campanha", description = "Lista todos os votos de uma campanha")
    public ResponseEntity<List<VotosResponseDTO>> listarVotosPorCampanha(@PathVariable Long campanhaId) {
        try {
            return ResponseEntity.ok(votacaoService.listarVotosPorCampanha(campanhaId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}