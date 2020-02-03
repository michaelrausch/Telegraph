package nz.rausch.contact.http.ratelimiter;

public interface Clock {
    long getUnixTimestamp();
}
