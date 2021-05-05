package au.telegraph.messaging;

import au.telegraph.messaging.exceptions.MessageSendException;
import au.telegraph.configuration.models.ClientConfiguration;

public interface MessageHandler  {
    void send(Message message, ClientConfiguration client) throws MessageSendException;
}
