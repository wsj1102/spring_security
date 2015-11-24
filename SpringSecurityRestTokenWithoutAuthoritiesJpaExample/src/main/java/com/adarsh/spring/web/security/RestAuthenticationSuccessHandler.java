package com.adarsh.spring.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @author Adarsh
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 0001 $, $Date:: 1/1/13 00:11 AM#$
 */
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(RestAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        log.info("RestAuthenticationSuccessHandler onAuthenticationSuccess()");
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("MESSAGE", "AUTHENTICATION-SUCCESS");
        response.addHeader("LOGIN-MESSAGE","LOGIN-SUCCESS") ;
        clearAuthenticationAttributes(request);
    }
}