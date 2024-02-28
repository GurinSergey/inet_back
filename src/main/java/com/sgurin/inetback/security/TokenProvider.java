package com.sgurin.inetback.security;

import com.sgurin.inetback.config.AppProperties;
import com.sgurin.inetback.exeption.InvalidTokenException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Slf4j
public class TokenProvider {
    private final AppProperties appProperties;

    @Autowired
    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String createToken(Authentication authentication, String ip) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(appProperties.getAuth().getTokenExpirationMsec());

        return Jwts.builder()
                .setSubject(String.format("%s,%s", userPrincipal.getId(), userPrincipal.getEmail()))
                .setIssuer(ip)
                .setExpiration(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    private String[] getTokenSubjectItems(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject().split(",");
    }

    public Long getUserIdFromToken(String token) {
        String[] split = getTokenSubjectItems(token);

        return Long.parseLong(split[0]);
    }

    public Long getUserEmailFromToken(String token) {
        String[] split = getTokenSubjectItems(token);

        return Long.parseLong(split[1]);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }

        return false;
    }
}
