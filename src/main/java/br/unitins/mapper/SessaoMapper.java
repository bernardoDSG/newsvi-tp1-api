package br.unitins.mapper;

import br.unitins.dto.SessaoRequestDTO;
import br.unitins.dto.SessaoResponseDTO;
import br.unitins.model.Sessao;

public class SessaoMapper {
   
    public static Sessao toEntity(SessaoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Sessao sessao = new Sessao();
        sessao.setInicio(dto.inicio());
        sessao.setFim(dto.fim());
        return sessao;
    }

    public static SessaoResponseDTO toResponseDTO(Sessao sessao) {
        if (sessao == null) {
            return null;
        }
        return new SessaoResponseDTO(
            sessao.getId(),
            sessao.getInicio(),
            sessao.getFim(),
            sessao.getFilme() != null ? sessao.getFilme().getNome() : null,
            sessao.getTipo() != null ? sessao.getTipo().getNOME() : null,
            sessao.getSalas() != null ? sessao.getSalas().stream().map(s -> s.getId()).toList() : null
        );
    }
}