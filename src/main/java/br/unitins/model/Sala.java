package br.unitins.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Sala extends DefaultEntity {
    private Integer capacidade;  // NOVO - capacidade total da sala
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sala_id")
    private List<Poltrona> poltronas;

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