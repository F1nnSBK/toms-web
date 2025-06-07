package com.toms.app.security.apikey;

import java.util.Collection;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Profile("prod")
public class ApiKeyAuthentication extends AbstractAuthenticationToken {
    
    private final String apiKey;
    private final Object principal;

    public ApiKeyAuthentication(String apiKey, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.apiKey = apiKey;
        this.principal = apiKey;
        setAuthenticated(true);
    }

    public ApiKeyAuthentication(String apiKey) {
        super(null);
        this.apiKey = apiKey;
        this.principal = apiKey;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getApiKey() {
        return apiKey;
    }

}
