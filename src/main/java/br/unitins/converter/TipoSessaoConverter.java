package br.unitins.converter;

import br.unitins.model.TipoSessao;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoSessaoConverter implements AttributeConverter<TipoSessao, Long> {

    @Override
    public Long convertToDatabaseColumn(TipoSessao tipo) {
        return tipo == null ? null : tipo.getId();
    }

    @Override
    public TipoSessao convertToEntityAttribute(Long id) {
        return TipoSessao.valueOf(id);
    }
}