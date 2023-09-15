package br.luahr.topicos1.converterjpa;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import br.luahr.topicos1.model.TipoFlor;

@Converter(autoApply = true)
public class TipoFlorConverter implements AttributeConverter<TipoFlor, Integer>{

    @Override
    public Integer convertToDatabaseColumn(TipoFlor tipoFlor) {
        return tipoFlor == null ? null : tipoFlor.getId();
    }

    @Override
    public TipoFlor convertToEntityAttribute(Integer id) {
        return TipoFlor.valueOf(id);
    }
    
}
