package com.adarsh.spring.resteasy.security;



import com.adarsh.spring.resteasy.persistance.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Adarsh Kumar
 * @author $LastChangedBy: Adarsh Kumar$
 * @version $Revision: 0001 $, $Date:: 1/1/10 0:00 AM#$
 */
@Service
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

    private static Logger LOG = LoggerFactory.getLogger(AuthenticationTokenProcessingFilter.class);

    @Autowired(required = true)
    private TokenProvider tokenProvider;

    @Autowired(required = true)
    private AuthenticationManager authenticationManager;

    private SecurityContextProvider securityContextProvider  = new SecurityContextProvider();
    private WebAuthenticationDetailsSource webAuthenticationDetailsSource = new WebAuthenticationDetailsSource();

    public AuthenticationTokenProcessingFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        LOG.debug("Checking headers and parameters for authentication token...");

        String token = null;

        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (httpServletRequest.getHeader("Authentication-token") != null) {
            token = httpServletRequest.getHeader("Authentication-token");
            LOG.debug("Found token '" + token + "' in request headers");
        }

        if (token != null) {
            if (tokenProvider.isTokenValid(token)) {
                final User user = tokenProvider.getUserFromToken(token);
                LOG.debug("Inside-AuthenticationTokenProcessingFilter.java");
                this.authenticateUser(httpServletRequest, user);
            }
        }
        chain.doFilter(request, response);
    }

    private void authenticateUser(HttpServletRequest request, User user) {
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword());
        authentication.setDetails(this.webAuthenticationDetailsSource.buildDetails(request));
        final SecurityContext securityContext = this.securityContextProvider.getSecurityContext();
        securityContext.setAuthentication(this.authenticationManager.authenticate(authentication));
    }
}
