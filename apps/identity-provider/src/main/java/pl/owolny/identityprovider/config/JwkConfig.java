package pl.owolny.identityprovider.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwkConfig {

    @Value("${jwt.key.private}")
    private RSAPrivateKey privateKeyResource;

    @Value("${jwt.key.public}")
    private RSAPublicKey publicKeyResource;

    public JWKSet getJwkSet() {
        return new JWKSet(new RSAKey.Builder(this.publicKeyResource)
                .privateKey(this.privateKeyResource)
                .build());
    }
}

