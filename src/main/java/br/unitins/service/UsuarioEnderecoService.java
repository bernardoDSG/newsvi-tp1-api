package br.unitins.service;

import java.util.List;

import br.unitins.dto.UsuarioEnderecoRequestDTO;
import br.unitins.dto.UsuarioEnderecoResponseDTO;

public interface UsuarioEnderecoService {

    List<UsuarioEnderecoResponseDTO> listByUsuario(String login);

    UsuarioEnderecoResponseDTO addEndereco(String login, UsuarioEnderecoRequestDTO dto);
}

