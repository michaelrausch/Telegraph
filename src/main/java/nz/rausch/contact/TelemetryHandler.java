package nz.rausch.contact;

import nz.rausch.contact.configuration.models.AppConfig;
import nz.rausch.contact.http.HttpContext;
import nz.rausch.contact.http.HttpHandler;
import nz.rausch.contact.http.TelemetryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TelemetryHandler implements HttpHandler {
    private static Logger logger = LoggerFactory.getLogger(TelemetryHandler.class.getName());
    private AppConfig config;

    public TelemetryHandler(AppConfig configuration) {
        config = configuration;
    }

    @Override
    public void Handle(HttpContext ctx) {
        String resultString = "OK c=";

        Integer clientListCount = config.getClientList().size();

        resultString += clientListCount.toString();
        resultString += TelemetryData.getInstance().getTelemetryDataString();

        ctx.result(resultString);
    }
}
