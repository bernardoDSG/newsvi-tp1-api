package br.unitins.mapper;

import br.unitins.dto.AtorPremiadoRequestDTO;
import br.unitins.dto.AtorPremiadoResponseDTO;
import br.unitins.model.AtorPremiado;

public class AtorPremiadoMapper {
    
    public static AtorPremiado toEntity(AtorPremiadoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        AtorPremiado atorPremiado = new AtorPremiado();
        atorPremiado.setNome(dto.nome());
        return atorPremiado;
    }

    public static AtorPremiadoResponseDTO toResponseDTO (AtorPremiado atorPremiado) {
        if (atorPremiado == null) {
            return null;
        }
        return new AtorPremiadoResponseDTO(atorPremiado.getId(), atorPremiado.getNome(), atorPremiado.getPremiacao().stream().map(PremioMapper::toResponseDTO).toList());
    }
    
}
