package br.unitins.service;

import br.unitins.dto.AuthRequestDTO;
import br.unitins.dto.AuthResponseDTO;

public interface AuthService {

    AuthResponseDTO login(AuthRequestDTO dto);
}
