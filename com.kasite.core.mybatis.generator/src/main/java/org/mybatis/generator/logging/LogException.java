package org.mybatis.generator.logging;

public class LogException extends RuntimeException
{
    private static final long serialVersionUID = 7522435242386492002L;
    
    public LogException() {
    }
    
    public LogException(final String message) {
        super(message);
    }
    
    public LogException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public LogException(final Throwable cause) {
        super(cause);
    }
}
