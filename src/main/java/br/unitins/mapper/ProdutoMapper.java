package br.unitins.mapper;

import br.unitins.dto.ProdutoResponseDTO;
import br.unitins.model.Sessao;

public class ProdutoMapper {

    public static ProdutoResponseDTO toResponseDTO(Sessao sessao) {
        if (sessao == null) {
            return null;
        }
        return new ProdutoResponseDTO(
            sessao.getId(),
            sessao.getFilme() != null ? sessao.getFilme().getNome() : null,
            sessao.getFilme() != null ? sessao.getFilme().getSinopse() : null,
            sessao.getCinema() != null ? sessao.getCinema().getNome() : null,
            sessao.getInicio(),
            sessao.getFim(),
            sessao.getCapacidadeDisponivel(),
            sessao.getTipo() != null ? sessao.getTipo().getNome() : null,
            sessao.getStatus() != null ? sessao.getStatus().getNome() : null
        );
    }
}

