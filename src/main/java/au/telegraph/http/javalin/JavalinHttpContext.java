package au.telegraph.http.javalin;

import au.telegraph.http.HttpContext;
import io.javalin.http.Context;

public class JavalinHttpContext extends HttpContext {
    private final Context context;

    public JavalinHttpContext(Context context) {
        this.context = context;
    }

    @Override
    public String getIp() {
        return context.ip();
    }

    @Override
    public String getFormParameter(String param) {
        return context.formParam(param);
    }

    @Override
    public HttpContext result(String resultString) {
        getTelemetryData().logOk();
        context.result(resultString);
        return this;
    }

    @Override
    public HttpContext setStatus(Integer status) {
        context.status(status);
        return this;
    }

    @Override
    public HttpContext contentType(String contentType) {
        context.contentType(contentType);
        return this;
    }

    @Override
    public HttpContext resultJson(String json) {
        this.contentType("application/json");
        getTelemetryData().logOk();
        this.result(json);
        return this;
    }

}
