package br.unitins.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cinema extends DefaultEntity {
    private String nome;
    private String cnpj;
    private String telefone;

    @OneToOne(mappedBy = "cinema")
    private Endereco endereco;

    @OneToMany(mappedBy = "cinema")
    private List<Sala> salas = new ArrayList<>();

    @OneToMany(mappedBy = "cinema")
    private List<Sessao> sessoes = new ArrayList<>();

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }

    public List<Sala> getSalas() { return salas; }
    public void setSalas(List<Sala> salas) { this.salas = salas; }

    public List<Sessao> getSessoes() { return sessoes; }
    public void setSessoes(List<Sessao> sessoes) { this.sessoes = sessoes; }
}