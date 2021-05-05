package nz.rausch.contact.http;

import java.time.Instant;
import java.util.Date;

public class TelemetryData {
    private static TelemetryData instance;

    private Integer requests = 0;
    private Integer failed = 0;
    private Date lastUpdate = Date.from(Instant.now());

    public static TelemetryData getInstance() {
        if (instance == null) {
            instance = new TelemetryData();
        }

        return instance;
    }

    public String getTelemetryDataString() {
        return " r=" + requests.toString() + " f=" + failed.toString() + " l=" + lastUpdate.toString();
    }

    public void logOk() {
        requests += 1;
        lastUpdate = Date.from(Instant.now());
    }

    public void logFailed() {
        failed += 1;
        lastUpdate = Date.from(Instant.now());
    }
}
