package com.sgurin.inetback.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgurin.inetback.response.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        String result = new ObjectMapper().writeValueAsString(GenericResponse.errorHttp("UNAUTHORIZED", HttpStatus.FORBIDDEN).getBody());
        httpServletResponse.getWriter().write(result);
        httpServletResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
