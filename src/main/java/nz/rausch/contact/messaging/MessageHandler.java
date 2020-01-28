package nz.rausch.contact.messaging;

import nz.rausch.contact.messaging.exceptions.MessageSendException;

public interface MessageHandler  {
    void send(Message message) throws MessageSendException;
}
