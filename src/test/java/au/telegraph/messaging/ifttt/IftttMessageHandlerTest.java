package au.telegraph.messaging.ifttt;

import au.telegraph.configuration.models.ClientConfiguration;
import au.telegraph.http.client.HttpClient;
import au.telegraph.http.client.HttpResponse;
import au.telegraph.messaging.Message;
import au.telegraph.messaging.exceptions.MessageSendException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class IftttMessageHandlerTest {
    private ClientConfiguration appConfig;
    private IftttConfiguration configuration;
    private HttpClient httpClient;
    private IftttMessageHandler handler;
    private ClientConfiguration clientConfiguration;

    @Before
    public void setup() throws IOException {
        configuration = mock(IftttConfiguration.class);
        httpClient = mock(HttpClient.class);
        handler = new IftttMessageHandler(configuration, httpClient);
        appConfig = mock(ClientConfiguration.class);

        when(configuration.getEnabled()).thenReturn(true);
        when(configuration.getEventName()).thenReturn("test");
        when(configuration.getPrivateKey()).thenReturn("testkey");

        when(httpClient.executePost()).thenReturn(new HttpResponse(200, ""));

        clientConfiguration = new ClientConfiguration();
        clientConfiguration.setPublicKey("TEST");
    }

    @Test
    public void checkHandlerDoesntRunWhenDisabled() throws IOException, MessageSendException {
        when(configuration.getEnabled()).thenReturn(false);


        handler.send(new Message(), clientConfiguration);

        verify(httpClient, never()).executePost();
    }

    @Test
    public void checkHandlerCreatesCorrectURL() throws MessageSendException {
        String expected = "https://maker.ifttt.com/trigger/test/with/key/testkey";

        handler.send(new Message(), clientConfiguration);

        verify(httpClient).setUrl(expected);
    }

    @Test(expected = MessageSendException.class)
    public void checkHandlerFailsOnNon200Response() throws IOException, MessageSendException {
        when(httpClient.executePost()).thenReturn(new HttpResponse(500, ""));

        handler.send(new Message(), appConfig);
    }
}