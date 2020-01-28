package nz.rausch.contact;

import nz.rausch.contact.configuration.AppConfigLoader;
import nz.rausch.contact.configuration.exception.ConfigLoadException;
import nz.rausch.contact.configuration.loader.ConfigLoader;
import nz.rausch.contact.configuration.models.AppConfig;
import nz.rausch.contact.http.HttpContext;
import nz.rausch.contact.http.HttpServer;
import nz.rausch.contact.http.javalin.JavalinHttpServer;
import nz.rausch.contact.messaging.*;
import nz.rausch.contact.messaging.exceptions.MessageSendException;
import nz.rausch.contact.messaging.exceptions.ValidationException;
import nz.rausch.contact.messaging.mailjet.MailjetMessageHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final String NAME = "name";
    private static final String MESSAGE = "message";
    private static final String EMAIL = "email";
    private static final String PATH = "/";

    private static AppConfig configuration;

    public static void main(String... args) {
        try {
            configuration = new AppConfigLoader().load(AppConfig.class).getConfig();
        } catch (ConfigLoadException e) {
            e.printStackTrace();
            return;
        }

        HttpServer server = new JavalinHttpServer(configuration.getPort());
        server.start();
        server.post(PATH, Server::handleMessage);
    }

    private static void handleMessage(HttpContext ctx) {
        List<String> requiredParams = new ArrayList<>();
        List<MessageHandler> handlers = new ArrayList<>();

        Message message = new Message();

        requiredParams.add(NAME);
        requiredParams.add(EMAIL);
        requiredParams.add(MESSAGE);

        handlers.add(new ConsoleMessageHandler());
        handlers.add(new MailjetMessageHandler());

        if (!ctx.checkParamExists(requiredParams)) {
            ctx.badRequest();
            return;
        }

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

        for (MessageHandler handler : handlers) {
            try {
                handler.send(message);
            } catch (MessageSendException e) {
                e.printStackTrace();
                ctx.serverError();
                return;
            }
        }

        ctx.accept();
    }
}
