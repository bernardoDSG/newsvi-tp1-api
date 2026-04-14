package br.unitins.mapper;

import br.unitins.dto.SessaoRequestDTO;
import br.unitins.dto.SessaoResponseDTO;
import br.unitins.model.Sessao;
import br.unitins.model.StatusSessao;
import br.unitins.model.TipoSessao;

public class SessaoMapper {
   
    public static Sessao toEntity(SessaoRequestDTO dto) {
        if (dto == null) return null;
        Sessao sessao = new Sessao();
        sessao.setInicio(dto.inicio());
        sessao.setFim(dto.fim());
        sessao.setPreco(dto.preco());
        sessao.setCapacidadeTotal(dto.capacidadeTotal());
        sessao.setCapacidadeDisponivel(dto.capacidadeDisponivel());
        
        if (dto.statusId() != null) {
            sessao.setStatus(StatusSessao.valueOf(dto.statusId()));
        }
        if (dto.tipoSessaoId() != null) {
            sessao.setTipo(TipoSessao.valueOf(dto.tipoSessaoId()));
        }
        return sessao;
    }

    public static SessaoResponseDTO toResponseDTO(Sessao sessao) {
        if (sessao == null) return null;
        return new SessaoResponseDTO(
            sessao.getId(),
            sessao.getInicio(),
            sessao.getFim(),
            sessao.getPreco(),
            sessao.getCapacidadeTotal(),
            sessao.getCapacidadeDisponivel(),
            sessao.getStatus() != null ? sessao.getStatus().name() : null,
            sessao.getStatus() != null ? sessao.getStatus().getNome() : null,
            sessao.getTipo() != null ? sessao.getTipo().getNome() : null,
            sessao.getFilme() != null ? sessao.getFilme().getNome() : null,
            sessao.getCinema() != null ? sessao.getCinema().getNome() : null,
            sessao.getSalas() != null ? sessao.getSalas().stream().map(s -> s.getId()).toList() : null
        );
    }
}