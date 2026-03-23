package br.unitins.model;

public enum TipoSessao {
    DUBLADA(1L,"Dublada"),
    LEGENDADA(2L,"Legendada");
    
    private final Long ID;
    private final String NOME;

    TipoSessao(Long id, String nome) {
        this.ID = id;
        this.NOME = nome;
    }

    public Long getID() {
        return ID;
    }

    public String getNOME() {
        return NOME;
    }

    public static TipoSessao valueOf(Long id) {
        if (id == null) {
            return null;
        }

        for (TipoSessao tipoSessao : TipoSessao.values()) {
            if (tipoSessao.getID().equals(id)) {
                return tipoSessao;
            }
        }
        return null;
    }
}
