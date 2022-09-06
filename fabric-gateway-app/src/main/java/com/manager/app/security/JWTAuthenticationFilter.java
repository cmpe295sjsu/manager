package com.manager.app.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.app.model.ClientCredentials;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.manager.app.security.SecurityConstants.EXPIRATION_TIME;
import static com.manager.app.security.SecurityConstants.SECRET;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/users/signin");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            ClientCredentials creds = new ObjectMapper()
                    .readValue(req.getInputStream(), ClientCredentials.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.email,
                            creds.password,
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
        res.setContentType("application/json");
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("token", token);
        res.getWriter().write(jsonObj.toString());
        res.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req,
                                              HttpServletResponse res,
                                              AuthenticationException exception) throws IOException {
        res.setContentType("application/json");
        JSONObject jsonObj = new JSONObject();
        String exceptionMsg = exception.getMessage();
        if(exceptionMsg.contains("Bad credentials")) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonObj.put("response", "Authentication failed. Please enter valid email ID and password.");

        }
        else {
            jsonObj.put("response", exceptionMsg);
        }
        res.getWriter().write(jsonObj.toString());
        res.getWriter().flush();
    }
}