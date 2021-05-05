package au.telegraph.messaging.exceptions;

public class MessageSendException extends Exception {
    public MessageSendException(String errorMessage) {
        super(errorMessage);
    }
}
