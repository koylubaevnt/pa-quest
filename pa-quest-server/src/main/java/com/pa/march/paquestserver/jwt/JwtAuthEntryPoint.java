package com.pa.march.paquestserver.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        if (e instanceof BadCredentialsException) {
            LOG.error("BadCredentials error. Message - {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Некорректные учетные данные!");
        } else {
            LOG.error("Unauthorized error. Message - {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Неавторизованный доступ");
        }
    }

}
