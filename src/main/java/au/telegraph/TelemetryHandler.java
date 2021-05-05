package au.telegraph;

import au.telegraph.configuration.models.AppConfig;
import au.telegraph.http.HttpContext;
import au.telegraph.http.HttpHandler;
import au.telegraph.http.TelemetryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TelemetryHandler implements HttpHandler {
    private static Logger logger = LoggerFactory.getLogger(TelemetryHandler.class.getName());
    private AppConfig config;

    public TelemetryHandler(AppConfig configuration) {
        logger.info("Loading Telemetry Handler");

        config = configuration;
    }

    @Override
    public void Handle(HttpContext ctx) {
        String logo = "\n\n  ______     __                            __  \n" +
                " /_  __/__  / /__  ____ __________ _____  / /_ \n" +
                "  / / / _ \\/ / _ \\/ __ `/ ___/ __ `/ __ \\/ __ \\\n" +
                " / / /  __/ /  __/ /_/ / /  / /_/ / /_/ / / / /\n" +
                "/_/  \\___/_/\\___/\\__, /_/   \\__,_/ .___/_/ /_/ \n" +
                "                /____/          /_/            \n\n";


        String resultString = logo + "Telegraph Telemetry Endpoint\nService HEALTHY\n\n";

        Integer clientListCount = config.getClientList().size();

        resultString += "Client Endpoints> " + clientListCount.toString() + "\n";
        resultString += TelemetryData.getInstance().getTelemetryDataString() + "\n\n=== ENDS ===";

        ctx.result(resultString);
    }
}
