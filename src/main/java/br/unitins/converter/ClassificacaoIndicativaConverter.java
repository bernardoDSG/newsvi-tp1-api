package br.unitins.converter;

import br.unitins.model.ClassificacaoIndicativa;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ClassificacaoIndicativaConverter implements AttributeConverter<ClassificacaoIndicativa, Long> {

    @Override
    public Long convertToDatabaseColumn(ClassificacaoIndicativa classificacao) {
        return classificacao == null ? null : classificacao.getId();
    }

    @Override
    public ClassificacaoIndicativa convertToEntityAttribute(Long id) {
        return ClassificacaoIndicativa.valueOf(id);
    }
}