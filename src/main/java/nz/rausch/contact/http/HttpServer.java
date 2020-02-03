package nz.rausch.contact.http;

public abstract class HttpServer {
    private final Integer port;

    public HttpServer(Integer port) {
        this.port = port;
    }

    /**
     * Start the server on the previously specified port
     */
    public abstract void start();

    /**
     * Add a HTTP GET request handler
     * @param path The path to bind the handler to
     * @param handler A handler to handle the request
     */
    public abstract void get(String path, HttpHandler handler);

    /**
     * Add a HTTP POST request handler
     * @param path The path to bind the handler to
     * @param handler A handler to handle the request
     */
    public abstract void post(String path, HttpHandler handler);

    /**
     * Get the port the server is bound to
     * @return The port
     */
    protected Integer getPort() {
        return port;
    }
}
