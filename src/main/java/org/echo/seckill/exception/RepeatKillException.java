package org.echo.seckill.exception;

/**
 * Created by Administrator on 8/6/2017.
 */
public class RepeatKillException extends RuntimeException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
