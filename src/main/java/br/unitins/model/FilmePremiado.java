package br.unitins.model;

import java.util.List;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

public class FilmePremiado extends Filme {
    @ManyToMany
    @JoinTable(name = "filme_premio", 
               joinColumns = @JoinColumn(name = "filme_id"), 
               inverseJoinColumns = @JoinColumn(name = "premio_id"))
    private List<Premio> premiacao;

    public List<Premio> getPremiacao() {
        return premiacao;
    }

    public void setPremiacao(List<Premio> premiacao) {
        this.premiacao = premiacao;
    }
    
}
