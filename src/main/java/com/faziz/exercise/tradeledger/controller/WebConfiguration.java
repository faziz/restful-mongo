package com.faziz.exercise.tradeledger.controller;

import com.faziz.exercise.tradeledger.controller.filter.Filter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import static java.net.URLDecoder.decode;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer{

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, Filter.class, this::toFilter);
    }

    private Filter toFilter(String filter) {
        try {
            return mapper.readValue(decode(filter, "UTF-8"), Filter.class);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Incorrect filter.");
        }
    }
}
