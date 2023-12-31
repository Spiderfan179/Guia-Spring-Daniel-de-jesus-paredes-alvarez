package com.tutorial.crud.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorial.crud.dto.Mensaje;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;






@Component
public class JwtEntryPoint implements AuthenticationEntryPoint
{
 private final static Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);
 
@Override
 public void commence(HttpServletRequest req,HttpServletResponse res, AuthenticationException e) throws IOException, ServletException
 {

    logger.error("error en el metodo commence: " + e.getMessage());
    //res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
     
    res.setHeader("Content-Type", "application/json");
    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    ObjectMapper objectMapper = new ObjectMapper();
    res.getWriter().write(objectMapper.writeValueAsString(new Mensaje("Credenciales incorrectas")));

   }
}
