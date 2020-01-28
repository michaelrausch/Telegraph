package nz.rausch.contact.messaging;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import nz.rausch.contact.messaging.exceptions.MessageSendException;
import org.json.JSONArray;
import org.json.JSONObject;

public class MailjetMessageHandler implements MessageHandler{
    private static final String SUBJECT = "Contact Form Message";
    private static final String GREETING = "New contact form message";
    private static final String TO_NAME = "Michael Rausch";
    private static final String TO_EMAIL = "michael@rausch.nz";

    private MailjetClient client;

    public MailjetMessageHandler() {
        // @todo read from config file
        client = new MailjetClient("dbc654f8ecc8a2391e9ed9070822432e", "67a86e89292628cc700811148b7d0d51", new ClientOptions("v3.1"));
    }

    @Override
    public void send(Message message) throws MessageSendException {
        MailjetRequest request;
        MailjetResponse response;

        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                .put(new JSONObject()
                .put(Emailv31.Message.FROM, new JSONObject()
                .put("Email", TO_EMAIL)
                .put("Name", TO_NAME))
                .put(Emailv31.Message.TO, new JSONArray()
                .put(new JSONObject()
                .put("Email", TO_EMAIL)
                .put("Name", TO_NAME)))
                .put(Emailv31.Message.SUBJECT, SUBJECT)
                .put(Emailv31.Message.TEXTPART, formatMessagePlain(message))
                .put(Emailv31.Message.HTMLPART, formatMessageHTML(message))));

        try {
            response = client.post(request);
        } catch (MailjetSocketTimeoutException | MailjetException e) {
            e.printStackTrace();
            throw new MessageSendException(e.getMessage());
        }

        if (response.getStatus() != 200) {
            throw new MessageSendException("Invalid response code from Mailjet (Got " + response.getStatus() +
                    " " + response.getData());
        }
    }

    private String formatMessageHTML(Message message) {
        return "<h3>" + GREETING + " from " + message.getName() + " (" + message.getSenderAddress() + ")</h3>" +
                "<br/>Message: <br/>" + message.getMessage();
    }

    private String formatMessagePlain(Message message) {
        return GREETING + " from " + message.getName() + " (" + message.getSenderAddress() + ")\n" +
                "Message: \n" + message.getMessage();
    }
}
