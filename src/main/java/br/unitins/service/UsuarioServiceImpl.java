package br.unitins.service;

import java.util.List;

import br.unitins.dto.UsuarioResponseDTO;
import br.unitins.dto.UsuarioUpdateRequestDTO;
import br.unitins.exception.ValidationException;
import br.unitins.mapper.UsuarioMapper;
import br.unitins.model.Usuario;
import br.unitins.repository.UsuarioRepository;
import br.unitins.repository.UsuarioEnderecoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    UsuarioEnderecoRepository enderecoRepository;

    @Override
    public UsuarioResponseDTO findCurrent(String login) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado"));
        return UsuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO updateCurrent(String login, UsuarioUpdateRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado"));

        if (dto.email() != null && !dto.email().equals(usuario.getEmail())) {
            validateUniqueEmail(dto.email());
            usuario.setEmail(dto.email());
        }
        if (dto.nome() != null) {
            usuario.setNome(dto.nome());
        }
        return UsuarioMapper.toResponseDTO(usuario);
    }

    @Override
    public Usuario findByLogin(String login) {
        return usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado"));
    }

    @Override
    public List<UsuarioResponseDTO> listAll() {
        return usuarioRepository.listAll().stream().map(UsuarioMapper::toResponseDTO).toList();
    }

    private void validateUniqueEmail(String email) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new ValidationException("Email ja cadastrado", "email");
        }
    }
}
