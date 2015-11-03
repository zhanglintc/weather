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
    String get(String url) {
        HttpGet getMethod = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        String resultContent = null;
        int resultCode = 0;

        try {
            HttpResponse response = httpClient.execute(getMethod);
            resultContent = EntityUtils.toString(response.getEntity(), "utf-8");
            resultCode = response.getStatusLine().getStatusCode();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

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