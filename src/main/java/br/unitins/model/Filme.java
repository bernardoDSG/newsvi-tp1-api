package br.unitins.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
public class Filme extends DefaultEntity {
    private String nome;
    private String duracao;  // formato "2h 30min" para exibição
    private Integer duracaoMinutos;  // para cálculos
    private String sinopse;
    private String idiomaOriginal;
    private Integer anoLancamento;
    private String imagemPoster;
    private String trailerUrl;
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
    
    @OneToMany
    @JoinTable(name = "filme_premio",
               joinColumns = @JoinColumn(name = "filme_id"),
               inverseJoinColumns = @JoinColumn(name = "premio_id"))
    private List<Premio> premios;

    // Método para converter duracao String para minutos
    public static Integer converterDuracaoParaMinutos(String duracao) {
        if (duracao == null || duracao.trim().isEmpty()) {
            return null;
        }
        
        int horas = 0;
        int minutos = 0;
        String duracaoLimpa = duracao.toLowerCase().trim();
        
        try {
            if (duracaoLimpa.contains("h")) {
                String parteHoras = duracaoLimpa.split("h")[0].trim();
                horas = Integer.parseInt(parteHoras);
                
                if (duracaoLimpa.contains("min")) {
                    String parteMinutos = duracaoLimpa.split("h")[1].replace("min", "").trim();
                    minutos = Integer.parseInt(parteMinutos);
                }
            } else if (duracaoLimpa.contains("min")) {
                minutos = Integer.parseInt(duracaoLimpa.replace("min", "").trim());
            }
        } catch (NumberFormatException e) {
            return null;
        }
        
        return horas * 60 + minutos;
    }
    
    // Método para converter minutos para formato de exibição
    public static String converterMinutosParaDuracao(Integer minutos) {
        if (minutos == null || minutos <= 0) {
            return null;
        }
        
        int horas = minutos / 60;
        int minutosRestantes = minutos % 60;
        
        if (horas > 0 && minutosRestantes > 0) {
            return horas + "h " + minutosRestantes + "min";
        } else if (horas > 0) {
            return horas + "h";
        } else {
            return minutosRestantes + "min";
        }
    }
    
    // Antes de salvar ou atualizar, converter duracao para minutos automaticamente
    @PrePersist
    @PreUpdate
    private void calcularDuracaoMinutos() {
        if (this.duracao != null && !this.duracao.trim().isEmpty()) {
            this.duracaoMinutos = converterDuracaoParaMinutos(this.duracao);
        }
    }

    // Getters e Setters
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
        // Converter automaticamente quando setar a duracao
        if (duracao != null && !duracao.trim().isEmpty()) {
            this.duracaoMinutos = converterDuracaoParaMinutos(duracao);
        }
    }

    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
        // Converter automaticamente quando setar os minutos
        if (duracaoMinutos != null && duracaoMinutos > 0) {
            this.duracao = converterMinutosParaDuracao(duracaoMinutos);
        }
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

    public String getImagemPoster() {
        return imagemPoster;
    }

    public void setImagemPoster(String imagemPoster) {
        this.imagemPoster = imagemPoster;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

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

    public List<Premio> getPremios() {
        return premios;
    }

    public void setPremios(List<Premio> premios) {
        this.premios = premios;
    }
}