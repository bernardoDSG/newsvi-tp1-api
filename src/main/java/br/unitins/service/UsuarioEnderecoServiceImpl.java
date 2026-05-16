package br.unitins.service;

import java.util.List;

import br.unitins.dto.UsuarioEnderecoRequestDTO;
import br.unitins.dto.UsuarioEnderecoResponseDTO;
import br.unitins.mapper.UsuarioEnderecoMapper;
import br.unitins.model.Usuario;
import br.unitins.model.UsuarioEndereco;
import br.unitins.repository.UsuarioEnderecoRepository;
import br.unitins.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UsuarioEnderecoServiceImpl implements UsuarioEnderecoService {

    @Inject
    UsuarioEnderecoRepository enderecoRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Override
    public List<UsuarioEnderecoResponseDTO> listByUsuario(String login) {
        return enderecoRepository.listByUsuarioLogin(login).stream()
                .map(UsuarioEnderecoMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public UsuarioEnderecoResponseDTO addEndereco(String login, UsuarioEnderecoRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado"));

        UsuarioEndereco endereco = UsuarioEnderecoMapper.toEntity(dto);
        endereco.setUsuario(usuario);
        enderecoRepository.persist(endereco);
        return UsuarioEnderecoMapper.toResponseDTO(endereco);
    }
}

