package com.project.PetApp1.Security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import javax.xml.crypto.dsig.spec.HMACParameterSpec;
import java.util.Date;


@Component
public class JwtTokenProvider {

    @Value("${petapp.app.secret}")
    private String APP_SECRET;
    @Value("${petapp.expires.in}")
    private Long EXPIRES_IN;

    public String generateJwtToken(Authentication auth){
        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
        return Jwts.builder().subject(Long.toString(userDetails.getId()))
                .issuedAt(new Date()).expiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, APP_SECRET).compact();
    }

    public String generateJwtTokenByUserId(Long userId) {
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
        return Jwts.builder().setSubject(Long.toString(userId))
                .setIssuedAt(new Date()).setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, APP_SECRET).compact();
    }

    Long getUserIdFromJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(APP_SECRET).build().parseSignedClaims(token).getPayload();
        return Long.parseLong(claims.getSubject());
    }

    boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(APP_SECRET).build().parseSignedClaims(token);
            return !isTokenExpired(token);
        }catch( MalformedJwtException | ExpiredJwtException | UnsupportedJwtException |
               IllegalArgumentException e){
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().setSigningKey(APP_SECRET).build().parseSignedClaims(token).getPayload().getExpiration();
        return expiration.before(new Date());
    }

}
