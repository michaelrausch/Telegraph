package nz.rausch.contact;

import ch.qos.logback.classic.Logger;
import nz.rausch.contact.configuration.AppConfigLoader;
import nz.rausch.contact.configuration.exception.ConfigLoadException;
import nz.rausch.contact.configuration.models.AppConfig;
import nz.rausch.contact.http.HttpServer;
import nz.rausch.contact.http.client.apacheclient.ApacheHttpClient;
import nz.rausch.contact.http.javalin.JavalinHttpServer;
import nz.rausch.contact.http.ratelimiter.RateLimiter;
import nz.rausch.contact.http.ratelimiter.SystemClock;
import nz.rausch.contact.messaging.ifttt.IftttMessageHandler;
import nz.rausch.contact.messaging.mailjet.MailjetMessageHandler;
import org.slf4j.LoggerFactory;


public class Server {
    private static final String PATH = "/";
    private static Logger logger = (Logger) LoggerFactory.getLogger(Server.class.getName());

    public static void main(String... args) {
        AppConfig configuration;

        displayLogo();

        try {
            configuration = new AppConfigLoader().load(AppConfig.class).getConfig();
            logger.info("Configuration Loaded OK");
        } catch (ConfigLoadException e) {
            e.printStackTrace();
            logger.error("Error Loading Configuration");
            return;
        }

        logger.info("--- Configuration Port: {}", configuration.getPort());
        logger.info("--- Rate limiter cooldown: {} seconds", configuration.getRateLimiter().getCooldown());

        HttpServer server = new JavalinHttpServer(configuration.getPort());
        RateLimiter rateLimiter = new RateLimiter(configuration.getRateLimiter(), new SystemClock());

        ContactPostHandler contactPostHandler = new ContactPostHandler(configuration, rateLimiter);

        contactPostHandler.addMessageHandler(new MailjetMessageHandler(configuration.getMailjetConfig()));
        contactPostHandler.addMessageHandler(new IftttMessageHandler(configuration.getIfttt(), new ApacheHttpClient()));

        server.post(PATH, contactPostHandler);

        server.start();
    }

    public static void displayLogo() {
        System.out.println("  /$$$$$$                        /$$                           /$$            /$$$$$$  /$$$$$$$  /$$$$$$\n" +
                " /$$__  $$                      | $$                          | $$           /$$__  $$| $$__  $$|_  $$_/\n" +
                "| $$  \\__/  /$$$$$$  /$$$$$$$  /$$$$$$    /$$$$$$   /$$$$$$$ /$$$$$$        | $$  \\ $$| $$  \\ $$  | $$  \n" +
                "| $$       /$$__  $$| $$__  $$|_  $$_/   |____  $$ /$$_____/|_  $$_/        | $$$$$$$$| $$$$$$$/  | $$  \n" +
                "| $$      | $$  \\ $$| $$  \\ $$  | $$      /$$$$$$$| $$        | $$          | $$__  $$| $$____/   | $$  \n" +
                "| $$    $$| $$  | $$| $$  | $$  | $$ /$$ /$$__  $$| $$        | $$ /$$      | $$  | $$| $$        | $$  \n" +
                "|  $$$$$$/|  $$$$$$/| $$  | $$  |  $$$$/|  $$$$$$$|  $$$$$$$  |  $$$$/      | $$  | $$| $$       /$$$$$$\n" +
                " \\______/  \\______/ |__/  |__/   \\___/   \\_______/ \\_______/   \\___/        |__/  |__/|__/      |______/\n" +
                "                                                                                                        \n" +
                "                                                                                                        \n" +
                "                                                                                                        ");
    }
}
