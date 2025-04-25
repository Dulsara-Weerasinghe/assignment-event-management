package com.event.service.filter;

import com.event.service.security.JwtService;
import com.event.service.service.UserInfoService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserInfoService userInfoService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        String token = null;
        String userId = null;
        if (Objects.nonNull(authorization) && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
            userId = jwtService.getUserName(token);
        }

        if (Objects.nonNull(userId) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            UserDetails userDetails = userInfoService.loadUserByUsername(userId);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken unPwAuthToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                unPwAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(unPwAuthToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
