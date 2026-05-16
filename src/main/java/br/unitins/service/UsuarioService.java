package br.unitins.service;

import java.util.List;

import br.unitins.dto.UsuarioResponseDTO;
import br.unitins.dto.UsuarioUpdateRequestDTO;
import br.unitins.model.Usuario;

public interface UsuarioService {

    UsuarioResponseDTO findCurrent(String login);

    UsuarioResponseDTO updateCurrent(String login, UsuarioUpdateRequestDTO dto);

    Usuario findByLogin(String login);

    List<UsuarioResponseDTO> listAll();
}
