package au.telegraph.http;

import java.util.List;

/**
 * Provides access to functions for handling the request and response
 */
public abstract class HttpContext {
    private TelemetryData telemetryData = TelemetryData.getInstance();

    protected TelemetryData getTelemetryData() {
        return TelemetryData.getInstance();
    }

    /**
     * Get the IP Address from the request
     * @return The request IP address
     */
    public abstract String getIp();

    /**
     * Gets a form param if it exists, else a default value (null if not specified explicitly).
     * @param param The param name
     * @return The param value, or null
     */
    public abstract String getFormParameter(String param);

    /**
     * Check if a form parameter is included in the request
     * @param param Parameter name
     * @return true if the parameter exists, false if it doesn't
     */
    public Boolean checkParamExists(String param) {
        return getFormParameter(param) != null;
    }

    /**
     * Checks an array of form parameters to ensure they are included in the request
     * @param params A list of parameter names
     * @return bool Returns false if a single parameter is missing, otherwise returns true
     */
    public Boolean checkParamExists(List<String> params) {
        for (String param : params) {
            if (!checkParamExists(param)) return false;
        }

        return true;
    }

    /**
     * Sets the result to the specified string
     * @param resultString The response
     * @return This HTTP Context
     */
    public abstract HttpContext result(String resultString);

    /**
     * Sets the status of the HTTP response
     * @param status The status code
     * @return This HTTP Context
     */
    public abstract HttpContext setStatus(Integer status);

    /**
     * Sets the content type of the HTTP response
     * @param contentType The content type (e.g application/json)
     * @return This HTTP Context
     */
    public abstract HttpContext contentType(String contentType);

    /**
     * Sets the result to the specified JSON string and sets the content type to application/json
     * @param resultString A JSON formatted string
     * @return This HTTP Context
     */
    public abstract HttpContext resultJson(String resultString);

    /**
     * Sends a 200 status code, along with a generic server error message
     * @return This HTTP Context
     */
    public HttpContext badRequest() {
        setStatus(400);
        result("Bad Request");
        telemetryData.logFailed();
        return this;
    }

    /**
     * Sends a 200 status code, along with a generic server error message
     * @return This HTTP Context
     */
    public HttpContext ok() {
        setStatus(200);
        result("OK");
        telemetryData.logOk();
        return this;
    }

    /**
     * Sends a 200 status code, along with a generic server error message
     * @return This HTTP Context
     */
    public HttpContext serverError() {
        setStatus(500);
        result("Internal Server Error");
        telemetryData.logFailed();
        return this;
    }

    /**
     * Sends a 202 status code, along with a generic server error message.
     * This shows that the request has been accepted, but has not been processed yet and that an error has
     * the potential to occur later.
     * @return This HTTP Context
     */
    public HttpContext accept() {
        setStatus(202);
        result("OK");
        telemetryData.logOk();
        return this;
    }
}
