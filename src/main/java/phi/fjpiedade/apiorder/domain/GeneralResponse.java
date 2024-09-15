package phi.fjpiedade.apiorder.domain;

import java.time.LocalDateTime;
import java.util.Map;

public class GeneralResponse {
//    protected LocalDateTime timeStamp;
//    protected int statusCode;
////    protected HttpStatus status;
//    protected String reason;
//    protected String message;
//    protected String developerMessage;
//    protected Map<?, ?> data;

    private LocalDateTime timeStamp;
    private int statusCode;
    private String status;
    private String message;
    private Object data;

    public GeneralResponse(int statusCode, String status, String message, Object data) {
        this.timeStamp = LocalDateTime.now();
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
