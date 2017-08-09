package org.echo.seckill.exception;

/**
 * Created by Administrator on 8/6/2017.
 */
public class SeckillClosedException extends RuntimeException {

    public SeckillClosedException(String message) {
        super(message);
    }

    public SeckillClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
