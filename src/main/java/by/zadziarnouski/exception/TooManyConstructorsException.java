package by.zadziarnouski.exception;

public class TooManyConstructorsException extends RuntimeException{
    public TooManyConstructorsException(String message) {
        super(message);
    }
}
