package nz.rausch.contact.http.ratelimiter;

import java.util.HashMap;
import java.util.Map;

public class RateLimiter {
    private final RateLimiterConfiguration configuration;
    private final Clock clock;
    private final Map<String, AccessLogEntry> accessLog;

    public RateLimiter(RateLimiterConfiguration configuration, Clock clock) {
        this.clock = clock;
        this.configuration = configuration;
        this.accessLog = new HashMap<>();
    }

    public Boolean shouldAllowAccess(String ip) {
        long time = clock.getUnixTimestamp();
        AccessLogEntry entry = accessLog.get(ip);

        if (entry == null) {
            accessLog.put(ip, new AccessLogEntry(time));
            return true;
        }

        if (time - entry.getLastAccess() <= configuration.getCooldown()){
            return false;
        }

        entry.setLastAccess(time);
        accessLog.replace(ip, entry);

        return true;
    }
}
