package dev.kuehni.jeecms.auth;

import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthFilter implements Filter {
    @Inject
    private AuthBean authBean;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!authBean.getAuthState().isLoggedIn()) {
            ((HttpServletResponse) response).sendRedirect(request.getServletContext().getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}
