package au.telegraph.http.client;

public class HttpResponse {
    private Integer statusCode;
    private String responseData;

    public HttpResponse(Integer statusCode, String responseData) {
        this.statusCode = statusCode;
        this.responseData = responseData;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }
}
