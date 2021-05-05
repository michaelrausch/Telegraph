package au.telegraph.http.client.apacheclient;

import au.telegraph.http.client.HttpResponse;
import au.telegraph.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ApacheHttpClient implements HttpClient {
    private CloseableHttpClient client;
    private String url;
    private Map<String, String> headers;
    private String body;

    public ApacheHttpClient() {
        client = HttpClientBuilder.create().build();
        headers = new HashMap<>();
    }

    @Override
    public void setBody(String content) {
        this.body = content;
    }

    @Override
    public void setContentType(String contentType) {
        addHeader("content-type", contentType);
    }

    @Override
    public void addHeader(String header, String value) {
        headers.put(header, value);
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public HttpResponse executePost() throws IOException {
        HttpPost request = new HttpPost(url);

        for (String header : headers.keySet()) {
            request.setHeader(header, headers.get(header));
        }

        StringEntity body = new StringEntity(this.body);
        request.setEntity(body);

        CloseableHttpResponse response = client.execute(request);

        Scanner s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
        String resultAsString = s.hasNext() ? s.next() : "";
        Integer statusCode = response.getStatusLine().getStatusCode();

        response.close();

        return new HttpResponse(statusCode, resultAsString);
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
