package br.unitins.mapper;

import br.unitins.dto.AtorRequestDTO;
import br.unitins.dto.AtorResponseDTO;
import br.unitins.model.Ator;

public class AtorMapper {
   
    public static Ator toEntity(AtorRequestDTO dto) {
        if (dto == null) return null;
        Ator ator = new Ator();
        ator.setNome(dto.nome());
        ator.setDataNascimento(dto.dataNascimento());
        ator.setNacionalidade(dto.nacionalidade());
        ator.setUrlFoto(dto.urlFoto());  // NOVO
        return ator;
    }

    public static AtorResponseDTO toResponseDTO(Ator ator) {
        if (ator == null) return null;
        return new AtorResponseDTO(
            ator.getId(),
            ator.getNome(),
            ator.getDataNascimento(),
            ator.getNacionalidade(),
            ator.getUrlFoto(),  // NOVO
            ator.getPremios() != null ? ator.getPremios().stream()
                .map(PremioMapper::toResponseDTO)
                .toList() : null
        );
    }
}