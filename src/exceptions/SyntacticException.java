package exceptions;

public class SyntacticException extends RuntimeException {

    public SyntacticException(String msg) {
        super(msg);
        System.exit(0);
    }

}
