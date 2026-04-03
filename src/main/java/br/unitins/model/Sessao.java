package br.unitins.model;

import java.time.LocalDateTime;
import java.util.List;

import br.unitins.converter.TipoSessaoConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Sessao extends DefaultEntity {
   LocalDateTime inicio;
   LocalDateTime fim;
   @ManyToOne
   Filme filme;
   @Convert(converter = TipoSessaoConverter.class)
   TipoSessao tipo;
   @ManyToMany
   @JoinTable(name = "sessao_sala", 
               joinColumns = @JoinColumn(name = "sessao_id"), 
               inverseJoinColumns = @JoinColumn(name = "sala_id"))
   List<Sala> salas;
   
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
