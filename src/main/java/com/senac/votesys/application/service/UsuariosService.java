package com.senac.votesys.application.service;

import com.senac.votesys.application.dto.login.LoginRequestDTO;
import com.senac.votesys.application.dto.usuario.*;
import com.senac.votesys.domain.entity.Usuarios;
import com.senac.votesys.domain.interfaces.IEnvioEmail;
import com.senac.votesys.domain.repository.UsuariosRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuariosService {

    @Autowired
    private UsuariosRepository usuarioRepository;

    @Autowired
    private IEnvioEmail envioEmail;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${votesys.frontend.base-url}")
    private String frontendBaseUrl;

    public boolean validarSenha(LoginRequestDTO loginRequestDto) {
        var usuarioOptional = usuarioRepository.findByEmail(loginRequestDto.email());

        if (usuarioOptional.isEmpty()) return false;

        var usuario = usuarioOptional.get();
        return passwordEncoder.matches(loginRequestDto.senha(), usuario.getSenha());
    }

    @Transactional
    public UsuarioResponseDTO consultarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioResponseDTO::new)
                .orElse(null); // Retorna null para o controller tratar 404
    }

    public List<UsuarioResponseDTO> consultarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO usuarioRequest) {
        var usuarioExistente = usuarioRepository.findByEmail(usuarioRequest.email());

        if (usuarioExistente.isPresent()) {
            throw new RuntimeException("E-mail já cadastrado. Use outro e-mail ou faça login.");
        }

        var novoUsuario = new Usuarios(usuarioRequest);
        novoUsuario.setRole("ROLE_USER");
        novoUsuario.setSenha(passwordEncoder.encode(usuarioRequest.senha()));

        usuarioRepository.save(novoUsuario);

        return novoUsuario.toDtoResponse();
    }

    @Transactional
    public UsuarioResponseDTO salvarUsuario(Long id, UsuarioRequestDTO dto, UsuarioPrincipalDTO usuarioLogado) {

        var usuarioParaAtualizar = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário com ID " + id + " não encontrado."));

        // Verifica se o novo email já está em uso por OUTRA pessoa
        var emailExistente = usuarioRepository.findByEmail(dto.email());
        if (emailExistente.isPresent() && !emailExistente.get().getId().equals(usuarioParaAtualizar.getId())) {
            throw new RuntimeException("Este e-mail já está em uso por outra conta.");
        }

        usuarioParaAtualizar.setNome(dto.nome());
        usuarioParaAtualizar.setEmail(dto.email());
        usuarioParaAtualizar.setCpf(dto.cpf());

        if (usuarioLogado.isAdminGeral()) {
            usuarioParaAtualizar.setRole(dto.role());
        }

        usuarioRepository.save(usuarioParaAtualizar);
        return usuarioParaAtualizar.toDtoResponse();
    }

    @Transactional
    public List<UsuarioResponseDTO> consultarPaginaFiltrado(Long take, Long page, String filtro) {
        return usuarioRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Usuarios::getId).reversed())
                .filter(p -> p.getDataCadastro().isAfter(LocalDateTime.now().minusDays(7)))
                .filter(a -> filtro != null ? a.getNome().contains(filtro) : true)
                .skip(page * take)
                .limit(take)
                .map(UsuarioResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void gerarLinkRecuperacao(RecuperarSenhaDTO recuperarSenhaDTO) {
        var usuarioOptional = usuarioRepository.findByEmail(recuperarSenhaDTO.email());

        if (usuarioOptional.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado com este e-mail.");
        }

        var usuario = usuarioOptional.get();

        String token = UUID.randomUUID().toString();
        usuario.setTokenSenha(token);
        usuario.setTokenSenhaExpiracao(LocalDateTime.now().plusDays(1));
        usuarioRepository.save(usuario);

        String link = frontendBaseUrl + "/resetarsenha?token=" + token;

        envioEmail.enviarEmailTemplate(
                usuario.getEmail(),
                "Recuperação de Senha - VoteSys",
                link
        );
    }

    @Transactional
    public void resetarSenha(ResetarSenhaDTO resetarSenhaDTO) {
        var usuarioOptional = usuarioRepository.findByTokenSenha(resetarSenhaDTO.token());

        if (usuarioOptional.isEmpty()) {
            throw new RuntimeException("Token inválido ou expirado.");
        }

        var usuario = usuarioOptional.get();

        if (usuario.getTokenSenhaExpiracao() == null ||
                usuario.getTokenSenhaExpiracao().isBefore(LocalDateTime.now())) {

            usuario.setTokenSenha(null);
            usuario.setTokenSenhaExpiracao(null);
            usuarioRepository.save(usuario);
            throw new RuntimeException("Token inválido ou expirado.");
        }

        usuario.setSenha(passwordEncoder.encode(resetarSenhaDTO.senha()));
        usuario.setTokenSenha(null);
        usuario.setTokenSenhaExpiracao(null);

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void alterarSenha(AlterarSenhaDTO alterarSenhaDTO) {
        var usuarioOptional = usuarioRepository.findByEmail(alterarSenhaDTO.email());

        if (usuarioOptional.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        var usuario = usuarioOptional.get();

        if (!passwordEncoder.matches(alterarSenhaDTO.senhaAtual(), usuario.getSenha())) {
            throw new RuntimeException("Senha atual incorreta.");
        }

        usuario.setSenha(passwordEncoder.encode(alterarSenhaDTO.novaSenha()));
        usuarioRepository.save(usuario);
    }

    public UsuarioResponseDTO consultarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(UsuarioResponseDTO::new)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }
}