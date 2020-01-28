package nz.rausch.contact.messaging.mailjet;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import nz.rausch.contact.configuration.AppConfigLoader;
import nz.rausch.contact.configuration.exception.ConfigLoadException;
import nz.rausch.contact.configuration.models.AppConfig;
import nz.rausch.contact.messaging.Message;
import nz.rausch.contact.messaging.MessageHandler;
import nz.rausch.contact.messaging.exceptions.MessageSendException;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.security.krb5.Config;

import java.io.IOException;

public class MailjetMessageHandler implements MessageHandler {
    private static final String SUBJECT = "Contact Form Message";
    private static final String GREETING = "New contact form message";

    private MailjetClient client;
    private static AppConfig configuration;

    public MailjetMessageHandler() {
        try {
            configuration = new AppConfigLoader().load(AppConfig.class).getConfig();
        } catch (ConfigLoadException e) {
            return;
        }

        client = new MailjetClient(configuration.getMailjetConfig().getPublicKey(),
                configuration.getMailjetConfig().getPrivateKey(),
                new ClientOptions("v3.1"));
    }

    @Override
    public void send(Message message) throws MessageSendException {
        MailjetRequest request;
        MailjetResponse response;

        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                .put(new JSONObject()
                .put(Emailv31.Message.FROM, new JSONObject()
                .put("Email", message.getToAddress())
                .put("Name", message.getName()))
                .put(Emailv31.Message.TO, new JSONArray()
                .put(new JSONObject()
                .put("Email", message.getToAddress())
                .put("Name", configuration.getToName())))
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

    //@todo put into formatter module
    private String formatMessageHTML(Message message) {
        return "<h3>" + GREETING + " from " + message.getName() + " (" + message.getSenderAddress() + ")</h3>" +
                "<br/>Message: <br/>" + message.getMessage();
    }

    private String formatMessagePlain(Message message) {
        return GREETING + " from " + message.getName() + " (" + message.getSenderAddress() + ")\n" +
                "Message: \n" + message.getMessage();
    }
}
