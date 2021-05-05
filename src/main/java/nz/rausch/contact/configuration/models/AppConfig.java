package nz.rausch.contact.configuration.models;

import nz.rausch.contact.http.ratelimiter.RateLimiterConfiguration;
import nz.rausch.contact.messaging.ifttt.IftttConfiguration;
import nz.rausch.contact.messaging.mailjet.MailjetConfig;

import java.util.List;

public class AppConfig {
    private MailjetConfig mailjetConfig;
    private RateLimiterConfiguration rateLimiter;
    private Integer port;
    private IftttConfiguration ifttt;
    private List<ClientConfiguration> clientList;

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

    public IftttConfiguration getIfttt() {
        return ifttt;
    }

    public void setIfttt(IftttConfiguration ifttt) {
        this.ifttt = ifttt;
    }

    public List<ClientConfiguration> getClientList() {
        return clientList;
    }

    public void setClientList(List<ClientConfiguration> clientList) {
        this.clientList = clientList;
    }
}
