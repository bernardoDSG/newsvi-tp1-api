package br.unitins.model;

import java.util.List;

public class AtorPremiado extends Ator {
    private List<Premio> premios;

    public List<Premio> getPremios() {
        return premios;
    }

    public void setPremios(List<Premio> premios) {
        this.premios = premios;
    }
    
}
