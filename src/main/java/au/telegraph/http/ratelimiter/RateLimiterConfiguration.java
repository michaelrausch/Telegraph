package au.telegraph.http.ratelimiter;

public class RateLimiterConfiguration {
    private long cooldown;

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }
}
