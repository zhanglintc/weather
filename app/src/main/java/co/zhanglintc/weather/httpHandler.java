package co.zhanglintc.weather;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class httpHandler {
    // TODO: 10/31/15 use thread to handle network things

    String get(String url) {
        HttpGet getMethod = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        String resultContent = null;
        HttpResponse response;

        try {
            response = httpClient.execute(getMethod);
            resultContent = EntityUtils.toString(response.getEntity(), "utf-8");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return resultContent;
    }

    /* remove this code in the future
    void get() {
        String baseUrl = "http://zhanglintc.co/JS-Prac";

        HttpGet getMethod = new HttpGet(baseUrl);
        HttpClient httpClient = new DefaultHttpClient();

        try {
            HttpResponse response = httpClient.execute(getMethod);

            Log.i("http", "resCode = " + response.getStatusLine().getStatusCode());
            Log.i("http", "result = " + EntityUtils.toString(response.getEntity(), "utf-8"));
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
}