package nz.rausch.contact.configuration.models;

import nz.rausch.contact.http.ratelimiter.RateLimiterConfiguration;

public class AppConfig {
    private String toEmail;
    private String toName;
    private MailjetConfig mailjetConfig;
    private RateLimiterConfiguration rateLimiter;
    private Integer port;

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public MailjetConfig getMailjetConfig() {
        return mailjetConfig;
    }

    public void setMailjetConfig(MailjetConfig mailjetConfig) {
        this.mailjetConfig = mailjetConfig;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public RateLimiterConfiguration getRateLimiter() {
        return rateLimiter;
    }

    public void setRateLimiter(RateLimiterConfiguration rateLimiter) {
        this.rateLimiter = rateLimiter;
    }
}
