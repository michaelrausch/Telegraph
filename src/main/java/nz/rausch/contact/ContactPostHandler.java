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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ContactPostHandler implements HttpHandler {
    private static final String NAME = "name";
    private static final String MESSAGE = "message";
    private static final String EMAIL = "email";

    private final AppConfig configuration;
    private final RateLimiter rateLimiter;
    private final List<MessageHandler> messageHandlers;
    private static Logger logger = LoggerFactory.getLogger(ContactPostHandler.class.getName());

    public ContactPostHandler(AppConfig config, RateLimiter rateLimiter){
        this.configuration = config;
        this.messageHandlers = new ArrayList<>();
        this.rateLimiter = rateLimiter;
    }

    @Override
    public void Handle(HttpContext ctx) {
        List<String> requiredParams = getRequiredMessageParameters();
        Message message = new Message();

        logger.debug("New Request from IP " + ctx.getIp());

        // Check required params exist
        if (!ctx.checkParamExists(requiredParams)) {
            logger.debug("Request missing required parameters ");
            ctx.badRequest();
            return;
        }

        // Try creating message object
        try {
            message.setName(ctx.getFormParameter(NAME))
                    .setMessage(ctx.getFormParameter(MESSAGE))
                    .setSenderAddress(ctx.getFormParameter(EMAIL));
        } catch (ValidationException e){
            logger.debug("Request validation failed ");
            ctx.setStatus(400);
            ctx.result(e.getMessage());
            return;
        }

        if (!rateLimiter.shouldAllowAccess(ctx.getIp())){
            ctx.setStatus(429);
            ctx.result("Rate Limit Exceeded");
            logger.info("Request from IP " + ctx.getIp() + " blocked, rate limit exceeded");
            return;
        }

        // Try running message handlers
        for (MessageHandler handler : messageHandlers) {
            try {
                handler.send(message);
                logger.debug("Message forwarded to handler " + handler.toString());
            } catch (MessageSendException e) {
                ctx.serverError();
                logger.error("Handler threw exception " + e.getMessage());
                return;
            }
        }

        ctx.ok();
    }

    /**
     * Replaces the current list of message handlers with the ones specified
     * @param messageHandlers A list of MessageHandlers
     */
    public void setMessageHandlers(List<MessageHandler> messageHandlers) {
        logger.debug("Setting new message handlers ");

        this.messageHandlers.clear();
        this.messageHandlers.addAll(messageHandlers);
    }

    /**
     * Add a message handler
     * @param messageHandler The message handler
     */
    public void addMessageHandler(MessageHandler messageHandler) {
        logger.debug("Adding new message handler {}", messageHandler.getClass().getName());
        this.messageHandlers.add(messageHandler);
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
