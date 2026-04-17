package br.unitins.mapper;

import br.unitins.dto.DiretorRequestDTO;
import br.unitins.dto.DiretorResponseDTO;
import br.unitins.model.Diretor;

public class DiretorMapper {
   
    public static Diretor toEntity(DiretorRequestDTO dto) {
        if (dto == null) return null;
        Diretor diretor = new Diretor();
        diretor.setNome(dto.nome());
        diretor.setDataNascimento(dto.dataNascimento());
        diretor.setNacionalidade(dto.nacionalidade());
        diretor.setUrlFoto(dto.urlFoto());
        return diretor;
    }

    public static DiretorResponseDTO toResponseDTO(Diretor diretor) {
        if (diretor == null) return null;
        return new DiretorResponseDTO(
            diretor.getId(),
            diretor.getNome(),
            diretor.getDataNascimento(),
            diretor.getNacionalidade(),
            diretor.getUrlFoto()
        );
    }
}