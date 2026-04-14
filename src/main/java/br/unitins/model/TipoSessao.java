package br.unitins.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TipoSessao {
    DUBLADA(1L, "Dublada"),
    LEGENDADA(2L, "Legendada");

    private final Long id;
    private final String nome;

    TipoSessao(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }

    public static TipoSessao valueOf(Long id) {
        for (TipoSessao t : values()) {
            if (t.getId().equals(id)) return t;
        }
        return null;
    }
}