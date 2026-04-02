package br.unitins.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Sala extends DefaultEntity {
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sala_id")
    List<Poltrona> poltronas;

    public List<Poltrona> getPoltronas() {
        return poltronas;
    }

    public void setPoltronas(List<Poltrona> poltronas) {
        this.poltronas = poltronas;
    }
}
