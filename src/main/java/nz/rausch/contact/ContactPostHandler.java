package nz.rausch.contact;

import nz.rausch.contact.configuration.models.AppConfig;
import nz.rausch.contact.http.HttpContext;
import nz.rausch.contact.http.HttpHandler;
import nz.rausch.contact.http.ratelimiter.RateLimiter;
import nz.rausch.contact.messaging.ConsoleMessageHandler;
import nz.rausch.contact.messaging.Message;
import nz.rausch.contact.messaging.MessageHandler;
import nz.rausch.contact.messaging.exceptions.MessageSendException;
import nz.rausch.contact.messaging.exceptions.ValidationException;
import nz.rausch.contact.messaging.mailjet.MailjetMessageHandler;

import java.util.ArrayList;
import java.util.List;

public class ContactPostHandler implements HttpHandler {
    private static final String NAME = "name";
    private static final String MESSAGE = "message";
    private static final String EMAIL = "email";
    private final AppConfig configuration;
    private final RateLimiter rateLimiter;
    private final List<MessageHandler> messageHandlers;

    public ContactPostHandler(AppConfig config, RateLimiter rateLimiter){
        this.configuration = config;
        this.messageHandlers = new ArrayList<>();
        this.rateLimiter = rateLimiter;
    }

    @Override
    public void Handle(HttpContext ctx) {
        List<String> requiredParams = getRequiredMessageParameters();
        Message message = new Message();

        if (!rateLimiter.shouldAllowAccess(ctx.getIp())){
            ctx.setStatus(429);
            ctx.result("Rate Limit Exceeded");
            return;
        }

        // Check required params exist
        if (!ctx.checkParamExists(requiredParams)) {
            ctx.badRequest();
            return;
        }

        // Try creating message object
        try {
            message.setName(ctx.getFormParameter(NAME))
                    .setMessage(ctx.getFormParameter(MESSAGE))
                    .setSenderAddress(ctx.getFormParameter(EMAIL))
                    .setToAddress(configuration.getToEmail());
        } catch (ValidationException e){
            ctx.setStatus(400);
            ctx.result(e.getMessage());
            return;
        }

        // Try running message handlers
        for (MessageHandler handler : messageHandlers) {
            try {
                handler.send(message);
            } catch (MessageSendException e) {
                ctx.serverError();
                return;
            }
        }

        ctx.accept();
    }

    /**
     * Clears all current MessageHandlers and sets the default ones (Console and Mailjet)
     */
    public void setDefaultMessageHandlers() {
        this.messageHandlers.clear();
        messageHandlers.add(new ConsoleMessageHandler());
        messageHandlers.add(new MailjetMessageHandler());
    }

    /**
     * Replaces the current list of message handlers with the ones specified
     * @param messageHandlers A list of MessageHandlers
     */
    public void setMessageHandlers(List<MessageHandler> messageHandlers) {
        this.messageHandlers.clear();
        this.messageHandlers.addAll(messageHandlers);
    }

    /**
     * Returns a list of parameters required to be present in the form data
     * @return A List of required parameters
     */
    private List<String> getRequiredMessageParameters() {
        List<String> requiredParams = new ArrayList<>();

        requiredParams.add(NAME);
        requiredParams.add(EMAIL);
        requiredParams.add(MESSAGE);

        return requiredParams;
    }
}
