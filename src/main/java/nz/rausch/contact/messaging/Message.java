package nz.rausch.contact.messaging;

import nz.rausch.contact.messaging.exceptions.ValidationException;
import org.apache.commons.validator.routines.EmailValidator;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class Message {
    private String name;
    private String message;
    private String senderAddress;
    private String toAddress;

    private PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

    public Message() {
        name = "";
        message = "";
        senderAddress = "";
        toAddress = "";
    }

    /**
     * Sets the name of the message sender
     * - Name must be longer than 1 character.
     * - Name is sanitized to prevent XSS attacks.
     * @param name The senders name
     * @return The message object
     * @throws ValidationException If the name is too short
     */
    public Message setName(String name) throws ValidationException {
        if (name.length() <= 1) throw new ValidationException("Name must be longer than 1 character");
        this.name = policy.sanitize(name);
        return this;
    }

    /**
     * Sets the message to be sent to the recipient.
     * - Messages must be longer than 1 character.
     * - Messages are sanitized to prevent XSS attacks
     * @param message The message to send
     * @return The message object
     * @throws ValidationException If there is no message
     */
    public Message setMessage(String message) throws ValidationException {
        if (message.length() <= 1) throw new ValidationException("Message must be longer than 1 character");
        this.message = policy.sanitize(message);
        return this;
    }

    /**
     * Sets the email address of the person sending the message. Email addresses must conform to the specification set out in @todo
     * @param senderAddress The senders email
     * @return The message object
     * @throws ValidationException If the email is invalid
     */
    public Message setSenderAddress(String senderAddress) throws ValidationException {
        EmailValidator validator = EmailValidator.getInstance();

        if (!validator.isValid(senderAddress)) {
            throw new ValidationException("Invalid Email Supplied (" + senderAddress + ")");
        }

        this.senderAddress = senderAddress;
        return this;
    }

    /**
     * Sets the email address of the person receiving the message. Email addresses must conform to the specification set out in @todo
     * @param toAddress The recipients email address
     * @return The message object
     * @throws ValidationException If the email is invalid
     */
    public Message setToAddress(String toAddress) throws ValidationException {
        EmailValidator validator = EmailValidator.getInstance();

        if (!validator.isValid(toAddress)) {
            throw new ValidationException("Invalid Email Supplied (" + toAddress + ")");
        }

        this.toAddress = toAddress;
        return this;
    }

    public String getToAddress() {
        return toAddress;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }
}
