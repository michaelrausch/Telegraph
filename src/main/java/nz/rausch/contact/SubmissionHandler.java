package nz.rausch.contact;

public abstract class SubmissionHandler {
    private String message;
    private String senderName;
    private String senderEmailAddress;

    public SubmissionHandler() {
         message = "";
         senderEmailAddress = "";
         senderName = "";
    }

    public SubmissionHandler setMessage(String message) {
        this.message = message;
        return this;
    }

    public SubmissionHandler setSenderEmail(String email) {
        this.senderEmailAddress = email;
        return this;
    }

    public SubmissionHandler setSenderName(String name) {
        this.senderName = name;
        return this;
    }

    public abstract void submit();
}
