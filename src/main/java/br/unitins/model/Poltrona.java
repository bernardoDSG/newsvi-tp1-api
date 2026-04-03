package br.unitins.model;

import br.unitins.converter.DisponibilidadeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;

@Entity
public class Poltrona extends DefaultEntity {
    private String codigo;
    @Convert(converter = DisponibilidadeConverter.class)
    private Disponibilidade disponibilidade;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Disponibilidade getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(Disponibilidade disponibilidade) {
        this.disponibilidade = disponibilidade;
    }
}
