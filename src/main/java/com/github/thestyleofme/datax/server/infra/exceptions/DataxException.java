package com.github.thestyleofme.datax.server.infra.exceptions;

/**
 * <p>
 * JSON异常
 * </p>
 *
 * @author thestyleofme 2020/6/29 13:35
 * @since 1.0.0
 */
public class DataxException extends RuntimeException {

    private static final long serialVersionUID = -1818741539098940203L;

    public DataxException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataxException(String message) {
        super(message);
    }

    public DataxException(Throwable cause) {
        super(cause);
    }

}
