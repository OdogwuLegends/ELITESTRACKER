package com.capstoneproject.ElitesTracker.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.Instant;
import java.util.List;

import static com.capstoneproject.ElitesTracker.utils.HardCoded.*;

public class JwtUtil {
    public static String generateAccessTokenWithSecurity(List<String> authorities, String email){
        return JWT.create()
                .withClaim(USER_ROLE, authorities)
                .withClaim(USER_EMAIL, email)
                .withIssuer(ELITES_TRACKER)
                .withExpiresAt(Instant.now().plusSeconds(3600 * 24))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }
    public static String generateAccessTokenWithOutSecurity(String email){
        return JWT.create()
                .withClaim(USER_EMAIL, email)
                .withIssuer(ELITES_TRACKER)
                .withExpiresAt(Instant.now().plusSeconds(3600 * 24))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public static String extractEmailFromToken(String jwtToken){
        var claim = JWT.decode(jwtToken).getClaim(USER_EMAIL);
        return (String) claim.asMap().get(USER_EMAIL);
    }
}
