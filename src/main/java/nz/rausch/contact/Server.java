package nz.rausch.contact;

import nz.rausch.contact.http.HttpServer;
import nz.rausch.contact.http.javalin.JavalinHttpServer;

public class Server {
    public static void main(String... args) {
        SubmissionHandler handler = new SendgridSubmissionHandler();
        HttpServer server = new JavalinHttpServer(8000);
        server.start();

        server.get("/", ctx -> {
            handler.setMessage(ctx.getFormParameter("message"));
            handler.setSenderEmail(ctx.getFormParameter("email"));
            handler.setSenderName(ctx.getFormParameter("name"));

            handler.submit();
        });
    }

}
