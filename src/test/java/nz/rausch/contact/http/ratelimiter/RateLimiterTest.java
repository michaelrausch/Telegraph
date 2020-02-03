package nz.rausch.contact.http.ratelimiter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RateLimiterTest {
    private Clock clock;
    private RateLimiter rateLimiter;
    private final long COOLDOWN = 100;

    @Before
    public void setUp() {
        clock = mock(Clock.class);

        RateLimiterConfiguration config = new RateLimiterConfiguration();
        config.setCooldown(COOLDOWN);

        rateLimiter = new RateLimiter(config, clock);
    }

    @Test
    public void checkRateLimiterAllowsFirstTimeAccess() {
        when(clock.getUnixTimestamp()).thenReturn(1000L);

        Boolean result = rateLimiter.shouldAllowAccess("10.10.10.10");
        assertEquals(true, result);
    }

    @Test
    public void checkTwoAttemptsWithinCooldownNotAllowed() {
        when(clock.getUnixTimestamp()).thenReturn(1000L);
        rateLimiter.shouldAllowAccess("10.10.10.10");

        when(clock.getUnixTimestamp()).thenReturn(1005L);
        Boolean result = rateLimiter.shouldAllowAccess("10.10.10.10");
        assertEquals(false, result);
    }

    @Test
    public void checkTwoAttemptsAllowedWhenCooldownExpired() {
        when(clock.getUnixTimestamp()).thenReturn(1000L);
        rateLimiter.shouldAllowAccess("10.10.10.10");

        when(clock.getUnixTimestamp()).thenReturn(1000L + COOLDOWN + 20L);
        Boolean result = rateLimiter.shouldAllowAccess("10.10.10.10");
        assertEquals(true, result);
    }
}