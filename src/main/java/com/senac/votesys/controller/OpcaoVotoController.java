package com.senac.votesys.controller;

import com.senac.votesys.repository.OpcaoVotoRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/opcaoVoto")
@Tag(name = "Contador de Votos", description = "Camada resposável por controle do contador de votos")
public class OpcaoVotoController {

    @Autowired
    private OpcaoVotoRepository opcaoVotoRepository;
}
