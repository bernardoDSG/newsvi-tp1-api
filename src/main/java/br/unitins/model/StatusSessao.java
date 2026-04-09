package br.unitins.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StatusSessao {
    EM_BREVE(1L, "Em Breve"),
    EM_EXIBICAO(2L, "Em Exibição"),
    ENCERRADO(3L, "Encerrado"),
    CANCELADA(4L, "Cancelada");

    private final Long id;
    private final String nome;

    StatusSessao(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public static StatusSessao valueOf(Long id) {
        if (id == null) {
            return null;
        }
        for (StatusSessao status : StatusSessao.values()) {
            if (status.getId().equals(id)) {
                return status;
            }
        }
        return null;
    }
}