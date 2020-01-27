package nz.rausch.contact.http;

@FunctionalInterface
public interface HttpHandler {
    void Handle(HttpContext context);
}
