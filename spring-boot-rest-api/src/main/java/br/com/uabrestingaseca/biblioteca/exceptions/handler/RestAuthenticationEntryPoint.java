package br.com.uabrestingaseca.biblioteca.exceptions.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        String authorization = httpServletRequest.getHeader("authorization");
        HashMap<String, Object> response = new LinkedHashMap<>();
        response.put("message", authorization == null? "Token de autorização ausente" : "Token de autorização ou inválido ou expirado!");
        if (authorization == null){
            response.put("errors", new String[]{"Um token de autenticação deve ser informado no cabeçalho da requisição"});
        }
        httpServletResponse.setStatus(401);//unauthorized
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, response);
        out.flush();
    }
}
