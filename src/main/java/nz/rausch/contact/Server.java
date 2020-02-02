package nz.rausch.contact;

import nz.rausch.contact.configuration.AppConfigLoader;
import nz.rausch.contact.configuration.exception.ConfigLoadException;
import nz.rausch.contact.configuration.models.AppConfig;
import nz.rausch.contact.http.HttpContext;
import nz.rausch.contact.http.HttpServer;
import nz.rausch.contact.http.javalin.JavalinHttpServer;
import nz.rausch.contact.messaging.*;
import nz.rausch.contact.messaging.exceptions.MessageSendException;
import nz.rausch.contact.messaging.exceptions.ValidationException;
import nz.rausch.contact.messaging.mailjet.MailjetMessageHandler;

import java.util.ArrayList;
import java.util.List;

public class Server {
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
        server.post(PATH, new ContactPostHandler(configuration));
    }
}
