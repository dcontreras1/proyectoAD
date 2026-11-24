package com.uao.bienestarencasa.reservas.config;

import java.time.LocalTime;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

/** Permite guardar y leer las horas de atención como texto "HH:mm". */
@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of(new TextoAHora(), new HoraATexto()));
    }

    @ReadingConverter
    static class TextoAHora implements Converter<String, LocalTime> {
        @Override
        public LocalTime convert(String texto) {
            return LocalTime.parse(texto);
        }
    }

    @WritingConverter
    static class HoraATexto implements Converter<LocalTime, String> {
        @Override
        public String convert(LocalTime hora) {
            return hora.toString();
        }
    }
}
