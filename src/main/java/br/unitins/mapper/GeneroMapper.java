package br.unitins.mapper;

import br.unitins.dto.GeneroRequestDTO;
import br.unitins.dto.GeneroResponseDTO;
import br.unitins.model.Genero;

public class GeneroMapper {
    
    public static Genero toEntity(GeneroRequestDTO generoDTO) {
        if (generoDTO == null) {
            return null;
        }
        Genero genero = new Genero();
        genero.setNome(generoDTO.nome());
        return genero;
    }

    public static GeneroResponseDTO toResponseDTO (Genero genero) {
        if (genero == null) {
            return null;
        }
        return new GeneroResponseDTO(genero.getId(), genero.getNome());
    }
}
