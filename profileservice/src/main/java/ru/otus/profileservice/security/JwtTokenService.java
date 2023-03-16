package ru.otus.profileservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtTokenService {
    @Value("${jwt.privateKey}")
    private String privateKeyValue;
    @Value("${jwt.livingTimeInHours}")
    private Integer livingTimeInHours;
    private PrivateKey privateKey;

    @SneakyThrows
    @PostConstruct
    private void init() {
        privateKey = KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyValue
                                .replaceAll("\\n", "")
                                .replace("-----BEGIN PRIVATE KEY-----", "")
                                .replace("-----END PRIVATE KEY-----", "")
                                .replaceAll(" ", ""))));
    }

    public String generate(String userLogin) {
        Date date = Date.from(LocalDateTime.now().plusHours(livingTimeInHours).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(userLogin)
                .setExpiration(date)
                .setHeaderParam("alg", "RS256")
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}
