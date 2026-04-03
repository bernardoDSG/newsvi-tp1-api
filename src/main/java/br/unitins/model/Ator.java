package br.unitins.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;


@Entity
public class Ator extends DefaultEntity{
    private String nome;
    @OneToMany
    @JoinTable(
        name = "ator_premio",
        joinColumns = @JoinColumn(name = "ator_id"),
        inverseJoinColumns = @JoinColumn(name = "premio_id")
    )
    private List<Premio> premios = new ArrayList<>();


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Premio> getPremios() {
        return premios;
    }

    public void setPremios(List<Premio> premios) {
        this.premios = premios;
    }

}
