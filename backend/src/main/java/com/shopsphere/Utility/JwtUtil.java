package com.shopsphere.Utility;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	  private final String SECRET="ewiugfwiufswobfawbfsolhfoSFLKswbvfloWGHOFEWLFWBPBEW";
	    private final long EXPIRATION = 1000 * 60 * 15; 
	    private Key secretKey=Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
	    
	    public String generateToken(String email) {
	    	return Jwts.builder()
	    			.setSubject(email)
	    			.setIssuedAt(new Date())
	    			.setExpiration(new Date(System.currentTimeMillis()+EXPIRATION))
	    			.signWith(secretKey,SignatureAlgorithm.HS256)
	    			.compact();
	    }
	    public boolean validateToken(String token) {
	    	try {
	    		getValidClaims(token);
				return true;
			} catch (Exception e) {
				return false;
			}
	    	
	    }
		private Claims getValidClaims(String token) {
                Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
                if (claims.getExpiration().before(new Date())) {
                    throw new ExpiredJwtException(null, claims, "Token expired");
                }
                return claims;
                
		}

	    public String extractEmail(String token) {
	        return getValidClaims(token).getSubject(); // reuse same logic
	    }
}
