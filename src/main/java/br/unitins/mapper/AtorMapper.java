package br.unitins.mapper;

import br.unitins.dto.AtorRequestDTO;
import br.unitins.dto.AtorResponseDTO;
import br.unitins.model.Ator;

public class AtorMapper {
   
    public static Ator toEntity(AtorRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Ator ator = new Ator();
        ator.setNome(dto.nome());
        return ator;
    }

    public static AtorResponseDTO toResponseDTO(Ator ator) {
        if (ator == null) {
            return null;
        }
        return new AtorResponseDTO(
            ator.getId(),
            ator.getNome(),
            ator.getPremios() != null ? ator.getPremios().stream()
                .map(PremioMapper::toResponseDTO)
                .toList() : null
        );
    }
}