package nz.rausch.contact.messaging;

import nz.rausch.contact.Server;
import nz.rausch.contact.messaging.exceptions.ValidationException;
import org.apache.commons.validator.routines.EmailValidator;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Message {
    private String name;
    private String message;
    private String senderAddress;

    private final PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
    private static Logger logger = LoggerFactory.getLogger(Server.class.getName());

    public Message() {
        name = "";
        message = "";
        senderAddress = "";
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
        if (!EmailValidator.getInstance().isValid(senderAddress)) {
            logger.debug("Email address validation failed on " + senderAddress);
            throw new ValidationException("Invalid 'senderAddress' Email Supplied (" + senderAddress + ")");
        }

        this.senderAddress = senderAddress;
        return this;
    }

    /**
     * Returns the message formatted in a HTML string.
     * @return The message formatted as HTML.
     */
    public String asHtml() {
        return "<h3>Message From " + this.getName() + " (" + this.getSenderAddress() + ")</h3>" +
                "<br/><br/>" + getMessage();
    }

    @Override
    public String toString() {
        return "Message From " + this.getName() + " (" + this.getSenderAddress() + ")" +
                "\n\n" + getMessage();
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
