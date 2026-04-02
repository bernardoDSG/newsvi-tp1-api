package br.unitins.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class AtorPremiado extends Ator {
    @ManyToMany
    @JoinTable(name = "ator_premio", 
               joinColumns = @JoinColumn(name = "ator_id"), 
               inverseJoinColumns = @JoinColumn(name = "premio_id"))
    private List<Premio> premiacao;

    public List<Premio> getPremiacao() {
        return premiacao;
    }

    public void setPremiacao(List<Premio> premiacao) {
        this.premiacao = premiacao;
    }

}
