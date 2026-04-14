package br.unitins.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;

@Entity
public class Ator extends Pessoa {
    private LocalDate dataNascimento;

    @OneToMany
    @JoinTable(
        name = "ator_premio",
        joinColumns = @JoinColumn(name = "ator_id"),
        inverseJoinColumns = @JoinColumn(name = "premio_id")
    )
    private List<Premio> premios = new ArrayList<>();

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public List<Premio> getPremios() {
        return premios;
    }

    public void setPremios(List<Premio> premios) {
        this.premios = premios;
    }
}