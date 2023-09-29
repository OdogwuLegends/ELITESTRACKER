package com.capstoneproject.ElitesTracker.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.capstoneproject.ElitesTracker.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.capstoneproject.ElitesTracker.enums.ExceptionMessages.INVALID_AUTHORIZATION_HEADER_EXCEPTION;
import static com.capstoneproject.ElitesTracker.enums.ExceptionMessages.VERIFICATION_FAILED_EXCEPTION;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@ToString
@Slf4j
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

    public static String retrieveAndVerifyJwtToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            throw new UnauthorizedException(INVALID_AUTHORIZATION_HEADER_EXCEPTION.getMessage());
        }

        String authorizationToken = authorizationHeader.substring(BEARER.length());
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                    .withIssuer(ELITES_TRACKER)
                    .withClaimPresence(USER_EMAIL)
                    .build();

        DecodedJWT verifiedToken =  verifier.verify(authorizationToken);

        if(verifiedToken != null){
            return authorizationToken;
        }
        throw new UnauthorizedException(VERIFICATION_FAILED_EXCEPTION.getMessage());
    }

    public static String extractEmailFromToken(String jwtToken){
        DecodedJWT decodedJWT = JWT.decode(jwtToken);
        Map<String, Claim> claimsMap = decodedJWT.getClaims();
        if (claimsMap.containsKey(USER_EMAIL)) {
            return claimsMap.get(USER_EMAIL).asString();
        }
        throw new UnauthorizedException(VERIFICATION_FAILED_EXCEPTION.getMessage());
    }

}
