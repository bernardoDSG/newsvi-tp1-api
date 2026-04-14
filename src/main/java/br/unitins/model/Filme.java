package br.unitins.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Filme extends DefaultEntity {
    private String nome;
    private String duracao;
    private Integer duracaoMinutos;
    private String sinopse;
    private String idiomaOriginal;
    private Integer anoLancamento;
    private String imagemPoster;
    private String trailerUrl;

    @ManyToOne
    @JoinColumn(name = "diretor_id")
    private Diretor diretor;

    @ManyToOne
    @JoinColumn(name = "classificacao_id")
    private ClassificacaoIndicativa classificacaoIndicativa;

    @ManyToMany
    @JoinTable(name = "filme_genero", 
               joinColumns = @JoinColumn(name = "filme_id"), 
               inverseJoinColumns = @JoinColumn(name = "genero_id"))
    private List<Genero> generos = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "filme_ator",
               joinColumns = @JoinColumn(name = "filme_id"),
               inverseJoinColumns = @JoinColumn(name = "ator_id"))
    private List<Ator> atores = new ArrayList<>();

    @OneToMany
    @JoinTable(name = "filme_premio",
               joinColumns = @JoinColumn(name = "filme_id"),
               inverseJoinColumns = @JoinColumn(name = "premio_id"))
    private List<Premio> premios = new ArrayList<>();

    // Método de conversão de duração
    public static Integer converterDuracaoParaMinutos(String duracao) {
        if (duracao == null || duracao.trim().isEmpty()) {
            return null;
        }
        String duracaoLimpa = duracao.toLowerCase().trim().replaceAll("\\s+", "");
        int horas = 0;
        int minutos = 0;
        
        try {
            if (duracaoLimpa.contains("h") && duracaoLimpa.contains("m")) {
                String[] partes = duracaoLimpa.split("h");
                horas = Integer.parseInt(partes[0]);
                String parteMinutos = partes[1].replace("m", "");
                minutos = Integer.parseInt(parteMinutos);
            } else if (duracaoLimpa.contains("h")) {
                horas = Integer.parseInt(duracaoLimpa.replace("h", ""));
            } else if (duracaoLimpa.contains("m")) {
                minutos = Integer.parseInt(duracaoLimpa.replace("m", ""));
            }
            return horas * 60 + minutos;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDuracao() { return duracao; }
    public void setDuracao(String duracao) {
        this.duracao = duracao;
        this.duracaoMinutos = converterDuracaoParaMinutos(duracao);
    }

    public Integer getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(Integer duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }

    public String getSinopse() { return sinopse; }
    public void setSinopse(String sinopse) { this.sinopse = sinopse; }

    public String getIdiomaOriginal() { return idiomaOriginal; }
    public void setIdiomaOriginal(String idiomaOriginal) { this.idiomaOriginal = idiomaOriginal; }

    public Integer getAnoLancamento() { return anoLancamento; }
    public void setAnoLancamento(Integer anoLancamento) { this.anoLancamento = anoLancamento; }

    public String getImagemPoster() { return imagemPoster; }
    public void setImagemPoster(String imagemPoster) { this.imagemPoster = imagemPoster; }

    public String getTrailerUrl() { return trailerUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }

    public Diretor getDiretor() { return diretor; }
    public void setDiretor(Diretor diretor) { this.diretor = diretor; }

    public ClassificacaoIndicativa getClassificacaoIndicativa() { return classificacaoIndicativa; }
    public void setClassificacaoIndicativa(ClassificacaoIndicativa classificacaoIndicativa) { 
        this.classificacaoIndicativa = classificacaoIndicativa; 
    }

    public List<Genero> getGeneros() { return generos; }
    public void setGeneros(List<Genero> generos) { this.generos = generos; }

    public List<Ator> getAtores() { return atores; }
    public void setAtores(List<Ator> atores) { this.atores = atores; }

    public List<Premio> getPremios() { return premios; }
    public void setPremios(List<Premio> premios) { this.premios = premios; }
}