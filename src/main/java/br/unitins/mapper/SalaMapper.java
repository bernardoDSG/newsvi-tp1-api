package br.unitins.mapper;

import br.unitins.dto.SalaRequestDTO;
import br.unitins.dto.SalaResponseDTO;
import br.unitins.model.Sala;


public class SalaMapper {
    
    

    public static Sala toEntity(SalaRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Sala sala = new Sala();
        sala.setPoltronas(dto.poltronas().stream()
                .map(PoltronaMapper::toEntity)
                .toList());
        return sala;
    }

    public static SalaResponseDTO toResponseDTO(Sala sala) {
        if (sala == null) {
            return null;
        }
        return new SalaResponseDTO(sala.getId(), sala.getPoltronas()
        .stream().map(PoltronaMapper:: toResponseDTO).toList());
    }
}
