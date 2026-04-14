package br.unitins.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Poltrona extends DefaultEntity {
    private String codigo;
    private String linha;
    private Integer coluna;

    @ManyToOne
    @JoinColumn(name = "sala_id")
    private Sala sala;

    @ManyToOne
    @JoinColumn(name = "disponibilidade_id")
    private Disponibilidade disponibilidade;

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getLinha() { return linha; }
    public void setLinha(String linha) { this.linha = linha; }

    public Integer getColuna() { return coluna; }
    public void setColuna(Integer coluna) { this.coluna = coluna; }

    public Sala getSala() { return sala; }
    public void setSala(Sala sala) { this.sala = sala; }

    public Disponibilidade getDisponibilidade() { return disponibilidade; }
    public void setDisponibilidade(Disponibilidade disponibilidade) { this.disponibilidade = disponibilidade; }
}