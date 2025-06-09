package com.toms.app.security;

import java.util.HashSet;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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

        ApiKeyAuthFilter apiKeyAuthFilter = new ApiKeyAuthFilter(apiKeyAuthManager);

        Set<String> protectedApiPaths = new HashSet<>();
        protectedApiPaths.add("/api/v1/items/**");
        protectedApiPaths.add("/api/v1/users/**");
        apiKeyAuthFilter.setProtectedApiPaths(protectedApiPaths);


        http
                .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/api/v1/**")
                )
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                    // PUBLIC ASSETS (most specific)
                    .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico").permitAll()

                    // PUBLIC API ENDPOINTS (specific to your new data API) - these will bypass ApiKeyAuthFilter
                    .requestMatchers("/api/v1/public/**").permitAll()

                    // PUBLIC WEB PAGES (your main website pages)
                    .requestMatchers("/", "/produkte", "/kontakt", "/ueber-mich", "/produkte/**").permitAll()
                    .requestMatchers("/login", "/logout").permitAll()

                    // API USERS (these are the ones that are *protected* by ApiKeyAuthFilter)
                    .requestMatchers("/api/v1/items/**").hasRole("API_USER")
                    .requestMatchers("/api/v1/users/**").hasRole("API_USER")

                    // ADMIN PAGES (most restrictive, specific paths)
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
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}