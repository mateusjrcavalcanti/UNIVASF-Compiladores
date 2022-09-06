package exceptions;

public class LexiconException extends RuntimeException {

    public LexiconException(String msg) {
        super(msg);
        System.exit(0);
    }

}
