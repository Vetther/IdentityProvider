package pl.owolny.identityprovider.config.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwkConfig {

    private final RSAPrivateKey privateKeyResource;
    private final RSAPublicKey publicKeyResource;

    public JwkConfig(@Value("${jwt.key.private}") RSAPrivateKey privateKeyResource,
                     @Value("${jwt.key.public}") RSAPublicKey publicKeyResource) {
        this.privateKeyResource = privateKeyResource;
        this.publicKeyResource = publicKeyResource;
    }

    public JWKSet getJwkSet() {
        return new JWKSet(new RSAKey.Builder(this.publicKeyResource)
                .privateKey(this.privateKeyResource)
                .build());
    }
}

