package nz.rausch.contact;

import nz.rausch.contact.configuration.loader.TestConfig;
import nz.rausch.contact.configuration.loader.YAMLConfigLoaderTest;
import nz.rausch.contact.configuration.models.AppConfig;
import nz.rausch.contact.http.HttpContext;
import nz.rausch.contact.http.HttpHandler;
import nz.rausch.contact.messaging.Message;
import nz.rausch.contact.messaging.MessageHandler;
import nz.rausch.contact.messaging.exceptions.MessageSendException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ContactPostHandlerTest {
    private ContactPostHandler contactHandler;
    private AppConfig testConfig;
    private HttpContext context;

    @Before
    public void setup() {
        context = mock(HttpContext.class);

        // Setup test config
        testConfig = new AppConfig();
        testConfig.setToEmail("test@example.com");

        // Create contact handler
        contactHandler = new ContactPostHandler(testConfig);

        // Setup HttpContext mock
        when(context.getFormParameter("name")).thenReturn("Michael");
        when(context.getFormParameter("email")).thenReturn("me@me.com");
        when(context.getFormParameter("message")).thenReturn("This is a message");
    }

    @Test
    public void checkMessageAcceptsWhenValid() {
        when(context.checkParamExists(any(List.class))).thenReturn(true);

        contactHandler.Handle(context);

        verify(context).accept();
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

        verify(messageHandler).send(any(Message.class));
    }

    @Test
    public void checkMessageFailureReturnsServerError() throws MessageSendException {
        MessageHandler messageHandler = mock(MessageHandler.class);
        when(context.checkParamExists(any(List.class))).thenReturn(true);
        doThrow(MessageSendException.class).when(messageHandler).send(any(Message.class));

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
}