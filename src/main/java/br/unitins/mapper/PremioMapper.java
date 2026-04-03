package br.unitins.mapper;

import br.unitins.dto.PremioRequestDTO;
import br.unitins.dto.PremioResponseDTO;
import br.unitins.model.Premio;

public class PremioMapper {
   
    public static Premio toEntity(PremioRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Premio premio = new Premio();
        premio.setNome(dto.nome());
        premio.setAno(dto.ano());
        premio.setCategoria(dto.categoria());
        return premio;
    }

    public static PremioResponseDTO toResponseDTO(Premio premio) {
        if (premio == null) {
            return null;
        }
        return new PremioResponseDTO(
            premio.getId(), 
            premio.getNome(), 
            premio.getAno(),
            premio.getCategoria()
        );
    }
}