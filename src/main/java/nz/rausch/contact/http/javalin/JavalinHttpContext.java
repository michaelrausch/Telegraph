package nz.rausch.contact.http.javalin;

import io.javalin.http.Context;
import nz.rausch.contact.http.HttpContext;

public class JavalinHttpContext extends HttpContext {
    private Context context;

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
        this.result(json);
        return this;
    }

}
