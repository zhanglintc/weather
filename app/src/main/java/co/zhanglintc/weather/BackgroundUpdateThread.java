package co.zhanglintc.weather;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import co.zhanglintc.weather.common.WeatherUtils;

/**
 * Created by zhanglin on 2015/11/06.
 */
public class BackgroundUpdateThread extends Thread {
    Bitmap bmImg;
    String APIurl = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=chongqing&nu%20m_of_days=3&format=json&lang=zh";
    TextView todayTemp;
    TextView todayStatus;
    ImageView imView;
    Activity activity;
    String rawJsonData;

    BackgroundUpdateThread(Activity activity) {
        this.activity = activity;
        this.todayTemp = (TextView) activity.findViewById(R.id.todayTemp);
        this.todayStatus = (TextView) activity.findViewById(R.id.todayStatus);
        this.imView = (ImageView) activity.findViewById(R.id.imview);
        rawJsonData = activity.getResources().getString(R.string.rawJsonData);
    }

    public void run() {
        // true process
        httpHandler http = new httpHandler();
        String s = http.get(APIurl);

        // fake process
            /*
            String s = rawJsonData;
            try {
                sleep(1000 * 2);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            */
        final String temp_C;
        final String weatherDesc;
        final String iconURL;
        try {
            // final JSONObject json = new JSONObject(new JSONTokener(s));
            WeatherParser we = new WeatherParser(s);
            temp_C = we.getCurTemp_C();
            weatherDesc = we.getCurWeatherDesc();
            iconURL = we.getCurWeatherIconUrl();
            bmImg  = WeatherUtils.returnBitMap(iconURL);
            Log.i("http", "Current temperature: " + temp_C);
            Log.i("http", "Current condition: " + weatherDesc);
            Log.i("http", "Current weather icon URL: " + iconURL);

            activity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            todayTemp.setText("Current temperature: " + temp_C);
                            todayStatus.setText("Current status: " + weatherDesc);
                            imView.setImageBitmap(bmImg);
                        }
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
