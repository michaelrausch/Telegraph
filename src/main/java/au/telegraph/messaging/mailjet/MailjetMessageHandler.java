package au.telegraph.messaging.mailjet;

import au.telegraph.messaging.Message;
import au.telegraph.messaging.exceptions.MessageSendException;
import ch.qos.logback.classic.Logger;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import au.telegraph.configuration.models.ClientConfiguration;
import au.telegraph.messaging.MessageHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

public class MailjetMessageHandler implements MessageHandler {
    private static final String SUBJECT = "Contact Form Message";
    private static final String DUMMY_EMAIL = "test@rausch.nz";

    private MailjetClient client;
    private MailjetConfig configuration;
    private static Logger logger = (Logger) LoggerFactory.getLogger(MailjetMessageHandler.class.getName());

    public MailjetMessageHandler(MailjetConfig config) {
        this.configuration = config;

        client = new MailjetClient(configuration.getPublicKey(),
                configuration.getPrivateKey(),
                new ClientOptions("v3.1"));

        logger.debug("Created new Mailjet client");
    }

    @Override
    public void send(Message message, ClientConfiguration clientCfg) throws MessageSendException {
        MailjetRequest request;
        MailjetResponse response;

        if (!configuration.getEnabled()) {
            logger.debug("Mailjet disabled");
            return;
        }

        if (message.getSenderAddress().equals(DUMMY_EMAIL)) {
            logger.info("Dummy Email Used");
            return;
        }

        logger.debug("Preparing to send new message from " + message.getSenderAddress());

        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                .put(new JSONObject()
                .put(Emailv31.Message.FROM, new JSONObject()
                .put("Email", configuration.getFromEmail())
                .put("Name", configuration.getFromName()))
                .put(Emailv31.Message.TO, new JSONArray()
                .put(new JSONObject()
                .put("Email", clientCfg.getMailTo())
                .put("Name", clientCfg.getName())))
                .put(Emailv31.Message.SUBJECT, SUBJECT)
                .put(Emailv31.Message.TEXTPART, message.toString())
                .put(Emailv31.Message.HTMLPART, message.asHtml())));
        try {
            response = client.post(request);
        } catch (MailjetSocketTimeoutException | MailjetException e) {
            logger.error("Mailjet Exception " + e.getMessage());
            throw new MessageSendException(e.getMessage());
        }

        if (response.getStatus() != 200) {
            logger.error("Non-200 response from Mailjet got: " + response.getStatus());
            throw new MessageSendException("Invalid response code from Mailjet (Got " + response.getStatus() +
                    " " + response.getData());
        }
    }

}
