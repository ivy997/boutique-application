package boutique.models;

public class MessageResponse {
    private String stackTrace;
    private String message;

    public MessageResponse(String message)
    {
        this.message = message;
    }

    public MessageResponse(String message, String stackTrace)
    {
        this(message);

        this.stackTrace = stackTrace;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
