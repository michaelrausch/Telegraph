package nz.rausch.contact.http.ratelimiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RateLimiter {
    private final RateLimiterConfiguration configuration;
    private final Clock clock;
    private final Map<String, AccessLogEntry> accessLog;
    private Logger logger = LoggerFactory.getLogger("RateLimiter");

    public RateLimiter(RateLimiterConfiguration configuration, Clock clock) {
        this.clock = clock;
        this.configuration = configuration;
        this.accessLog = new HashMap<>();
        logger.debug("New RateLimiter Configured");
    }

    public Boolean shouldAllowAccess(String ip) {
        long time = clock.getUnixTimestamp();
        AccessLogEntry entry = accessLog.get(ip);

        logger.debug("RateLimiter received request from " + ip + " at " + clock.getUnixTimestamp());

        if (entry == null) {
            logger.debug("Client " + ip + " never seen before, should be allowed");
            accessLog.put(ip, new AccessLogEntry(time));
            return true;
        }

        if (time - entry.getLastAccess() <= configuration.getCooldown()){
            logger.debug("Client " + ip + " hasn't let their cooldown expire, request should be blocked");
            return false;
        }

        logger.debug("Client " + ip + " has been seen before, but their cooldown has expired. Request should be allowed");
        logger.debug("Updating record for " + ip);

        entry.setLastAccess(time);
        accessLog.replace(ip, entry);

        return true;
    }
}
