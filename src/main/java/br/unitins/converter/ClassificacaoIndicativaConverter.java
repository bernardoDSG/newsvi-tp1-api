package br.unitins.converter;

import br.unitins.model.ClassificacaoIndicativa;
import jakarta.persistence.AttributeConverter;

public class ClassificacaoIndicativaConverter implements AttributeConverter<ClassificacaoIndicativa, Long> {

    @Override
    public Long convertToDatabaseColumn(ClassificacaoIndicativa classificacao) {
        if (classificacao == null) {
            return null;
        }
        return classificacao.getID();
    }

    @Override
    public ClassificacaoIndicativa convertToEntityAttribute(Long id) {
        if (id == null) {
            return null;
        }
        return ClassificacaoIndicativa.valueOf(id);
    }
    
}
