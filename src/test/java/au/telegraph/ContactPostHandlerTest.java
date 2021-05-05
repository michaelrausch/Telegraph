package au.telegraph;

import au.telegraph.configuration.models.AppConfig;
import au.telegraph.configuration.models.ClientConfiguration;
import au.telegraph.http.HttpContext;
import au.telegraph.http.ratelimiter.RateLimiter;
import au.telegraph.messaging.Message;
import au.telegraph.messaging.MessageHandler;
import au.telegraph.messaging.exceptions.MessageSendException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ContactPostHandlerTest {
    private ContactPostHandler contactHandler;
    private AppConfig testConfig;
    private HttpContext context;

    private final String RATELIMIT_IP_DENY = "10.10.10.0";
    private final String RATELIMIT_IP_ALLOW = "10.10.10.1";

    @Before
    public void setup() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMailTo("test@test.co");
        clientConfiguration.setName("Test");
        clientConfiguration.setPublicKey("TEST");

        List<ClientConfiguration> configurations = new ArrayList<>();
        configurations.add(clientConfiguration);

        context = mock(HttpContext.class);
        RateLimiter rateLimiter = mock(RateLimiter.class);

        // Setup test config
        testConfig = new AppConfig();
        testConfig.setClientList(configurations);

        // Setup HttpContext mock
        when(context.getFormParameter("name")).thenReturn("Michael");
        when(context.getFormParameter("email")).thenReturn("me@me.com");
        when(context.getFormParameter("message")).thenReturn("This is a message");
        when(context.getFormParameter("email_h_v")).thenReturn("");
        when(context.getFormParameter("realm")).thenReturn("TEST");

        when(context.checkParamExists(any(String.class))).thenReturn(true);

        when(context.getIp()).thenReturn(RATELIMIT_IP_ALLOW);

        when(rateLimiter.shouldAllowAccess(RATELIMIT_IP_ALLOW)).thenReturn(true);
        when(rateLimiter.shouldAllowAccess(RATELIMIT_IP_DENY)).thenReturn(false);

        // Create contact handler
        contactHandler = new ContactPostHandler(testConfig, rateLimiter);
    }

    @Test
    public void checkMessageAcceptsWhenValid() {
        when(context.checkParamExists(any(List.class))).thenReturn(true);

        contactHandler.Handle(context);

        verify(context).ok();
    }

    @Test
    public void checkBadRequestWhenRequiredParametersNotSupplied() {
        when(context.checkParamExists(any(List.class))).thenReturn(false);

        contactHandler.Handle(context);

        verify(context).badRequest();
    }

    @Test
    public void checkMessageHandlerCalledOnValidMessage() throws MessageSendException {
        MessageHandler messageHandler = mock(MessageHandler.class);
        when(context.checkParamExists(any(List.class))).thenReturn(true);

        List<MessageHandler> handlers = new ArrayList<>();
        handlers.add(messageHandler);
        contactHandler.setMessageHandlers(handlers);

        contactHandler.Handle(context);

        verify(messageHandler).send(any(Message.class), any(ClientConfiguration.class));
    }

    @Test
    public void checkMessageFailureReturnsServerError() throws MessageSendException {
        MessageHandler messageHandler = mock(MessageHandler.class);
        when(context.checkParamExists(any(List.class))).thenReturn(true);
        doThrow(MessageSendException.class).when(messageHandler).send(any(Message.class), any(ClientConfiguration.class));

        List<MessageHandler> handlers = new ArrayList<>();
        handlers.add(messageHandler);
        contactHandler.setMessageHandlers(handlers);

        contactHandler.Handle(context);

        verify(context).serverError();
    }

    @Test
    public void checkValidationErrorReturnsToUser()  {
        when(context.checkParamExists(any(List.class))).thenReturn(true);
        when(context.getFormParameter("email")).thenReturn("notavalidemail");

        contactHandler.Handle(context);

        verify(context).setStatus(400);
        verify(context).result("Invalid 'senderAddress' Email Supplied (notavalidemail)");
    }

    @Test
    public void checkRateLimitErrorWhenRateLimitHit() {
        when(context.getIp()).thenReturn(RATELIMIT_IP_DENY);
        when(context.checkParamExists(any(List.class))).thenReturn(true);

        contactHandler.Handle(context);

        verify(context).setStatus(429);
        verify(context).result("Rate Limit Exceeded");
    }

}