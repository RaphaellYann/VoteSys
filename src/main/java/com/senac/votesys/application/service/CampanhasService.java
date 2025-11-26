package com.senac.votesys.application.service;

import com.senac.votesys.application.dto.campanha.CampanhasRequestDTO;
import com.senac.votesys.application.dto.campanha.CampanhasResponseDTO;
import com.senac.votesys.application.dto.usuario.UsuarioPrincipalDTO;
import com.senac.votesys.domain.entity.Campanhas;
import com.senac.votesys.domain.entity.Usuarios;
import com.senac.votesys.domain.repository.CampanhasRepository;
import com.senac.votesys.domain.repository.UsuariosRepository;
import com.senac.votesys.domain.repository.VotosRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private VotosRepository votosRepository;

    public CampanhasResponseDTO listarPorId(Long id, UsuarioPrincipalDTO authentication) {
        Optional<Campanhas> campanhaOptional;

        if (authentication.isAdminGeral()) {
            campanhaOptional = campanhasRepository.findById(id);
        } else if (authentication.isAdminNormal()) {
            campanhaOptional = campanhasRepository.findByIdAndUsuarioId(id, authentication.id());
        } else {
            campanhaOptional = Optional.empty();
        }

        return campanhaOptional
                .map(c -> {
                    boolean jaVotou = votosRepository.existsByUsuarioIdAndCampanhaId(authentication.id(), c.getId());
                    return CampanhasResponseDTO.fromEntity(c, jaVotou);
                })
                .orElse(null);
    }

    public List<CampanhasResponseDTO> listarCampanhasParaResultados(String filtro, UsuarioPrincipalDTO authentication) {

        List<Campanhas> listaCampanhas = campanhasRepository.findAll();
        return filtrarEMapear(listaCampanhas, filtro, authentication.id());
    }

    public List<CampanhasResponseDTO> listarCampanhasParaVotacao(String filtro, UsuarioPrincipalDTO authentication) {
        LocalDateTime agora = LocalDateTime.now();
        var lista = campanhasRepository.findByAtivoAndDataInicioBeforeAndDataFimAfter(true, agora, agora);
        return filtrarEMapear(lista, filtro, authentication.id());
    }

    public List<CampanhasResponseDTO> listarTodos(String filtro, UsuarioPrincipalDTO authentication) {
        List<Campanhas> listaCampanhas;

        if (authentication.isAdminGeral()) {
            listaCampanhas = campanhasRepository.findAll();
        } else if (authentication.isAdminNormal()) {
            listaCampanhas = campanhasRepository.findByUsuarioId(authentication.id());
        } else {
            listaCampanhas = List.of();
        }

        return filtrarEMapear(listaCampanhas, filtro, authentication.id());
    }

    @Transactional
    public CampanhasResponseDTO criarCampanha(CampanhasRequestDTO dto, UsuarioPrincipalDTO authentication) {

        if (!authentication.isAdminGeral() && !authentication.isAdminNormal()) {
            throw new RuntimeException("Usuário sem permissão de criar campanhas");
        }

        Usuarios usuarioCriador = usuariosRepository.findById(authentication.id())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        var campanha = new Campanhas();
        campanha.setTitulo(dto.titulo());
        campanha.setDescricao(dto.descricao());
        campanha.setDataInicio(dto.dataInicio());
        campanha.setDataFim(dto.dataFim());
        campanha.setAtivo(dto.ativo());
        campanha.setTipoCampanha(dto.tipoCampanha());
        campanha.setUsuario(usuarioCriador);

        var salvo = campanhasRepository.save(campanha);
        // Retorna o DTO direto
        return CampanhasResponseDTO.fromEntity(salvo, false);
    }

    @Transactional
    public CampanhasResponseDTO atualizarCampanha(Long id, CampanhasRequestDTO dto, UsuarioPrincipalDTO authentication) {
        var campanha = campanhasRepository.findById(id).orElse(null);

        if (campanha == null) {
            throw new RuntimeException("Campanha não encontrada");
        }

        if (!authentication.isAdminGeral() && !campanha.getUsuario().getId().equals(authentication.id())) {
            throw new RuntimeException("Você não tem permissão para editar esta campanha.");
        }

        campanha.setTitulo(dto.titulo());
        campanha.setDescricao(dto.descricao());
        campanha.setDataInicio(dto.dataInicio());
        campanha.setDataFim(dto.dataFim());
        campanha.setAtivo(dto.ativo());
        campanha.setTipoCampanha(dto.tipoCampanha());

        var atualizado = campanhasRepository.save(campanha);
        boolean jaVotou = votosRepository.existsByUsuarioIdAndCampanhaId(authentication.id(), atualizado.getId());

        return CampanhasResponseDTO.fromEntity(atualizado, jaVotou);
    }

    @Transactional
    public void excluirCampanha(Long id) {
        if (!campanhasRepository.existsById(id)) {
            throw new RuntimeException("Campanha não encontrada.");
        }
        campanhasRepository.deleteById(id);
    }

    private List<CampanhasResponseDTO> filtrarEMapear(List<Campanhas> lista, String filtro, Long usuarioId) {
        return lista.stream()
                .sorted(Comparator.comparing(Campanhas::getTituloUpper))
                .filter(a -> filtro == null || a.getTitulo().contains(filtro))
                .map(c -> {
                    boolean jaVotou = votosRepository.existsByUsuarioIdAndCampanhaId(usuarioId, c.getId());
                    return CampanhasResponseDTO.fromEntity(c, jaVotou);
                })
                .collect(Collectors.toList());
    }
}