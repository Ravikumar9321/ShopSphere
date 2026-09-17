package com.shopsphere.Utility;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		  String authHead = request.getHeader("Authorization");
		  String path = request.getRequestURI();
		  // Skip Swagger + public endpoints
	        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") ||
	            path.startsWith("/swagger-resources") || path.startsWith("/webjars") ||
	            path.startsWith("/api/auth")) {
	            filterChain.doFilter(request, response);
	            return;
	        }
	        if(authHead==null || !authHead.startsWith("Bearer ")) {
	        	filterChain.doFilter(request, response);
	        	return;
	        }
	        String token = authHead.substring(7);

	        if (!jwtUtil.validateToken(token)) {
	            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired or invalid");
	            return;
	        }
	 

    if (SecurityContextHolder.getContext().getAuthentication() == null) {
        String email = jwtUtil.extractEmail(token);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
 	}

}
