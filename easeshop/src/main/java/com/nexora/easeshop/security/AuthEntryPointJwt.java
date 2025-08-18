package com.nexora.easeshop.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);


        var body = new HashMap<String, Object>();
        body.put("timestamp", new Date());
        body.put("status", 401);
        body.put("error", "Unauthorized");
        if (authException instanceof BadCredentialsException) {
            body.put("message", "Identifiants incorrects. Veuillez vérifier vos identifiants.");
        } else {
            body.put("message", authException.getMessage());
        }
        body.put("path", request.getServletPath());

        var mapper = new ObjectMapper();
        // Write the response body as JSON string and set it in the response body output stream
        mapper.writeValue(response.getOutputStream(), body);
    }
}
