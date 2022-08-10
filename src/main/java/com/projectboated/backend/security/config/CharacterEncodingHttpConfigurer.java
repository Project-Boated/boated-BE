package com.projectboated.backend.security.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CharacterEncodingFilter;

@Component
public class CharacterEncodingHttpConfigurer extends AbstractHttpConfigurer<CharacterEncodingHttpConfigurer, HttpSecurity> {

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        builder.addFilterBefore(characterEncodingFilter(), CsrfFilter.class);
    }

    public CharacterEncodingFilter characterEncodingFilter() {
        return new CharacterEncodingFilter("UTF-8", true);
    }
}
