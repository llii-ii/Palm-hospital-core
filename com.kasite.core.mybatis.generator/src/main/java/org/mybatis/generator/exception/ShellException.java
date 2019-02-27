package org.mybatis.generator.exception;

public class ShellException extends Exception
{
    static final long serialVersionUID = -2026841561754434544L;
    
    public ShellException() {
    }
    
    public ShellException(final String arg0) {
        super(arg0);
    }
    
    public ShellException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }
    
    public ShellException(final Throwable arg0) {
        super(arg0);
    }
}
