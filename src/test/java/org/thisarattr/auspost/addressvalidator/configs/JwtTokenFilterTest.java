package org.thisarattr.auspost.addressvalidator.configs;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.thisarattr.auspost.addressvalidator.models.User;
import org.thisarattr.auspost.addressvalidator.services.CacheService;

@RunWith(MockitoJUnitRunner.class)
public class JwtTokenFilterTest {

    private JwtTokenFilter filter;
    @Mock
    private CacheService mockCacheService;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private FilterChain mockFilterChain;
    private String secretKey = "eyJzdWIiOiJ1c2VyIiwicm9sZSI6InVzZXIiL";

    @Before
    public void setUp() throws Exception {
        filter = new JwtTokenFilter(mockCacheService, secretKey);
    }

    @Test
    public void shouldSendOkWhenHttpMethodIsOption() throws Exception {
        when(mockRequest.getMethod()).thenReturn("OPTIONS");
        filter.doFilter(mockRequest, mockResponse, mockFilterChain);
        verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        verify(mockFilterChain).doFilter(mockRequest, mockResponse);
    }

    @Test
    public void shouldSendUnAuthorisedWhenTokenIsNotProvided() throws Exception {
        filter.doFilter(mockRequest, mockResponse, mockFilterChain);
        verify(mockResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
    }

    @Test
    public void shouldSendUnAuthorisedWhenTokenNotInCorrectFormat() throws Exception {
        when(mockRequest.getHeader("authorization")).thenReturn("invalidToken");
        filter.doFilter(mockRequest, mockResponse, mockFilterChain);
        verify(mockResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
    }

    @Test
    public void shouldSendUnauthorisedWhenUserIsNotInCache() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6InVzZXIiLCJpYXQiOjE0OTk1ODg4ODF9."
                + "MFoBA1sbZWapVAyMpi-btZ-lQYsEVMiD45ptJxbQ4Lc";
        when(mockRequest.getHeader("authorization")).thenReturn("Bearer " + token);
        when(mockCacheService.get(token)).thenReturn(Optional.empty());
        filter.doFilter(mockRequest, mockResponse, mockFilterChain);
        verify(mockResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
    }

    @Test
    public void shouldSendOkWhenUserPresentInCache() throws Exception  {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6InVzZXIiLCJpYXQiOjE0OTk1ODg4ODF9."
                + "MFoBA1sbZWapVAyMpi-btZ-lQYsEVMiD45ptJxbQ4Lc";
        when(mockRequest.getHeader("authorization")).thenReturn("Bearer " + token);
        when(mockCacheService.get(token)).thenReturn(Optional.of(new User()));
        filter.doFilter(mockRequest, mockResponse, mockFilterChain);
        verify(mockRequest).setAttribute(any(String.class), any(LinkedHashMap.class));
    }

    @Test
    public void shouldSendUnauthorisedWhenMalformedToken() throws Exception  {
        String token = "123";
        when(mockRequest.getHeader("authorization")).thenReturn("Bearer " + token);
        when(mockCacheService.get(token)).thenReturn(Optional.of(new User()));
        filter.doFilter(mockRequest, mockResponse, mockFilterChain);
        verify(mockResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
    }

}