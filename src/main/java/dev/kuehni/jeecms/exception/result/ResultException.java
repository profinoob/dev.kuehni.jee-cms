package dev.kuehni.jeecms.exception.result;

public class ResultException extends RuntimeException {
    public final int httpStatusCode;

    public ResultException(int httpStatusCode) {
        super();
        this.httpStatusCode = httpStatusCode;
    }

    public ResultException(String message, int httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}
