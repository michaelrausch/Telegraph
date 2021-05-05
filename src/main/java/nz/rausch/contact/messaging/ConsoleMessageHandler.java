package nz.rausch.contact.messaging;

import nz.rausch.contact.configuration.models.ClientConfiguration;

/**
 * Prints messages to the console for debugging purposes
 */
public class ConsoleMessageHandler implements MessageHandler {
    @Override
    public void send(Message message, ClientConfiguration clientConfiguration) {
        System.out.println("---- MESSAGE ----");
        System.out.println("From: " + message.getSenderAddress());
        System.out.println("From: " + message.getName());
        System.out.println("Message: " + message.getMessage());
        System.out.println("ClientRealmPK: " + clientConfiguration.getPublicKey());
        System.out.println("ClientName: " + clientConfiguration.getName());
        System.out.println("ClientEmail: " + clientConfiguration.getMailTo());
    }
}
