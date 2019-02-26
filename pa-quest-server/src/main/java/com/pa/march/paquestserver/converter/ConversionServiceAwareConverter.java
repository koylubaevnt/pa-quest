package com.pa.march.paquestserver.converter;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public abstract class ConversionServiceAwareConverter<S, T> implements Converter<S, T> {

    @Inject
    private ConversionService conversionService;

    protected  ConversionService conversionService() {
        return conversionService;
    }

    @PostConstruct
    private void register() {
        if (conversionService instanceof ConverterRegistry) {
            ((ConverterRegistry) conversionService).addConverter(this);
        } else {
            throw new IllegalArgumentException("Не возможно зарегистрировать Converter в ConverterRegistry");
        }
    }

}
