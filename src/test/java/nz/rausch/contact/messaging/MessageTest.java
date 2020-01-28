package nz.rausch.contact.messaging;

import nz.rausch.contact.messaging.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessageTest {
    private Message message;

    @Before
    public void setup() {
        message = new Message();
    }

    @Test(expected = ValidationException.class)
    public void checkBlankNameNotAccepted() throws ValidationException {
        message.setName("");
    }

    @Test
    public void checkValidNameAccepted() throws ValidationException {
        String validName = "Test";
        message.setName(validName);

        assertEquals(validName, message.getName());
    }

    @Test(expected = ValidationException.class)
    public void checkBlankMessageNotAccepted() throws ValidationException{
        message.setMessage("");
    }

    @Test
    public void checkValidMessageAccepted() throws ValidationException{
        String validMessage = "This is a message";
        message.setMessage(validMessage);

        assertEquals(validMessage, message.getMessage());
    }

    @Test
    public void checkHtmlRemovedFromMessage() throws ValidationException{
        String messageWithHtml = "<img src=\"http://test...\"/>Hello, World!";
        String messageSanitized = "Hello, World!";

        message.setMessage(messageWithHtml);

        assertEquals(messageSanitized, message.getMessage());
    }

    @Test(expected = ValidationException.class)
    public void checkSenderAddressRejectedWhenInvalid() throws ValidationException {
        String invalidEmail = "invalid@";
        message.setSenderAddress(invalidEmail);
    }

    @Test
    public void checkSenderAddressAcceptedWhenValid() throws ValidationException {
        String validEmail = "test@test.co.nz";
        message.setSenderAddress(validEmail);

        assertEquals(validEmail, message.getSenderAddress());
    }

    @Test(expected = ValidationException.class)
    public void checkToAddressRejectedWhenInvalid() throws ValidationException {
        String invalidEmail = "invalid@";
        message.setToAddress(invalidEmail);
    }

    @Test
    public void checkToAddressAcceptedWhenValid() throws ValidationException {
        String validEmail = "test@test.co.nz";
        message.setToAddress(validEmail);

        assertEquals(validEmail, message.getToAddress());
    }

}