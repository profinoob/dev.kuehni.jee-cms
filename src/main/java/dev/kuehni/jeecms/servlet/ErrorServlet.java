package dev.kuehni.jeecms.servlet;

import dev.kuehni.jeecms.exception.result.ResultException;
import dev.kuehni.jeecms.util.html.HtmlUtils;
import dev.kuehni.jeecms.util.http.HttpStatusUtils;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

@WebServlet(urlPatterns = "/error")
public class ErrorServlet extends HttpServlet {

    @Nullable
    private static Exception getException(@Nonnull HttpServletRequest request) {
        Objects.requireNonNull(request, "request");

        final Object value = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        return value instanceof Exception exception ? exception : null;
    }

    @Nullable
    private static ResultException getResultException(@Nonnull Exception exception) {
        if (exception instanceof ResultException resultException) {
            return resultException;
        }
        if (exception.getCause() instanceof Exception cause) {
            return getResultException(cause);
        }
        return null;
    }

    @Override
    protected void doGet(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response)
            throws IOException {
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(response, "response");

        final Exception exception = getException(request);
        assert exception != null;

        final ResultException resultException = getResultException(exception);

        final int statusCode = resultException != null ? resultException.httpStatusCode : 500;
        final String status = statusCode + " " + HttpStatusUtils.getStatusText(statusCode);
        final String statusDescription = HttpStatusUtils.getStatusDescription(statusCode);

        request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE);
        request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        response.setContentType("text/html; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write("<html><head><title>" + HtmlUtils.escape(status) + "</title></head><body>");
            writer.write("<h2>" + HtmlUtils.escape(status) + "</h2>");
            if (statusDescription != null)
                writer.write("<p>" + HtmlUtils.escape(statusDescription) + "</p>");
            writer.write("</html></body>");
        }
    }
}
