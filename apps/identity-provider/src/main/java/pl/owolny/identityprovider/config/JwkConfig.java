package pl.owolny.identityprovider.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
public class JwkConfig {

    @Value("${jwt.key.private}")
    private RSAPrivateKey privateKeyResource;

    @Value("${jwt.key.public}")
    private RSAPublicKey publicKeyResource;

    public RSAPrivateKey getPrivateKey() {
        return privateKeyResource;
    }

    public RSAPublicKey getPublicKey() {
        return publicKeyResource;
    }

    public JWKSet getJwkSet() {
        return new JWKSet(new RSAKey.Builder(getPublicKey())
                .privateKey(getPrivateKey())
                .keyID(UUID.randomUUID().toString())
                .build());
    }
}

