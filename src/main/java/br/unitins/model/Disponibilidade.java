package br.unitins.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Disponibilidade {
    DISPONIVEL(1L, "Disponível"),
    INDISPONIVEL(2L, "Indisponível");

    private final Long ID;
    private final String NOME;

    Disponibilidade(Long id, String nome) {
        this.ID = id;
        this.NOME = nome;
    }

    public Long getID() {
        return ID;
    }

    public String getNOME() {
        return NOME;
    }

    public static Disponibilidade valueOf(Long id) {
        if (id == null) {
            return null;
        }

        for (Disponibilidade disponibilidade : Disponibilidade.values()) {
            if (disponibilidade.getID().equals(id)) {
                return disponibilidade;
            }
        }
        return null;
    }
}
