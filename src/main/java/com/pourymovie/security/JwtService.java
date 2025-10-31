package com.pourymovie.security;

import com.pourymovie.config.AppDefaults;
import com.pourymovie.entity.UserEntity;
import com.pourymovie.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {

  @Autowired
  private AppDefaults appDefaults;

  private SecretKey getKey() {
    byte[] keyBytes = appDefaults.getJwtSecretKey().getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public Long extractUserId(String token) {
    // extract the user id from jwt token
    String subject = extractClaim(token, Claims::getSubject);
    return Long.parseLong(subject);
  }

  public String extractEmail(String token) {
    return extractClaim(token, claims -> claims.get("email", String.class));
  }

  public String extractRole(String token) {
    return extractClaim(token, claims -> claims.get("role", String.class));
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date(System.currentTimeMillis()));
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String userEmail = extractEmail(token);
    return (userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
    final Claims claims = extractAllClaims(token);
    return claimResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
            .decryptWith(getKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }

  public String generateAccessToken(UserEntity user) {

    var claims = new HashMap<String, Object>();
    claims.put("email", user.getEmail());
    claims.put("role", user.getRole());

    var now = new Date(System.currentTimeMillis());
    var expiry = new Date(System.currentTimeMillis() + 1000L * 60 * appDefaults.getDefaultAccessTokenTTlInMinutes());

    return Jwts.builder()
            .subject(Long.toString(user.getId()))
            .claims(claims)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(getKey())
            .compact();
  }


}
