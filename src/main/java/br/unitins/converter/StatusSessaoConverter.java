package br.unitins.converter;

import br.unitins.model.StatusSessao;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusSessaoConverter implements AttributeConverter<StatusSessao, Long> {

    @Override
    public Long convertToDatabaseColumn(StatusSessao status) {
        return status == null ? null : status.getId();
    }

    @Override
    public StatusSessao convertToEntityAttribute(Long id) {
        return StatusSessao.valueOf(id);
    }
}