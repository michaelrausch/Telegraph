package au.telegraph.http.ratelimiter;

public class SystemClock implements Clock {
    @Override
    public long getUnixTimestamp() {
        return System.currentTimeMillis() / 1000L;
    }
}
