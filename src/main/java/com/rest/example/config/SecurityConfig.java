package com.rest.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth",
                                "/init",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers("GET", "/api/v1/patients").hasAuthority("Patient.Read")
                        .requestMatchers("GET", "/api/v1/patient/{id}").hasAuthority("Patient.Read")
                        .requestMatchers("POST", "/api/v1/add").hasAuthority("Patient.Write")
                        .requestMatchers("PUT", "/api/v1/update/{id}").hasAuthority("Patient.Write")
                        .requestMatchers("DELETE", "/api/v1/delete/{id}").hasAuthority("Patient.Delete")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(
                        jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(keycloakAuthConverter())
                ))
                .build();
    }


    private Converter<Jwt, ? extends AbstractAuthenticationToken> keycloakAuthConverter() {
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new AuthoritiesConverter());
        return converter;
    }
}
