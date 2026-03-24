package br.unitins.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

public class Sala extends DefaultEntity {
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sala_id")
    List<Poltrona> poltronas;
}
