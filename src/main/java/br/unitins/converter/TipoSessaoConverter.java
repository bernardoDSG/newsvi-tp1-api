package br.unitins.converter;

import br.unitins.model.TipoSessao;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoSessaoConverter implements AttributeConverter<TipoSessao, Long> {

    @Override
    public Long convertToDatabaseColumn(TipoSessao tipoSessao) {
        if (tipoSessao == null) {
            return null;
        }
        return tipoSessao.getID();
    }

    @Override
    public TipoSessao convertToEntityAttribute(Long id) {
        if (id == null) {
            return null;
        }
        return TipoSessao.valueOf(id);
    }
    
}
