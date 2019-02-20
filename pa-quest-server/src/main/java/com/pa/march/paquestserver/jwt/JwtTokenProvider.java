package com.pa.march.paquestserver.jwt;

import com.pa.march.paquestserver.config.JwtConfig;
import com.pa.march.paquestserver.service.UserDetailsServiceImpl;
import com.pa.march.paquestserver.service.UserPrinciple;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private static final Logger LOG = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Autowired
    private JwtConfig jwtConfig;


    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public String createJwtToken(Authentication authentication, Collection<? extends GrantedAuthority> grantedAuthorities) {

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        Claims claims = Jwts.claims().setSubject(userPrinciple.getUsername());
        claims.put("roles", grantedAuthorities.stream().map(s -> s.toString().replace("ROLE_","").toLowerCase()).collect(Collectors.toList()));
        claims.put("authorities", grantedAuthorities.stream().map(s -> s.toString().toUpperCase()).collect(Collectors.toList()));

        LocalDateTime currentTime = LocalDateTime.now();
        return Jwts.builder()
                    .setSubject(userPrinciple.getUsername())
                .setClaims(claims)
                .setIssuer(jwtConfig.getTokenIssuer())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime.plusMinutes(jwtConfig.getTokenExpirationTime()).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getTokenSigningKey())
                .compact();

    }

    public Authentication getAuthentication(String token) {
        String username = getUsernameFromJwtToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtConfig.getTokenSigningKey()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            LOG.error("Invalid JWT signature -> Message: {} ", e);
        } catch (MalformedJwtException e) {
            LOG.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            LOG.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            LOG.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            LOG.error("JWT claims string is empty -> Message: {}", e);
        }
        return false;
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                    .setSigningKey(jwtConfig.getTokenSigningKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
