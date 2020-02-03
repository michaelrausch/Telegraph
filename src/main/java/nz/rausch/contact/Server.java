package nz.rausch.contact;

import nz.rausch.contact.configuration.AppConfigLoader;
import nz.rausch.contact.configuration.exception.ConfigLoadException;
import nz.rausch.contact.configuration.models.AppConfig;
import nz.rausch.contact.http.HttpServer;
import nz.rausch.contact.http.javalin.JavalinHttpServer;
import nz.rausch.contact.http.ratelimiter.RateLimiter;
import nz.rausch.contact.http.ratelimiter.SystemClock;

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
        RateLimiter rateLimiter = new RateLimiter(configuration.getRateLimiter(), new SystemClock());
        server.start();
        server.post(PATH, new ContactPostHandler(configuration, rateLimiter));
    }
}
