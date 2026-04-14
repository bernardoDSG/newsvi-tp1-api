package br.unitins.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Disponibilidade {
    DISPONIVEL(1L, "Disponível"),
    INDISPONIVEL(2L, "Indisponível");

    private final Long id;
    private final String nome;

    Disponibilidade(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }

    public static Disponibilidade valueOf(Long id) {
        for (Disponibilidade d : values()) {
            if (d.getId().equals(id)) return d;
        }
        return null;
    }
}