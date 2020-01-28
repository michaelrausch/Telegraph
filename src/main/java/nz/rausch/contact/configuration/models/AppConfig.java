package nz.rausch.contact.configuration;

public class AppConfig {
    private String toEmail;
    private String toName;
    private MailjetConfig mailjetConfig;

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
}
