package nz.rausch.contact.messaging.ifttt;

import nz.rausch.contact.configuration.models.ClientConfiguration;
import nz.rausch.contact.http.client.HttpClient;
import nz.rausch.contact.http.client.HttpResponse;
import nz.rausch.contact.messaging.Message;
import nz.rausch.contact.messaging.MessageHandler;
import nz.rausch.contact.messaging.exceptions.MessageSendException;
import org.json.JSONObject;

import java.io.IOException;

public class IftttMessageHandler implements MessageHandler {
    private IftttConfiguration configuration;
    private String IFTTT_WEBHOOK_URL_BASE =  "https://maker.ifttt.com/trigger/";
    private HttpClient httpClient;

    public IftttMessageHandler (IftttConfiguration configuration, HttpClient httpClient) {
        this.configuration = configuration;
        this.httpClient = httpClient;
    }

    @Override
    public void send(Message message, ClientConfiguration clientConfiguration) throws MessageSendException {
        if (!configuration.getEnabled()) {
            return;
        }

        try {
            doWebhookRequest(message);
        } catch (IOException e) {
            throw new MessageSendException(e.getMessage());
        }
    }

    private String buildWebhookUrl() {
        String url = IFTTT_WEBHOOK_URL_BASE + configuration.getEventName() + "/with/key/"
                + configuration.getPrivateKey();

        return url;
    }

    private void doWebhookRequest(Message message) throws IOException, MessageSendException {
        httpClient.setContentType("application/json");
        httpClient.setBody(buildJsonBody(message.getName(), message.getSenderAddress(), message.getMessage()));
        httpClient.setUrl(buildWebhookUrl());

        HttpResponse response = httpClient.executePost();

        if (response.getStatusCode() != 200) {
            throw new MessageSendException("Non 200 response code from IFTTT");
        }
    }

    private String buildJsonBody(String name, String email, String message) {
        return new JSONObject()
                .put("value1", name)
                .put("value2", email)
                .put("value3", message)
                .toString();
    }
}
