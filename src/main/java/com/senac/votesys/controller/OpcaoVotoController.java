package com.senac.votesys.controller;

import com.senac.votesys.model.OpcaoVoto;
import com.senac.votesys.repository.OpcaoVotoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/opcaoVoto")
@Tag(name = "Contador de Votos", description = "Camada resposável por controle do contador de votos")
public class OpcaoVotoController {

    @Autowired
    private OpcaoVotoRepository opcaoVotoRepository;

    @GetMapping("/{id}")
    @Operation(summary = "Listar Opcão Voto", description = "Método resposável por consultar opcão votos por ID")
    public ResponseEntity<OpcaoVoto> consultarOpcaoVotoPorId(@PathVariable Long id){
        var opcaoVoto = opcaoVotoRepository.findById(id)
                .orElse(null);
        if(opcaoVoto == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(opcaoVoto);
    }

    @GetMapping
    @Operation(summary = "Listar Todos Opcão Voto", description = "Método resposável por contular todos opcão votos")
    public ResponseEntity<?> consultarOpcaoVotos(){

        return  ResponseEntity.ok(opcaoVotoRepository.findAll());

    }

    @PostMapping
    @Operation(summary = "Criar Opcão Voto", description = "Método resposável por criar opção voto")
    public ResponseEntity<?> criarOpcaoVoto(@RequestBody OpcaoVoto opcaoVoto) {

        try {

            var opcaoVotoResponse = opcaoVotoRepository.save(opcaoVoto);

            return ResponseEntity.ok(opcaoVotoResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Opção Voto", description = "Método resposável por atualizar opção voto")
    public ResponseEntity<?> atualizarOpcaoVoto(@PathVariable Long id, @RequestBody OpcaoVoto opcaoVoto){

        if(!opcaoVotoRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        try{
            opcaoVoto.setId(id);
            var opcaoVotoResponse = opcaoVotoRepository.save(opcaoVoto);
            return ResponseEntity.ok(opcaoVotoResponse);

        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir Opcão Voto", description = "Método reposável em excluir uma Opção Voto")
    public ResponseEntity<?> excluirOpcaoVoto (@PathVariable Long id){

        if(!opcaoVotoRepository.existsById(id)){

            return ResponseEntity.notFound().build();
        }

        try{
            opcaoVotoRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {

            return ResponseEntity.badRequest().build();
        }

    }
}
