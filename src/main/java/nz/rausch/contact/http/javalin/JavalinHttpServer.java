package nz.rausch.contact.http.javalin;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.core.util.Header;
import nz.rausch.contact.http.HttpHandler;
import nz.rausch.contact.http.HttpServer;

public class JavalinHttpServer extends HttpServer {
    private final Javalin app;

    public JavalinHttpServer(Integer port) {
        super(port);
        app = Javalin.create(JavalinConfig::enableCorsForAllOrigins);
    }

    @Override
    public void start() {
        app.start(this.getPort());
    }

    @Override
    public void get(String path, HttpHandler handler) {
        app.get(path, ctx -> handler.Handle(new JavalinHttpContext(ctx)));
    }

    @Override
    public void post(String path, HttpHandler handler) {
        app.post(path, ctx -> handler.Handle(new JavalinHttpContext(ctx)));
    }

}
