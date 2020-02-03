package nz.rausch.contact.messaging.mailjet;

public class MailjetConfig {
    private Boolean enabled;
    private String publicKey;
    private String privateKey;
    private String recipientName;
    private String mailToAddress;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getMailToAddress() {
        return mailToAddress;
    }

    public void setMailToAddress(String mailToAddress) {
        this.mailToAddress = mailToAddress;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
