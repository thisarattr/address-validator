package org.thisarattr.auspost.addressvalidator.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.GenericFilterBean;
import org.thisarattr.auspost.addressvalidator.models.User;
import org.thisarattr.auspost.addressvalidator.services.CacheService;

public class JwtTokenFilter extends GenericFilterBean {

    private static final String HEADER_AUTHORIZATION = "authorization";
    private static final String HTTP_OPTIONS = "OPTIONS";
    private static final String AUTH_PREFIX = "Bearer ";
    private static final String ATTRIBUTE_CLAIMS = "claims";
    private CacheService cacheService;
    private String secretKey;

    public JwtTokenFilter(CacheService cacheService, String secretKey) {
        this.cacheService = cacheService;
        this.secretKey = secretKey;
    }

    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        final String authHeader = request.getHeader(HEADER_AUTHORIZATION);

        if (HTTP_OPTIONS.equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);

            chain.doFilter(req, res);
        } else {

            if (authHeader == null || !authHeader.startsWith(AUTH_PREFIX)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
                return;
            }

            final String token = authHeader.substring(7);

            try {
                final Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
                Optional<User> user = cacheService.get(token);
                if (!user.isPresent()) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                    return;
                }
                request.setAttribute(ATTRIBUTE_CLAIMS, claims);
            } catch (SignatureException | MalformedJwtException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }

            chain.doFilter(req, res);
        }
    }
}
