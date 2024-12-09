package dev.kuehni.jeecms.exception.result;

import jakarta.servlet.http.HttpServletResponse;

public class NotFoundException extends ResultException {

    public NotFoundException() {
        super(HttpServletResponse.SC_NOT_FOUND);
    }

    public NotFoundException(String message) {
        super(message, HttpServletResponse.SC_NOT_FOUND);
    }
}
