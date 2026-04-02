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

    private final Long ID;
    private final String NOME;

    ClassificacaoIndicativa(Long id, String nome) {
        this.ID = id;
        this.NOME = nome;
    }

    public Long getID() {
        return ID;
    }

    public String getNOME() {
        return NOME;
    }

    public static ClassificacaoIndicativa valueOf(Long id) {
        if (id == null) {
            return null;
        }

        for (ClassificacaoIndicativa classificacao : ClassificacaoIndicativa.values()) {
            if (classificacao.getID().equals(id)) {
                return classificacao;
            }
        }
        return null;
    }
}
