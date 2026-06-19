package net.centroweg.gerenciamentocompras.modules.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.centroweg.gerenciamentocompras.modules.auth.domain.exception.InvalidTokenException;
import net.centroweg.gerenciamentocompras.modules.auth.service.CustomUserDetailsService;
import net.centroweg.gerenciamentocompras.modules.auth.service.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private HandlerExceptionResolver resolver;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private SecurityFilter securityFilter;

    @BeforeEach
    void setUp() {
        securityFilter = new SecurityFilter(jwtService, customUserDetailsService, resolver);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should proceed with request when no cookies are present")
    void shouldProceedWithoutToken() throws Exception {
        when(request.getCookies()).thenReturn(null);

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(jwtService, customUserDetailsService, resolver);
    }

    @Test
    @DisplayName("Should proceed with request when no jwt cookie is present")
    void shouldProceedWithoutJwtCookie() throws Exception {
        when(request.getCookies()).thenReturn(new Cookie[]{ new Cookie("other", "value") });

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(jwtService, customUserDetailsService, resolver);
    }

    @Test
    @DisplayName("Should authenticate request when token is valid")
    void shouldAuthenticateWithValidToken() throws Exception {
        when(request.getCookies()).thenReturn(new Cookie[]{ new Cookie("jwt", "my-token") });
        when(jwtService.validateToken("my-token")).thenReturn("maria@gmail.com");

        UserDetails mockUserDetails = mock(UserDetails.class);
        when(customUserDetailsService.loadUserByUsername("maria@gmail.com")).thenReturn(mockUserDetails);

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(mockUserDetails, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verifyNoInteractions(resolver);
    }

    @Test
    @DisplayName("Should delegate to exception resolver when token is invalid")
    void shouldThrowExceptionWhenTokenIsInvalid() throws Exception {
        when(request.getCookies()).thenReturn(new Cookie[]{ new Cookie("jwt", "invalid-token") });
        when(jwtService.validateToken("invalid-token")).thenReturn(null);

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(resolver).resolveException(eq(request), eq(response), any(), any(InvalidTokenException.class));
        verify(filterChain, never()).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Should delegate to exception resolver when general exception occurs")
    void shouldResolveGeneralException() throws Exception {
        when(request.getCookies()).thenReturn(new Cookie[]{ new Cookie("jwt", "some-token") });
        when(jwtService.validateToken("some-token")).thenThrow(new RuntimeException("Database down"));

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(resolver).resolveException(eq(request), eq(response), any(), any(RuntimeException.class));
        verify(filterChain, never()).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
