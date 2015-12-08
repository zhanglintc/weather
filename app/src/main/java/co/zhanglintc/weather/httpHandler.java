package co.zhanglintc.weather;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by lane on 11/1/15.
 * Handle network things.
 */
public class httpHandler {
    /**
     * Created by lane on 11/1/15.
     * Return http server respond.
     */
    String get(String url) throws IOException {
        HttpGet getMethod = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();

        HttpResponse response = httpClient.execute(getMethod);
        String resultContent = EntityUtils.toString(response.getEntity(), "utf-8");
        int resultCode = response.getStatusLine().getStatusCode();

        // return content if respond code between 200 and 300
        if (200 <= resultCode && resultCode < 300) {
            return resultContent;
        }

        // else return respond code
        else {
            return Integer.toString(resultCode);
        }
    }
}