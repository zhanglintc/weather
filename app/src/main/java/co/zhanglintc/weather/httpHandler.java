package co.zhanglintc.weather;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class httpHandler {
    String APIurl = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=chongqing&nu%20m_of_days=3&format=json&lang=zh";

    void logSth() {
        Log.i("httpHandler", APIurl);
    }

    // TODO: 10/30/15 connect APIurl using httpClient
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}