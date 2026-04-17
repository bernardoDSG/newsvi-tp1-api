package br.unitins.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Sala extends DefaultEntity {
    private Integer numero;
    private Integer capacidade;

    @OneToMany
    @JoinColumn(name = "sala_id")
    private List<Poltrona> poltronas = new ArrayList<>();

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public List<Poltrona> getPoltronas() {
        return poltronas;
    }

    public void setPoltronas(List<Poltrona> poltronas) {
        this.poltronas = poltronas;
    }
}