package br.unitins.mapper;

import br.unitins.dto.SessaoRequestDTO;
import br.unitins.dto.SessaoResponseDTO;
import br.unitins.model.Sessao;
import br.unitins.model.TipoSessao;


public class SessaoMapper {
    

    public static Sessao toEntity(SessaoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Sessao sessao = new Sessao();
        sessao.setInicio(dto.inicio());
        sessao.setFim(dto.fim());
        sessao.setFilme(FilmeMapper.toEntity(dto.filmeRequestDTO()));
        sessao.setTipo(TipoSessao.valueOf(dto.idTipoSessao()));
        sessao.setSalas(dto.salaRequestDTOs().stream()
            .map(SalaMapper::toEntity)
            .toList());
        return sessao;
    }

    public static SessaoResponseDTO toResponseDTO(Sessao sessao) {
        if (sessao == null) {
            return null;
        }
        return new SessaoResponseDTO(sessao.getId(),
            FilmeMapper.toResponseDTO(sessao.getFilme()), sessao.getTipo(),
            sessao.getSalas().stream()
                .map(SalaMapper::toResponseDTO)
                .toList(),sessao.getInicio(), sessao.getFim());
    }

}
