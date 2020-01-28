package nz.rausch.contact.messaging;

public class MessageSendException extends Exception {
    public MessageSendException(String errorMessage) {
        super(errorMessage);
    }
}
