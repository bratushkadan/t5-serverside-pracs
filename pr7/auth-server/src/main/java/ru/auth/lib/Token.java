package ru.auth.lib;

import java.util.Date;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import ru.auth.constant.Role;
import ru.auth.model.User;

public class Token {
  public static String create(User user) throws JWTCreationException {
    Algorithm algorithm = Algorithm.HMAC256("foobarbazqux");
    String token = JWT.create()
        .withIssuer("bratushkadan")
        .withClaim("username", user.getUsername())
        .withClaim("role", user.getRole().toString())
        .withJWTId(UUID.randomUUID().toString())
        .withIssuedAt(new Date())
        .withExpiresAt(new Date(System.currentTimeMillis() + 5000000000L))
        .sign(algorithm);
    return token;
  }

  public static TokenPayload verify(String token) throws JWTVerificationException {
    Algorithm algorithm = Algorithm.HMAC256("foobarbazqux");
    JWTVerifier verifier = JWT.require(algorithm)
        .withIssuer("bratushkadan")
        .build();

    DecodedJWT decodedJWT = verifier.verify(token);

    String usernameClaim = decodedJWT.getClaim("username").asString();
    String roleClaimStr = decodedJWT.getClaim("role").asString();
    Role roleClaim = Role.fromString(roleClaimStr);

    return new TokenPayload(usernameClaim, roleClaim);
  }
}
