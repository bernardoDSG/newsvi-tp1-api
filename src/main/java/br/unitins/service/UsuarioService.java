package br.unitins.service;

import java.util.List;

import br.unitins.dto.UsuarioCompleteRegisterRequestDTO;
import br.unitins.dto.UsuarioPasswordChangeRequestDTO;
import br.unitins.dto.UsuarioPasswordForgotRequestDTO;
import br.unitins.dto.UsuarioRegisterRequestDTO;
import br.unitins.dto.UsuarioResponseDTO;
import br.unitins.dto.UsuarioUpdateRequestDTO;
import br.unitins.model.Usuario;

public interface UsuarioService {

    UsuarioResponseDTO register(UsuarioRegisterRequestDTO dto);

    UsuarioResponseDTO registerComplete(UsuarioCompleteRegisterRequestDTO dto);

    UsuarioResponseDTO findCurrent(String login);

    UsuarioResponseDTO updateCurrent(String login, UsuarioUpdateRequestDTO dto);

    void changePassword(String login, UsuarioPasswordChangeRequestDTO dto);

    void forgotPassword(UsuarioPasswordForgotRequestDTO dto);

    Usuario findByLogin(String login);

    List<UsuarioResponseDTO> listAll();
}
