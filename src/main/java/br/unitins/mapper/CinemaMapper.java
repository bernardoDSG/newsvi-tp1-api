package br.unitins.mapper;

import br.unitins.dto.CinemaRequestDTO;
import br.unitins.dto.CinemaResponseDTO;
import br.unitins.model.Cinema;

public class CinemaMapper {
   
    public static Cinema toEntity(CinemaRequestDTO dto) {
        if (dto == null) return null;
        Cinema cinema = new Cinema();
        cinema.setNome(dto.nome());
        cinema.setCnpj(dto.cnpj());
        cinema.setTelefone(dto.telefone());
        if (dto.endereco() != null) {
            cinema.setEndereco(EnderecoMapper.toEntity(dto.endereco()));
        }
        return cinema;
    }

    public static CinemaResponseDTO toResponseDTO(Cinema cinema) {
        if (cinema == null) return null;
        return new CinemaResponseDTO(
            cinema.getId(),
            cinema.getNome(),
            cinema.getCnpj(),
            cinema.getTelefone(),
            cinema.getEndereco() != null ? EnderecoMapper.toResponseDTO(cinema.getEndereco()) : null,
            cinema.getSalas() != null ? cinema.getSalas().stream()
                .map(s -> String.valueOf(s.getNumero()))
                .toList() : null
        );
    }
}