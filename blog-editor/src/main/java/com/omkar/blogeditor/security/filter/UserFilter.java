package com.omkar.blogeditor.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UserFilter extends OncePerRequestFilter {

    private String SECRET_KEY = "secret";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody();
        List<String> roles = Arrays.asList(claims.get("role", String.class).split(","));
        if ( !roles.contains("user")){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
