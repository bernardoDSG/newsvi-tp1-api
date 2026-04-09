package br.unitins.model;

import java.time.LocalDateTime;
import java.util.List;

import br.unitins.converter.StatusSessaoConverter;
import br.unitins.converter.TipoSessaoConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Sessao extends DefaultEntity {
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private Double preco;
    private Integer capacidadeTotal;
    private Integer capacidadeDisponivel;
    
    @Convert(converter = StatusSessaoConverter.class)
    private StatusSessao status;  // Agora é enum!
    
    @ManyToOne
    private Filme filme;
    
    @Convert(converter = TipoSessaoConverter.class)
    private TipoSessao tipo;
    
    @ManyToMany
    @JoinTable(name = "sessao_sala", 
               joinColumns = @JoinColumn(name = "sessao_id"), 
               inverseJoinColumns = @JoinColumn(name = "sala_id"))
    private List<Sala> salas;

    // Getters e Setters
    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getCapacidadeTotal() {
        return capacidadeTotal;
    }

    public void setCapacidadeTotal(Integer capacidadeTotal) {
        this.capacidadeTotal = capacidadeTotal;
    }

    public Integer getCapacidadeDisponivel() {
        return capacidadeDisponivel;
    }

    public void setCapacidadeDisponivel(Integer capacidadeDisponivel) {
        this.capacidadeDisponivel = capacidadeDisponivel;
    }

    public StatusSessao getStatus() {
        return status;
    }

    public void setStatus(StatusSessao status) {
        this.status = status;
    }

    public Filme getFilme() {
        return filme;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
    }

    public TipoSessao getTipo() {
        return tipo;
    }

    public void setTipo(TipoSessao tipo) {
        this.tipo = tipo;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public void setSalas(List<Sala> salas) {
        this.salas = salas;
    }
}