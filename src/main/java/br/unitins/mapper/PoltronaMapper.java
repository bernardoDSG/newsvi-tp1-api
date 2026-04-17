package br.unitins.mapper;

import br.unitins.dto.PoltronaRequestDTO;
import br.unitins.dto.PoltronaResponseDTO;
import br.unitins.model.Disponibilidade;
import br.unitins.model.Poltrona;

public class PoltronaMapper {
   
    public static Poltrona toEntity(PoltronaRequestDTO dto) {
        if (dto == null) return null;
        Poltrona poltrona = new Poltrona();
        poltrona.setCodigo(dto.codigo());
        poltrona.setLinha(dto.linha());
        poltrona.setColuna(dto.coluna());
        if (dto.disponibilidadeId() != null) {
            poltrona.setDisponibilidade(Disponibilidade.valueOf(dto.disponibilidadeId()));
        }
        return poltrona;
    }

    public static PoltronaResponseDTO toResponseDTO(Poltrona poltrona) {
        if (poltrona == null) return null;
        return new PoltronaResponseDTO(
            poltrona.getId(),
            poltrona.getCodigo(),
            poltrona.getLinha(),
            poltrona.getColuna(),
            poltrona.getDisponibilidade() != null ? poltrona.getDisponibilidade().getNome() : null
        );
    }
}