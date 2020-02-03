package nz.rausch.contact.http.ratelimiter;

public class AccessLogEntry {
    private long lastAccess;

    public AccessLogEntry(long lastAccess){
        this.lastAccess = lastAccess;
    }

    public long getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(long lastAccess) {
        this.lastAccess = lastAccess;
    }
}
