package com.toms.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.toms.app.security.apikey.ApiKeyAuthFilter;
import com.toms.app.security.apikey.ApiKeyAuthManager;
import com.toms.app.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@Profile("prod")
public class SecurityConfig {

    private final ApiKeyAuthManager apiKeyAuthManager;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(ApiKeyAuthManager apiKeyAuthManager, CustomUserDetailsService customUserDetailsService) {
        this.apiKeyAuthManager = apiKeyAuthManager;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        ApiKeyAuthFilter apiKeyAuthFilter = new ApiKeyAuthFilter();
        apiKeyAuthFilter.setAuthenticationManager(apiKeyAuthManager);

        http
                // CSRF-Schutz: Deaktiviert für API-Pfade, Standard für alles andere (Formulare)
                .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/api/v1/**")
                )
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/", "/produkte", "/kontakt", "/ueber-mich", "/produkte/**").permitAll()
                    .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()

                    .requestMatchers("/api/v1/items/**").hasRole("API_USER")
                    .requestMatchers("/api/v1/users/**").hasRole("API_USER")

                    .requestMatchers("/admin/upload").hasRole("ADMIN")
                    .requestMatchers("/admin/**", "/admin").hasRole("ADMIN")

                    .anyRequest().authenticated()
                )
                .formLogin(form -> form
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/admin", true)
                )
                .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                )
                .csrf(Customizer.withDefaults());


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}