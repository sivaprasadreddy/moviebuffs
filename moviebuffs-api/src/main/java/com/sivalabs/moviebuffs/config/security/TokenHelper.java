package com.sivalabs.moviebuffs.config.security;

import com.sivalabs.moviebuffs.config.ApplicationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenHelper {

    private final ApplicationProperties applicationProperties;
    private final TimeProvider timeProvider;

    private static final String AUDIENCE_WEB = "web";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            if(claims == null) return null;
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public String refreshToken(String token) {
        String refreshedToken;
        Date a = timeProvider.now();
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            if(claims == null) return null;
            claims.setIssuedAt(a);
            refreshedToken = Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(generateExpirationDate())
                    .signWith( SIGNATURE_ALGORITHM, applicationProperties.getJwt().getSecret() )
                    .compact();
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setIssuer( applicationProperties.getJwt().getIssuer() )
                .setSubject(username)
                .setAudience(AUDIENCE_WEB)
                .setIssuedAt(timeProvider.now())
                .setExpiration(generateExpirationDate())
                .signWith( SIGNATURE_ALGORITHM, applicationProperties.getJwt().getSecret() )
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(applicationProperties.getJwt().getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(timeProvider.now().getTime() + applicationProperties.getJwt().getExpiresIn() * 1000);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username != null && username.equals(userDetails.getUsername()));
    }

    public String getToken( HttpServletRequest request ) {
        String authHeader = getAuthHeaderFromHeader( request );
        if ( authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(applicationProperties.getJwt().getHeader());
    }

}
