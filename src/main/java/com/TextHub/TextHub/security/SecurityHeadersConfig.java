package com.TextHub.TextHub.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

@Configuration
public class SecurityHeadersConfig {

    public static final String CSP_POLICY = String.join(" ",
            "default-src 'self';",
            "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com https://cdnjs.cloudflare.com;",
            "style-src-elem 'self' 'unsafe-inline' https://fonts.googleapis.com https://cdnjs.cloudflare.com;",
            "font-src 'self' https://fonts.gstatic.com https://cdnjs.cloudflare.com;",
            "img-src 'self' https:;",
            "script-src 'self' 'unsafe-inline' https://kit.fontawesome.com https://cdnjs.cloudflare.com https://cdn.jsdelivr.net;"
    );

    public static void configure(HeadersConfigurer<HttpSecurity> headers) throws Exception {
        headers
                .frameOptions(frame -> frame.deny())
                .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED))
                .contentSecurityPolicy(csp -> csp.policyDirectives(CSP_POLICY));
    }
}

