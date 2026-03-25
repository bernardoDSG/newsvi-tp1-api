package br.unitins.mapper;

import br.unitins.dto.AtorPremiadoRequestDTO;
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
    
}
