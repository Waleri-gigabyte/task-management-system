package taskmanagement.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain (
            HttpSecurity httpSecurity
    ) throws Exception {
        return httpSecurity
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // other matchers
                        .requestMatchers("/api/accounts").permitAll()
                        .requestMatchers("/error").permitAll() // expose the /error endpoint
                        .requestMatchers("/actuator/shutdown").permitAll() // required for tests
                        .requestMatchers("/h2-console/**").permitAll() // expose H2 console
                        .requestMatchers("/api/tasks").authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(Customizer.withDefaults())
                )
                .csrf(AbstractHttpConfigurer::disable) // allow modifying requests from tests
                .sessionManagement(sessions ->
                        sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no session
                )
                .build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain tokenSecurityFilterChain (
            HttpSecurity http
    ) throws Exception {
        return http
                .securityMatcher("/api/auth/token")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                .build();
    }
}
