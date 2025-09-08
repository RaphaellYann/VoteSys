package com.senac.votesys.controller;

import com.senac.votesys.model.Campanhas;
import com.senac.votesys.repository.CampanhasRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campanhas")
@Tag(name = "Controle de Campanhas", description = "Camada responsável por controlar os regisros de campanhas")
public class CampanhasController {

    @Autowired
    private CampanhasRepository campanhasRepository;

    @GetMapping("/{id}")
    @Operation(summary = "Listar Campanha", description = "Método reponsável por consultar campanhas por ID")
    public ResponseEntity<Campanhas> consultaPorId(@PathVariable long id){
        var campanha = campanhasRepository.findById(id)
                .orElse(null);
        if (campanha == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(campanha);

    }

    @GetMapping
    @Operation(summary = "Listar Todas Campanhas", description = "Método responsável por consultar todas as campanhas")
    public ResponseEntity<?> consultarTodos(){

        return ResponseEntity.ok(campanhasRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Criar Campanha", description = "Método Reponsável em criar uma nova campanha")
    public ResponseEntity<?> criarCampanha(@RequestBody Campanhas campanha) {

        try{

            var campanhaResponse = campanhasRepository.save(campanha);

            return ResponseEntity.ok(campanhaResponse);

    }catch (Exception e){
        return ResponseEntity.badRequest().build();}
    }

    @PutMapping("/atualizar/{id}")
    @Operation(summary = "Atualiza Campanha", description = "Método reposável em atualizar daodos de uma campanha")
    public ResponseEntity<?> atualizarCampanha(@PathVariable long id, @RequestBody Campanhas campanha){

        if (!campanhasRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        try{
            campanha.setId(id);
            var campanhaResponse = campanhasRepository.save(campanha);
            return ResponseEntity.ok(campanhaResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

}
