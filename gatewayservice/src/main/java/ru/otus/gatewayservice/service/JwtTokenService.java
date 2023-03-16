package ru.otus.gatewayservice.service;

import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;

@Service
@Slf4j
public class JwtTokenService {

    @Value("${jwt.publicKey}")
    private String publicKeyValue;

    private PublicKey publicKey;

    @SneakyThrows
    @PostConstruct
    private void init() {
        publicKey = KeyFactory.getInstance("RSA")
                .generatePublic(
                        new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyValue.replaceAll("\\n", "")
                                .replace("-----BEGIN PUBLIC KEY-----", "")
                                .replace("-----END PUBLIC KEY-----", "")
                                .replaceAll(" ", ""))));
    }

    public Optional<String> getLogin(String token) {
        try {
            return Optional.of(Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());
        } catch (Exception e) {
            log.error("Invalid token", e);
        }
        return Optional.empty();
    }
}
