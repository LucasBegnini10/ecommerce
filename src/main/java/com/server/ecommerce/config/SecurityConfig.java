package com.server.ecommerce.config;

import com.server.ecommerce.filter.TokenValidatorFilter;
import com.server.ecommerce.user.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final TokenValidatorFilter tokenValidatorFilter;

    @Autowired
    public SecurityConfig(TokenValidatorFilter tokenValidatorFilter){
    this.tokenValidatorFilter = tokenValidatorFilter;
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
                    config.setAllowedOrigins(List.of("https://ecommerce-web-ten-topaz.vercel.app", "http://ecommerce-web-ten-topaz.vercel.app", "https://ecommerce-web-ten-topaz.vercel.app/", "http://ecommerce-web-ten-topaz.vercel.app/"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
                    config.setAllowCredentials(true);
                    config.setExposedHeaders(List.of("Authorization", "token"));
                    return config;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests)-> requests
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/users").hasRole(RoleType.ADMIN.getRole())
                        .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasRole(RoleType.ADMIN.getRole())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole(RoleType.ADMIN.getRole())
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/products/**").hasRole(RoleType.ADMIN.getRole())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole(RoleType.ADMIN.getRole())
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                        .requestMatchers("api/v1/recovery-password/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(tokenValidatorFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
