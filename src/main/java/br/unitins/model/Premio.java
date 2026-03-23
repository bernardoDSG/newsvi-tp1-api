package br.unitins.model;

public enum Premio {
    OSCAR(1L, "Oscar"),
    PALMADEOURO(2L, "Palma de Ouro(Festival de Cannes)"),
    LEAODEOURO(3L, "Leão de Ouro(Festival de Veneza)"),
    URSODEOURO(4L, "Urso de Ouro(Festival de Berlim)"),
    BAFTA(5L, "British Academy Film Awards"),
    GLOBODEOURO(6L, "Globo de Ouro");

    private final Long ID;
    private final String NOME;

    Premio(Long id, String nome) {
        this.ID = id;
        this.NOME = nome;
    }

    public Long getID() {
        return ID;
    }

    public String getNOME() {
        return NOME;
    }

    public static Premio valueOf(Long id) {
        if (id == null) {
            return null;
        }

        for (Premio premio : Premio.values()) {
            if (premio.getID().equals(id)) {
                return premio;
            }
        }
        return null;
    }

}
