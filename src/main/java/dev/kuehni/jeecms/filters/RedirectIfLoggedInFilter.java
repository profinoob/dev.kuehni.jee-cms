package dev.kuehni.jeecms.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RedirectIfLoggedInFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        final var request = (HttpServletRequest) servletRequest;
        final var response = (HttpServletResponse) servletResponse;

        if (request.getUserPrincipal() != null) {
            response.sendRedirect(request.getRequestURI() + "/app");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
