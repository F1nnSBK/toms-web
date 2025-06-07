package com.toms.app.security.apikey;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Profile("prod")
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final String HEADER_NAME = "X-API-KEY";
    private final AuthenticationManager authenticationManager;
    private Set<String> protectedApiPaths = new HashSet<>();
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    public ApiKeyAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setProtectedApiPaths(Set<String> protectedApiPaths) {
        this.protectedApiPaths = protectedApiPaths;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        boolean isProtectedApiPath = false;
        for (String protectedPath : protectedApiPaths) {
            if (pathMatcher.match(protectedPath, requestUri)) {
                isProtectedApiPath = true;
                break;
            }
        }

        if (isProtectedApiPath) {
            String apiKey = request.getHeader(HEADER_NAME);

            if (apiKey == null || apiKey.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: API Key missing or empty");
                return;
            }

            try {
                ApiKeyAuthentication authRequest = new ApiKeyAuthentication(apiKey);
                Authentication authentication = authenticationManager.authenticate(authRequest);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Invalid API Key");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}