package net.centroweg.gerenciamentocompras.modules.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserPrincipal userPrincipal;
    private static final String TEST_SECRET = "mySecretKeyForTestingPurposesMustBeAtLeast32BytesLong!";
    private static final int TEST_EXPIRATION_HOURS = 2;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtService, "expirationHours", TEST_EXPIRATION_HOURS);

        Role role = new Role("ADMIN");
        role.setId(1L);

        User user = new User();
        user.setId(1L);
        user.setName("Maria Eduarda");
        user.setEmail("maria@gmail.com");
        user.setCpf("12345678900");
        user.setPassword("encryptedPassword");
        user.setExtensionNumber("1234");
        user.setActive(true);
        user.setRole(role);

        userPrincipal = new UserPrincipal(user);
    }

    @Test
    @DisplayName("Should generate a non-null token with correct claims")
    void shouldGenerateTokenSuccessfully() {
        String token = jwtService.generateToken(userPrincipal);
        assertNotNull(token);
        assertFalse(token.isEmpty());

        String username = jwtService.validateToken(token);
        assertEquals("maria@gmail.com", username);
    }

    @Test
    @DisplayName("Should return null when validating an invalid or signature-mismatched token")
    void shouldReturnNullForInvalidToken() {
        String invalidToken = "invalid.token.here";
        String username = jwtService.validateToken(invalidToken);
        assertNull(username);
    }

    @Test
    @DisplayName("Should return null when validating an expired token")
    void shouldReturnNullForExpiredToken() {
        // Set negative expiration hours to simulate immediate expiration during token generation
        ReflectionTestUtils.setField(jwtService, "expirationHours", -1);
        String token = jwtService.generateToken(userPrincipal);

        // Reset to original to validate with regular config (the token itself is expired)
        ReflectionTestUtils.setField(jwtService, "expirationHours", TEST_EXPIRATION_HOURS);
        String username = jwtService.validateToken(token);
        assertNull(username);
    }

    @Test
    @DisplayName("Should return null when token is signed with a different key")
    void shouldReturnNullForTokenWithDifferentKey() {
        String otherSecret = "anotherDifferentSecretKeyForTestingPurposesMustBeLong!";
        JwtService otherJwtService = new JwtService();
        ReflectionTestUtils.setField(otherJwtService, "secret", otherSecret);
        ReflectionTestUtils.setField(otherJwtService, "expirationHours", TEST_EXPIRATION_HOURS);

        String tokenFromOtherService = otherJwtService.generateToken(userPrincipal);
        String username = jwtService.validateToken(tokenFromOtherService);
        assertNull(username);
    }
}
