package com.example.spring_security_jwt.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.spring_security_jwt.security.jwt.AuthEntryPointJwt;

import com.example.spring_security_jwt.security.jwt.AuthTokenFilter;
import com.example.spring_security_jwt.security.services.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthTokenFilter authTokenFilter;
    private final AuthEntryPointJwt authEntryPointJwt;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

   @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider(userDetailsService);

        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration)
            throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                         .authenticationEntryPoint(authEntryPointJwt))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/tutorials/**")
                        .permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/tags/**")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/tutorials/**",
                                "/api/tags/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/tutorials/**",
                                "/api/tags/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/tutorials/**",
                                "/api/tags/**")
                        .hasRole("ADMIN")

                        .anyRequest()
                        .authenticated())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(
                        authTokenFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
