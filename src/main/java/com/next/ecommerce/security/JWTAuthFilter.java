package com.next.ecommerce.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.next.ecommerce.service.implementation.UserServiceImplementation;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTAuthFilter extends OncePerRequestFilter {

    private JWTService jwtService;

    private UserServiceImplementation userServiceImplementation;

    public JWTAuthFilter(JWTService jwtService, UserServiceImplementation userServiceImplementation) {
        this.jwtService = jwtService;
        this.userServiceImplementation = userServiceImplementation;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            String token = authorizationHeader.split(" ")[1];
            boolean isValidToken = jwtService.isValidToken(token);
            if(isValidToken) {
                String usernameFromToken = jwtService.getUsernameFromToken(token);
                UserDetails userDetails = userServiceImplementation.loadUserByUsername(usernameFromToken);
                UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                userAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(userAuthentication);
            }
        }
        filterChain.doFilter(request, response);
    }
    
}
