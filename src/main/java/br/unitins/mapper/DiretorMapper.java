package br.unitins.mapper;

import br.unitins.dto.DiretorRequestDTO;
import br.unitins.dto.DiretorResponseDTO;
import br.unitins.model.Diretor;

public class DiretorMapper {
   
    public static Diretor toEntity(DiretorRequestDTO dto) {
        if (dto == null) return null;
        Diretor diretor = new Diretor();
        diretor.setNome(dto.nome());
        diretor.setEmail(dto.email());
        diretor.setTelefone(dto.telefone());
        diretor.setNacionalidade(dto.nacionalidade());
        diretor.setBiografia(dto.biografia());
        return diretor;
    }

    public static DiretorResponseDTO toResponseDTO(Diretor diretor) {
        if (diretor == null) return null;
        return new DiretorResponseDTO(
            diretor.getId(),
            diretor.getNome(),
            diretor.getEmail(),
            diretor.getTelefone(),
            diretor.getNacionalidade(),
            diretor.getBiografia(),
            diretor.getFilmes() != null ? diretor.getFilmes().stream()
                .map(f -> f.getNome())
                .toList() : null
        );
    }
}