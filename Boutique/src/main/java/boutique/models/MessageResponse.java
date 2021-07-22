package boutique.models;

import java.util.Date;

public class MessageResponse {
    private String stackTrace;
    private String message;
    private Integer httpStatusCode;
    private Date date;
    private String description;

    public MessageResponse(String message)
    {
        this.message = message;
    }

    public MessageResponse(String message, String stackTrace)
    {
        this(message);

        this.stackTrace = stackTrace;
    }

    public MessageResponse(Integer httpStatusCode, Date date, String message, String description) {
        this(message);

        this.httpStatusCode = httpStatusCode;
        this.date = date;
        this.description = description;
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

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
