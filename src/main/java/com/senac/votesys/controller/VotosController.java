package com.senac.votesys.controller;

import com.senac.votesys.model.Votos;
import com.senac.votesys.repository.VotosRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votos")
@Tag(name = "Controle de Votos", description = "Camada reponsável por controlar os registros de votos")
public class VotosController {

    @Autowired
    private VotosRepository votosRepository;

    @GetMapping("/{id}")
    @Operation(summary = "Listar Voto", description = "Método resposável por consultar votos por ID")
    public ResponseEntity<Votos> consultarVotosPorId(@PathVariable long id) {
        var votos = votosRepository.findById(id)
                .orElse(null);
        if (votos == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(votos);
    }

    @GetMapping
    @Operation(summary = "Listar todos Voto", description = "Método resposável por consultar todos votos")
    public ResponseEntity<List<Votos>> consultarVotos() {

        return ResponseEntity.ok(votosRepository.findAll());

    }

    @PostMapping
    @Operation(summary = "CriarVoto", description = "Método resposável por criar voto")
    public ResponseEntity<Votos> criarVotos(@RequestBody Votos votos) {

        try {

            var votoResponse = votosRepository.save(votos);

            return ResponseEntity.ok(votoResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Votos", description = "Método reposável em atualizar votos")
    public ResponseEntity atualizarVotos(@PathVariable long id, @RequestBody Votos votos) {

        if (!votosRepository.existsById(id)) {
            return ResponseEntity.notFound().build();

        }
        try {
            votos.setId(id);

            var votoResponse = votosRepository.save(votos);

            return ResponseEntity.ok(votoResponse);
        } catch (Exception e) {

            return ResponseEntity.badRequest().build();

        }
    }
}

