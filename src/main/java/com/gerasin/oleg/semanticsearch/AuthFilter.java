package com.gerasin.oleg.semanticsearch;

import java.io.IOException;
import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author geras
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" })
public class AuthFilter implements Filter
{
    private static Logger log = LogManager.getLogger(AuthFilter.class.getName());

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException
    {
            try
        {
            log.info("doFilter ...");
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;

            HttpSession session = request.getSession(false);

            String user = (session != null) ? (String) session.getAttribute("username") : null;

            String loginURL = request.getContextPath() + "/login.xhtml";

            boolean loginRequest = request.getRequestURI().startsWith(loginURL);
            boolean resourceRequest = request.getRequestURI().startsWith(request.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER);

            if (user != null || loginRequest || resourceRequest)
            {
                chain.doFilter(request, response);
            }
            else
            {
                response.sendRedirect(loginURL);
            }
        }
        catch (IOException | ServletException ex)
        {
            log.warn(ex);
        }
    }
}
