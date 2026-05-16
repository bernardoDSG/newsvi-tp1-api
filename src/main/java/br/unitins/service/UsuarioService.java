package br.unitins.service;

import java.util.List;

import br.unitins.dto.UsuarioCadastroCompletoRequestDTO;
import br.unitins.dto.UsuarioCadastroRequestDTO;
import br.unitins.dto.UsuarioResponseDTO;
import br.unitins.dto.UsuarioUpdateRequestDTO;
import br.unitins.model.Perfil;
import br.unitins.model.Usuario;

public interface UsuarioService {

    UsuarioResponseDTO createCurrent(String login, Perfil perfil, UsuarioCadastroRequestDTO dto);

    UsuarioResponseDTO createCurrentCompleto(String login, Perfil perfil, UsuarioCadastroCompletoRequestDTO dto);

    UsuarioResponseDTO findCurrent(String login);

    UsuarioResponseDTO updateCurrent(String login, UsuarioUpdateRequestDTO dto);

    Usuario findByLogin(String login);

    List<UsuarioResponseDTO> listAll();
}
