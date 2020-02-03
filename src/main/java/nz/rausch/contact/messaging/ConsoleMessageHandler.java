package nz.rausch.contact.messaging;

/**
 * Prints messages to the console for debugging purposes
 */
public class ConsoleMessageHandler implements MessageHandler {
    @Override
    public void send(Message message) {
        System.out.println("---- MESSAGE ----");
        System.out.println("From: " + message.getSenderAddress());
        System.out.println("From: " + message.getName());
        System.out.println("Message: " + message.getMessage());
    }
}
