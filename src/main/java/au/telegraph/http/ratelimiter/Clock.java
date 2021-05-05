package au.telegraph.http.ratelimiter;

public interface Clock {
    long getUnixTimestamp();
}
