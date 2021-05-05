package au.telegraph.http.javalin;

import au.telegraph.http.HttpHandler;
import au.telegraph.http.HttpServer;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

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
