package au.telegraph;

import au.telegraph.http.HttpServer;
import au.telegraph.http.client.apacheclient.ApacheHttpClient;
import au.telegraph.messaging.ifttt.IftttMessageHandler;
import ch.qos.logback.classic.Logger;
import au.telegraph.configuration.AppConfigLoader;
import au.telegraph.configuration.exception.ConfigLoadException;
import au.telegraph.configuration.models.AppConfig;
import au.telegraph.http.javalin.JavalinHttpServer;
import au.telegraph.http.ratelimiter.RateLimiter;
import au.telegraph.http.ratelimiter.SystemClock;
import au.telegraph.messaging.ConsoleMessageHandler;
import au.telegraph.messaging.mailjet.MailjetMessageHandler;
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

        logger.info("Configuration Port: {}", configuration.getPort());
        logger.info("Rate limiter cooldown: {} seconds", configuration.getRateLimiter().getCooldown());
        logger.info("{} clients registered", configuration.getClientList().size());

        HttpServer server = new JavalinHttpServer(configuration.getPort());
        RateLimiter rateLimiter = new RateLimiter(configuration.getRateLimiter(), new SystemClock());

        ContactPostHandler contactPostHandler = new ContactPostHandler(configuration, rateLimiter);

        contactPostHandler.addMessageHandler(new MailjetMessageHandler(configuration.getMailjetConfig()));
        contactPostHandler.addMessageHandler(new IftttMessageHandler(configuration.getIfttt(), new ApacheHttpClient()));
        contactPostHandler.addMessageHandler(new ConsoleMessageHandler());

        server.post(PATH, contactPostHandler);
        server.get("/telemetry", new TelemetryHandler(configuration));

        logger.info("Starting HTTP Server");
        server.start();
    }

    public static void displayLogo() {
        System.out.println("\n\n  ______     __                            __  \n" +
                " /_  __/__  / /__  ____ __________ _____  / /_ \n" +
                "  / / / _ \\/ / _ \\/ __ `/ ___/ __ `/ __ \\/ __ \\\n" +
                " / / /  __/ /  __/ /_/ / /  / /_/ / /_/ / / / /\n" +
                "/_/  \\___/_/\\___/\\__, /_/   \\__,_/ .___/_/ /_/ \n" +
                "                /____/          /_/            \n\n");
    }
}
