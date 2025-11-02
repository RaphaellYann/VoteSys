package com.senac.votesys.application.service;

import com.senac.votesys.application.dto.campanha.CampanhasRequestDTO;
import com.senac.votesys.application.dto.campanha.CampanhasResponseDTO;
import com.senac.votesys.application.dto.usuario.UsuarioPrincipalDTO;
import com.senac.votesys.domain.entity.Campanhas;
import com.senac.votesys.domain.entity.Usuarios;
import com.senac.votesys.domain.repository.CampanhasRepository;
import com.senac.votesys.domain.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CampanhasService {

    @Autowired
    private CampanhasRepository campanhasRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    public ResponseEntity<CampanhasResponseDTO> listarPorId(Long id, @AuthenticationPrincipal UsuarioPrincipalDTO authentication
    ) {
        Optional<Campanhas> campanhaOptional;

        if (authentication.isAdminGeral()) {
            // ADMIN GERAL: busca qualquer campanha
            campanhaOptional = campanhasRepository.findById(id);

        } else if (authentication.isAdminNormal()) {
            // ADMIN NORMAL: busca apenas campanhas dele
            campanhaOptional = campanhasRepository.findByIdAndUsuarioId(id, authentication.id());
        } else {

            campanhaOptional = Optional.empty();
        }

        return campanhaOptional
                .map(campanha -> ResponseEntity.ok(CampanhasResponseDTO.fromEntity(campanha)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    public ResponseEntity<List<CampanhasResponseDTO>> listarTodos(String filtro, UsuarioPrincipalDTO authentication) {
        List<Campanhas> listaCampanhas;

        if(authentication.isAdminGeral()){

            listaCampanhas = campanhasRepository.findAll();

        } else if (authentication.isAdminNormal()) {

            listaCampanhas = campanhasRepository.findByUsuarioId(authentication.id());
        } else {

            listaCampanhas = List.of();
        }

        var listaFiltrada = listaCampanhas.stream()
                .sorted(Comparator.comparing(Campanhas::getTituloUpper))
                .filter( a -> filtro == null || a.getTitulo().contains(filtro))
                .map(CampanhasResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(listaFiltrada);
    }

    public ResponseEntity<?> criarCampanha(CampanhasRequestDTO dto, UsuarioPrincipalDTO authentication) {

        if (!authentication.isAdminGeral() && !authentication.isAdminNormal()){

            return ResponseEntity.status(403).body("Usuário sem permisão de criar campanhas");
        }

        Usuarios usuarioCriador = usuariosRepository.findById(authentication.id())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado.")
                );
        try {
            var campanha = new Campanhas();
            campanha.setTitulo(dto.titulo());
            campanha.setDescricao(dto.descricao());
            campanha.setDataInicio(dto.dataInicio());
            campanha.setDataFim(dto.dataFim());
            campanha.setAtivo(dto.ativo());
            campanha.setVotacaoAnonima(dto.votacaoAnonima());
            campanha.setTipoCampanha(dto.tipoCampanha());
            campanha.setUsuario(usuarioCriador);

            var salvo = campanhasRepository.save(campanha);

            return ResponseEntity.ok(CampanhasResponseDTO.fromEntity(salvo));

        } catch (Exception e) {

            return ResponseEntity.badRequest().body("Erro ao criar campanha: " + e.getMessage());
        }
    }

    public ResponseEntity<?> atualizarCampanha(Long id, CampanhasRequestDTO dto, UsuarioPrincipalDTO authentication) {
        var campanha = campanhasRepository.findById(id).orElse(null);

        if (campanha == null) {

            return ResponseEntity.notFound().build();
        }

        if (!authentication.isAdminGeral() && !campanha.getUsuario().getId().equals(authentication.id())) {
            // Se não for Admin Geral E não for o dono da campanha
            return ResponseEntity.status(403).body("Você não tem permissão para editar esta campanha.");
        }


        try {
            campanha.setTitulo(dto.titulo());
            campanha.setDescricao(dto.descricao());
            campanha.setDataInicio(dto.dataInicio());
            campanha.setDataFim(dto.dataFim());
            campanha.setAtivo(dto.ativo());
            campanha.setVotacaoAnonima(dto.votacaoAnonima());
            campanha.setTipoCampanha(dto.tipoCampanha());

            var atualizado = campanhasRepository.save(campanha);
            return ResponseEntity.ok(CampanhasResponseDTO.fromEntity(atualizado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar campanha: " + e.getMessage());
        }
    }

    public ResponseEntity<?> excluirCampanha(Long id) {
        if (!campanhasRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        try {
            campanhasRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao excluir campanha: " + e.getMessage());
        }
    }

    public ResponseEntity<List<CampanhasResponseDTO>> listarCampanhasParaVotacao(String filtro) { // <-- 1. ADICIONADO "filtro"
        LocalDateTime agora = LocalDateTime.now();

        var lista = campanhasRepository.findByAtivoAndDataInicioBeforeAndDataFimAfter(true, agora, agora)
                .stream()
                .sorted(Comparator.comparing(Campanhas::getTituloUpper))
                .filter( a -> filtro == null || a.getTitulo().contains(filtro))
                .map(CampanhasResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }
}

