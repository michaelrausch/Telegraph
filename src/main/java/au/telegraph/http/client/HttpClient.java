package au.telegraph.http.client;

import java.io.Closeable;
import java.io.IOException;

public interface HttpClient extends Closeable{
    void setBody(String content);
    void setContentType(String contentType);
    void addHeader(String header, String value);
    void setUrl(String url);
    HttpResponse executePost() throws IOException;
}
