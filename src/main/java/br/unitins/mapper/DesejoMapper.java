package br.unitins.mapper;

import br.unitins.dto.DesejoResponseDTO;
import br.unitins.model.Desejo;

public class DesejoMapper {

    public static DesejoResponseDTO toResponseDTO(Desejo desejo) {
        if (desejo == null) {
            return null;
        }
        return new DesejoResponseDTO(
            desejo.getId(),
            desejo.getSessao() != null ? desejo.getSessao().getId() : null,
            desejo.getSessao() != null && desejo.getSessao().getFilme() != null ? desejo.getSessao().getFilme().getNome() : null,
            desejo.getSessao() != null && desejo.getSessao().getCinema() != null ? desejo.getSessao().getCinema().getNome() : null,
            desejo.getUsuario() != null ? desejo.getUsuario().getId() : null,
            desejo.getUsuario() != null ? desejo.getUsuario().getLogin() : null
        );
    }
}

