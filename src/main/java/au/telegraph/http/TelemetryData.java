package au.telegraph.http;

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
        return "Requests> " + requests.toString() + "\nFailed Requests> " + failed.toString() + "\nLast Updated> " + lastUpdate.toString();
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
