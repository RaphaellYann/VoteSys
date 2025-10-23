package com.senac.votesys.presentation;

import com.senac.votesys.application.dto.campanhas.CampanhasRequestDTO;
import com.senac.votesys.application.dto.campanhas.CampanhasResponseDTO;
import com.senac.votesys.application.service.CampanhasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campanhas")
@Tag(name = "Controle de Campanhas", description = "Gerencia as campanhas de votação com diferentes tipos de regras")
public class CampanhasController {

    @Autowired
    private CampanhasService campanhasService;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar campanha por ID")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return campanhasService.listarPorId(id);
    }

    @GetMapping
    @Operation(summary = "Listar todas as campanhas")
    public ResponseEntity<List<CampanhasResponseDTO>> listarTodos(
            @RequestParam(required = false) String filtro) {
        return campanhasService.listarTodos(filtro);
    }


    @PostMapping
    @Operation(summary = "Criar nova campanha")
    public ResponseEntity<?> criar(@RequestBody CampanhasRequestDTO dto) {
        return campanhasService.criarCampanha(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar campanha existente")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody CampanhasRequestDTO dto) {
        return campanhasService.atualizarCampanha(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir campanha")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        return campanhasService.excluirCampanha(id);
    }
}
