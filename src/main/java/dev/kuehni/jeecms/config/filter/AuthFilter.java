package dev.kuehni.jeecms.config.filter;

import dev.kuehni.jeecms.service.auth.AuthBean;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthFilter implements Filter {
    @Inject
    private AuthBean authBean;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        authBean.refresh();
        if (!authBean.getAuthState().isLoggedIn()) {
            final HttpServletRequest httpRequest = (HttpServletRequest) request;
            final HttpServletResponse httpResponse = (HttpServletResponse) response;

            final String queryString = httpRequest.getQueryString();
            final String redirect = httpRequest.getRequestURI() + (queryString != null ? "?" + queryString : "");
            authBean.setRedirectToAfterLogin(redirect);
            httpResponse.sendRedirect(request.getServletContext().getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}
