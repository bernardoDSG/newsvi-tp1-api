package br.unitins.converter;

import br.unitins.model.Disponibilidade;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DisponibilidadeConverter implements AttributeConverter<Disponibilidade, Long> {

    @Override
    public Long convertToDatabaseColumn(Disponibilidade disponibilidade) {
        return disponibilidade == null ? null : disponibilidade.getId();
    }

    @Override
    public Disponibilidade convertToEntityAttribute(Long id) {
        return Disponibilidade.valueOf(id);
    }
}