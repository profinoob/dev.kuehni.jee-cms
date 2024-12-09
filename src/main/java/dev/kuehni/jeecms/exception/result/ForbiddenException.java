package dev.kuehni.jeecms.exception.result;

import jakarta.servlet.http.HttpServletResponse;

public class ForbiddenException extends ResultException {

    public ForbiddenException() {
        super(HttpServletResponse.SC_FORBIDDEN);
    }

    public ForbiddenException(String message) {
        super(message, HttpServletResponse.SC_FORBIDDEN);
    }
}
