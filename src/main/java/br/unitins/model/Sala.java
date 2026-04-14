package br.unitins.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Sala extends DefaultEntity {
    private Integer numero;
    private Integer capacidade;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sala")
    private List<Poltrona> poltronas = new ArrayList<>();

    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

    public Integer getCapacidade() { return capacidade; }
    public void setCapacidade(Integer capacidade) { this.capacidade = capacidade; }

    public Cinema getCinema() { return cinema; }
    public void setCinema(Cinema cinema) { this.cinema = cinema; }

    public List<Poltrona> getPoltronas() { return poltronas; }
    public void setPoltronas(List<Poltrona> poltronas) { this.poltronas = poltronas; }
}