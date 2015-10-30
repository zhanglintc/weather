package co.zhanglintc.weather;

import android.util.Log;

public class httpHandler {
    String APIurl = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=chongqing&nu%20m_of_days=3&format=json&lang=zh";

    void logSth() {
        Log.i("httpHandler", APIurl);
    }

    // TODO: 10/30/15 connect APIurl using httpClient
}