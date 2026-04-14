package br.unitins.converter;

import br.unitins.model.TipoSessao;
import jakarta.persistence.AttributeConverter;

public class TipoSessaoConverter implements AttributeConverter<TipoSessao, Long> {

    @Override
    public Long convertToDatabaseColumn(TipoSessao tipo) {
        if (tipo == null) {
            return null;
        }
        return tipo.getId();
    }

    @Override
    public TipoSessao convertToEntityAttribute(Long id) {
        if (id == null) {
            return null;
        }
        return TipoSessao.valueOf(id);
    }
}