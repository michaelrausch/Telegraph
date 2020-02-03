package nz.rausch.contact.messaging.ifttt;

import nz.rausch.contact.http.client.HttpClient;
import nz.rausch.contact.http.client.HttpResponse;
import nz.rausch.contact.messaging.Message;
import nz.rausch.contact.messaging.exceptions.MessageSendException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IftttMessageHandlerTest {
    private IftttConfiguration configuration;
    private HttpClient httpClient;
    private IftttMessageHandler handler;

    @Before
    public void setup() throws IOException {
        configuration = mock(IftttConfiguration.class);
        httpClient = mock(HttpClient.class);
        handler = new IftttMessageHandler(configuration, httpClient);

        when(configuration.getEnabled()).thenReturn(true);
        when(configuration.getEventName()).thenReturn("test");
        when(configuration.getPrivateKey()).thenReturn("testkey");

        when(httpClient.executePost()).thenReturn(new HttpResponse(200, ""));
    }

    @Test
    public void checkHandlerDoesntRunWhenDisabled() throws IOException, MessageSendException {
        when(configuration.getEnabled()).thenReturn(false);

        handler.send(new Message());

        verify(httpClient, never()).executePost();
    }

    @Test
    public void checkHandlerCreatesCorrectURL() throws MessageSendException {
        String expected = "https://maker.ifttt.com/trigger/test/with/key/testkey";

        handler.send(new Message());

        verify(httpClient).setUrl(expected);
    }

    @Test(expected = MessageSendException.class)
    public void checkHandlerFailsOnNon200Response() throws IOException, MessageSendException {
        when(httpClient.executePost()).thenReturn(new HttpResponse(500, ""));

        handler.send(new Message());
    }
}