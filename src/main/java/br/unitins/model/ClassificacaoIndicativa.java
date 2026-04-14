package br.unitins.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ClassificacaoIndicativa {
    LIVRE(1L, "Livre"),
    DEZ(2L, "10 anos"),
    DOZE(3L, "12 anos"),
    QUATORZE(4L, "14 anos"),
    DEZESSEIS(5L, "16 anos"),
    DEZOITO(6L, "18 anos");

    private final Long id;
    private final String nome;

    ClassificacaoIndicativa(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }

    public static ClassificacaoIndicativa valueOf(Long id) {
        for (ClassificacaoIndicativa c : values()) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }
}