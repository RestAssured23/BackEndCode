package coreapi.testingjdbc.exceptionerror;

public class DuplicateKeyViolationException extends RuntimeException{
    public DuplicateKeyViolationException(String message, String s) {
        super(message);
    }

}
