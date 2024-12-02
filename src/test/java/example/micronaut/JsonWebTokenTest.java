package example.micronaut;

import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTParser;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.generator.AccessTokenConfiguration;
import io.micronaut.security.token.generator.TokenGenerator;
import io.micronaut.security.token.validator.TokenValidator;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class JsonWebTokenTest {

    @Inject
    TokenGenerator tokenGenerator;

    @Inject
    TokenValidator<?> tokenValidator;

    @Inject
    AccessTokenConfiguration accessTokenConfiguration;

    @Test
    void testItWorks() throws ParseException {
        String username = "sherlock";
        Authentication authentication = Authentication.build(username);
        Optional<String> jwtOptional = tokenGenerator.generateToken(authentication, accessTokenConfiguration.getExpiration());
        assertNotNull(jwtOptional);
        assertTrue(jwtOptional.isPresent());
        String jwt = jwtOptional.get();
        assertTrue(JWTParser.parse(jwt) instanceof EncryptedJWT);
        Authentication auth = Mono.from(tokenValidator.validateToken(jwt, null)).block();
        assertNotNull(auth)
        ;assertEquals(username, auth.getName());
    }

}
