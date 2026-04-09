package br.unitins.converter;

import br.unitins.model.StatusSessao;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusSessaoConverter implements AttributeConverter<StatusSessao, Long> {

    @Override
    public Long convertToDatabaseColumn(StatusSessao status) {
        if (status == null) {
            return null;
        }
        return status.getId();
    }

    @Override
    public StatusSessao convertToEntityAttribute(Long id) {
        if (id == null) {
            return null;
        }
        return StatusSessao.valueOf(id);
    }
}