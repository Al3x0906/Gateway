package org.project.gateway.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.project.gateway.txn.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@Component
public class JWTFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    @Autowired
    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException,
            ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        try {
            // Allow login endpoint to pass without token
            if ("/login".equals(requestURI)) {
                log.debug("Allowing login endpoint without token.");
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            String token = resolveToken(request);

            if (Strings.isBlank(token)) {
                log.debug("Token is blank, unauthorized request.");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Unauthorized: Token is missing.");
                return;
            }

            if (!verifyToken(token)) {
                log.debug("Token verification failed.");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Unauthorized: Invalid token.");
                return;
            }

            log.debug("Token verified successfully.");

            // Proceed with the next filter in the chain
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            log.error("Error during JWT filter processing: ", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("Internal server error.");
        }
    }

    private boolean verifyToken(String token) {
        try {
            return tokenProvider.validateToken(token);
        } catch (Exception e) {
            log.error("Token verification failed: ", e);
            return false;
        }
    }


    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
