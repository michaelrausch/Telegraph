package au.telegraph.messaging.ifttt;

import au.telegraph.http.client.HttpClient;
import au.telegraph.http.client.HttpResponse;
import au.telegraph.configuration.models.ClientConfiguration;
import au.telegraph.messaging.Message;
import au.telegraph.messaging.MessageHandler;
import au.telegraph.messaging.exceptions.MessageSendException;
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
