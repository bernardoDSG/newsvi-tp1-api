package br.unitins.mapper;

import br.unitins.dto.AtorRequestDTO;
import br.unitins.dto.AtorResponseDTO;
import br.unitins.model.Ator;

public class AtorMapper {
   
    public static Ator toEntity(AtorRequestDTO atorDTO) {
        if (atorDTO == null) {
            return null;
        }
        Ator ator = new Ator();
        ator.setNome(atorDTO.nome());
        return ator;
    }

    public static AtorResponseDTO toResponseDTO (Ator ator) {
        if (ator == null) {
            return null;
        }
        return new AtorResponseDTO(ator.getId(), ator.getNome());
    }
}
