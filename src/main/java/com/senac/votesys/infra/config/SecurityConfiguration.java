package com.senac.votesys.infra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                // Rotas públicas (Swagger, Login, Cadastro)
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                                .requestMatchers("/auth/login", "/auth/recuperarsenha", "/auth/resetarsenha").permitAll()

                                //  Esta é a única rota de /usuarios pública
                                .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()

                                //  Protegendo as outras rotas /usuarios
                                // Apenas Admins podem LISTAR ou acessar por ID
                                .requestMatchers(HttpMethod.GET, "/usuarios", "/usuarios/**")
                                .hasAnyRole("ADMIN_GERAL", "ADMIN_NORMAL")

                                // Apenas Admins podem ATUALIZAR
                                .requestMatchers(HttpMethod.PUT, "/usuarios/**")
                                .hasAnyRole("ADMIN_GERAL", "ADMIN_NORMAL")

                                //  Qualquer outra rota exige autenticação
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}