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
        sala.setCapacidade(dto.capacidade());
        return sala;
    }

    public static SalaResponseDTO toResponseDTO(Sala sala) {
        if (sala == null) {
            return null;
        }
        return new SalaResponseDTO(
            sala.getId(),
            sala.getCapacidade(),
            sala.getPoltronas() != null ? sala.getPoltronas().stream().map(p -> p.getCodigo()).toList() : null
        );
    }
}