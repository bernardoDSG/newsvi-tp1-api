package br.unitins.converter;

import br.unitins.model.Disponibilidade;
import jakarta.persistence.AttributeConverter;

public class DisponibilidadeConverter implements AttributeConverter<Disponibilidade, Long> {

    @Override
    public Long convertToDatabaseColumn(Disponibilidade disponibilidade) {
        if (disponibilidade == null) {
            return null;
        }
        return disponibilidade.getID();
    }

    @Override
    public Disponibilidade convertToEntityAttribute(Long id) {
        if (id == null) {
            return null;
        }
        return Disponibilidade.valueOf(id);
    }
    
}
