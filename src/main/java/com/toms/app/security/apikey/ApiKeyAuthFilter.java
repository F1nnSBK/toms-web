package com.toms.app.security.apikey;

import java.io.IOException;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Profile("prod")
public class ApiKeyAuthFilter extends AbstractAuthenticationProcessingFilter {
    
    private final String HEADER_NAME = "X-API-KEY";

    public ApiKeyAuthFilter() {
        super(new AntPathRequestMatcher("/api/v1/**"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {
            String apiKey = request.getHeader(HEADER_NAME);

            if(apiKey == null || apiKey.isEmpty()) {
                return null;
            }

            ApiKeyAuthentication authRequest = new ApiKeyAuthentication(apiKey);
            return this.getAuthenticationManager().authenticate(authRequest);
        }

        @Override
        protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
            SecurityContextHolder.getContext().setAuthentication(authResult);
            chain.doFilter(request, response);
        }

        @Override
        protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Unauthorized: Invalid API Key");

        }
}
