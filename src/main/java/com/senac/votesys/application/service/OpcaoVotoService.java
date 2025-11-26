package com.senac.votesys.application.service;

import com.senac.votesys.application.dto.opcaoVoto.OpcaoVotoRequestDTO;
import com.senac.votesys.application.dto.opcaoVoto.OpcaoVotoResponseDTO;
import com.senac.votesys.application.dto.usuario.UsuarioPrincipalDTO;
import com.senac.votesys.domain.entity.OpcaoVoto;
import com.senac.votesys.domain.repository.CampanhasRepository;
import com.senac.votesys.domain.repository.OpcaoVotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpcaoVotoService {

    @Autowired
    private OpcaoVotoRepository opcaoVotoRepository;

    @Autowired
    private CampanhasRepository campanhasRepository;

    public OpcaoVotoResponseDTO consultarPorId(long id) {
        var opcao = opcaoVotoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opção de voto não encontrada"));
        return OpcaoVotoResponseDTO.fromEntity(opcao);
    }

    public List<OpcaoVotoResponseDTO> listarTodos() {
        return opcaoVotoRepository.findAll().stream()
                .map(OpcaoVotoResponseDTO::fromEntity)
                .toList();
    }

    public List<OpcaoVotoResponseDTO> listarPorCampanha(long campanhaId) {
        return opcaoVotoRepository.findByCampanhaId(campanhaId).stream()
                .map(OpcaoVotoResponseDTO::fromEntity)
                .toList();
    }

    public OpcaoVotoResponseDTO criarOpcaoVoto(OpcaoVotoRequestDTO dto, UsuarioPrincipalDTO authentication) {

        var campanha = campanhasRepository.findById(dto.campanhaId())
                .orElseThrow(() -> new RuntimeException("Campanha não encontrada"));

        if (!authentication.isAdminGeral() &&
                (campanha.getUsuario() == null || !campanha.getUsuario().getId().equals(authentication.id())))
        {

            throw new RuntimeException("Acesso negado: você não tem permissão para modificar esta campanha.");
        }

        OpcaoVoto novaOpcao = new OpcaoVoto();
        novaOpcao.setNome(dto.nome());
        novaOpcao.setCampanha(campanha);
        novaOpcao.setTotalVotos(0);

        var opcaoSalva = opcaoVotoRepository.save(novaOpcao);
        return OpcaoVotoResponseDTO.fromEntity(opcaoSalva);
    }

    public OpcaoVotoResponseDTO atualizarOpcaoVoto(long id, OpcaoVotoRequestDTO dto, UsuarioPrincipalDTO authentication) {
        var opcao = opcaoVotoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opção de voto não encontrada"));

        var campanha = campanhasRepository.findById(dto.campanhaId())
                .orElseThrow(() -> new RuntimeException("Campanha não encontrada"));

        if (!authentication.isAdminGeral() &&
                (campanha.getUsuario() == null || !campanha.getUsuario().getId().equals(authentication.id())))
        {

            throw new RuntimeException("Acesso negado: você não tem permissão para modificar esta campanha.");
        }


        if (!opcao.getCampanha().getId().equals(campanha.getId())) {
            throw new RuntimeException("A opção de voto não pertence à campanha informada.");
        }

        opcao.setNome(dto.nome());
        opcao.setCampanha(campanha);

        var opcaoAtualizada = opcaoVotoRepository.save(opcao);
        return OpcaoVotoResponseDTO.fromEntity(opcaoAtualizada);
    }

    public void excluirOpcaoVoto(long id, UsuarioPrincipalDTO authentication) {
        var opcao = opcaoVotoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opção de voto não encontrada"));

        var campanha = opcao.getCampanha();


        if (!authentication.isAdminGeral() &&
                (campanha.getUsuario() == null || !campanha.getUsuario().getId().equals(authentication.id())))
        {

            throw new RuntimeException("Acesso negado: você não tem permissão para modificar esta campanha.");
        }

        opcaoVotoRepository.deleteById(id);
    }
}