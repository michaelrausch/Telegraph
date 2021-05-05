package nz.rausch.contact.messaging;

import nz.rausch.contact.configuration.models.ClientConfiguration;
import nz.rausch.contact.messaging.exceptions.MessageSendException;

public interface MessageHandler  {
    void send(Message message, ClientConfiguration client) throws MessageSendException;
}
