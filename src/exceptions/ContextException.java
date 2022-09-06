package exceptions;

public class ContextException extends RuntimeException {

    public ContextException(String msg) {
        super(msg);
        System.exit(0);
    }

}
