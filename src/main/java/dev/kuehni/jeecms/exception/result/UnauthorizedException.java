package dev.kuehni.jeecms.exception.result;

import jakarta.servlet.http.HttpServletResponse;

public class UnauthorizedException extends ResultException {

    public UnauthorizedException(String message) {
        super(message, HttpServletResponse.SC_UNAUTHORIZED);
    }
}
