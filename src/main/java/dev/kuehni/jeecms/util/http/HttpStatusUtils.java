package dev.kuehni.jeecms.util.http;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class HttpStatusUtils {
    private HttpStatusUtils() {}

    @Nonnull
    public static String getStatusText(int statusCode) {
        return switch (statusCode) {
            case 200 -> "OK";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "Unknown";
        };
    }

    @Nullable
    public static String getStatusDescription(int statusCode) {
        // See https://developer.mozilla.org/en-US/docs/Web/HTTP/Status
        return switch (statusCode) {
            case 400 ->
                    "The server cannot or will not process the request due to something that is perceived to be a client error (e.g., malformed request syntax, invalid request message framing, or deceptive request routing)";
            case 401 -> "The client bust authenticate itself to get the requested resource.";
            case 403 -> "The client does not have access rights to the content.";
            case 404 -> "The server cannot find the requested resource.";
            case 500 -> "The server has encountered an internal error.";
            default -> null;
        };
    }
}
