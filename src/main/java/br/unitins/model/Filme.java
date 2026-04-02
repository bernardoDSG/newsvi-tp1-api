package br.unitins.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Filme extends DefaultEntity{
    private String nome;
    private String duracao;
    private String sinopse;
    private String idiomaOriginal;
    private Integer anoLancamento;
    private ClassificacaoIndicativa classificacaoIndicativa;
    @ManyToMany
    @JoinTable(name = "filme_genero", 
               joinColumns = @JoinColumn(name = "filme_id"), 
               inverseJoinColumns = @JoinColumn(name = "genero_id"))
    private List<Genero> generos;
    @ManyToMany
    @JoinTable(name = "filme_ator",
               joinColumns = @JoinColumn(name = "filme_id"),
               inverseJoinColumns = @JoinColumn(name = "ator_id"))
    private List<Ator> atores;

    public ClassificacaoIndicativa getClassificacaoIndicativa() {
        return classificacaoIndicativa;
    }

    public void setClassificacaoIndicativa(ClassificacaoIndicativa classificacaoIndicativa) {
        this.classificacaoIndicativa = classificacaoIndicativa;
    }

    public List<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(List<Genero> generos) {
        this.generos = generos;
    }

    public List<Ator> getAtores() {
        return atores;
    }

    public void setAtores(List<Ator> atores) {
        this.atores = atores;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getIdiomaOriginal() {
        return idiomaOriginal;
    }

    public void setIdiomaOriginal(String idiomaOriginal) {
        this.idiomaOriginal = idiomaOriginal;
    }

    public Integer getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(Integer anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

}
