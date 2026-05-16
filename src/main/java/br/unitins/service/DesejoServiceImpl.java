package br.unitins.service;

import java.util.List;

import br.unitins.dto.DesejoRequestDTO;
import br.unitins.dto.DesejoResponseDTO;
import br.unitins.exception.ValidationException;
import br.unitins.mapper.DesejoMapper;
import br.unitins.model.Desejo;
import br.unitins.model.Sessao;
import br.unitins.model.Usuario;
import br.unitins.repository.DesejoRepository;
import br.unitins.repository.SessaoRepository;
import br.unitins.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class DesejoServiceImpl implements DesejoService {

    @Inject
    DesejoRepository desejoRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    SessaoRepository sessaoRepository;

    @Override
    @Transactional
    public DesejoResponseDTO add(String login, DesejoRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado"));

        Sessao sessao = sessaoRepository.findById(dto.sessaoId());
        if (sessao == null) {
            throw new ValidationException("Sessao nao encontrada", "sessaoId");
        }

        if (desejoRepository.countByUsuarioAndSessaoId(usuario.getId(), dto.sessaoId()) > 0) {
            throw new ValidationException("Sessao ja adicionada na lista de desejos", "sessaoId");
        }

        Desejo desejo = new Desejo();
        desejo.setUsuario(usuario);
        desejo.setSessao(sessao);
        desejoRepository.persist(desejo);
        return DesejoMapper.toResponseDTO(desejo);
    }

    @Override
    @Transactional
    public void remove(String login, Long desejoId) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado"));
        Desejo desejo = desejoRepository.findById(desejoId);
        if (desejo == null || desejo.getUsuario() == null || !desejo.getUsuario().getId().equals(usuario.getId())) {
            throw new NotFoundException("Desejo nao encontrado para o usuario autenticado");
        }
        desejoRepository.delete(desejo);
    }

    @Override
    public List<DesejoResponseDTO> listByUsuario(String login) {
        return desejoRepository.listByUsuarioLogin(login).stream()
                .map(DesejoMapper::toResponseDTO)
                .toList();
    }
}

