package mx.edu.utez.sigede_backend.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class TokenJwtConfig {
    private TokenJwtConfig() {}
    public static final Key SECRET_KEY= Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static final String PREFIX_TOKEN ="Bearer ";
    public static final  String HEADER_AUTHORIZATION="Authorization";
}
