package br.unitins.service;

import java.util.List;

import br.unitins.dto.DesejoRequestDTO;
import br.unitins.dto.DesejoResponseDTO;

public interface DesejoService {

    DesejoResponseDTO add(String login, DesejoRequestDTO dto);

    void remove(String login, Long desejoId);

    List<DesejoResponseDTO> listByUsuario(String login);
}

