package co.zhanglintc.weather;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ant.liao.GifView;

import org.json.JSONException;

import co.zhanglintc.weather.common.WeatherUtils;

/**
 * Created by zhanglin on 2015/11/06.
 */
public class BGupdater extends Thread {
    Activity activity;

    TextView cityNameView;
    TextView curTimeView;
    TextView curTempCView;
    TextView curDescView;
    TextView curDateView;
    TextView nd1WeekView;
    TextView nd1TempCView;
    TextView nd2WeekView;
    TextView nd2TempCView;
    TextView nd3WeekView;
    TextView nd3TempCView;

    Bitmap curDescIcon;
    // ImageView curDescIconView;
    // GifView rfsIconView;

    String rawJsonData;
    String sysLang;
    String APIurl;

    String curTempC;
    String curDesc;
    String cityName;

    String nd1MaxTempC;
    String nd1MinTempC;
    String nd2MaxTempC;
    String nd2MinTempC;
    String nd3MaxTempC;
    String nd3MinTempC;

    BGupdater(Activity activity) {
        this.activity = activity;
        this.APIurl = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=chongqing&nu%20m_of_days=3&format=json&lang=" + WeatherUtils.getLanguge();
        rawJsonData = activity.getResources().getString(R.string.rawJsonData);
    }

    public void run() {
        Log.i("http", "Getting weather data...");
        httpHandler http = new httpHandler();
        String retVal = http.get(APIurl);

        try {
            WeatherParser wp = new WeatherParser(retVal);

            curTempC = wp.getCurTemp_C();
            cityName = wp.getRequestCity();
            nd1MaxTempC = wp.getNextNthDayMaxTempC(1);
            nd1MinTempC = wp.getNextNthDayMinTempC(1);
            nd2MaxTempC = wp.getNextNthDayMaxTempC(2);
            nd2MinTempC = wp.getNextNthDayMinTempC(2);
            nd3MaxTempC = wp.getNextNthDayMaxTempC(3);
            nd3MinTempC = wp.getNextNthDayMinTempC(3);

            curDescIcon  = WeatherUtils.returnBitMap(wp.getCurWeatherIconUrl());

            sysLang = WeatherUtils.getLanguge();
            if ("en".equals(sysLang)) {
                curDesc = wp.getCurWeatherDesc();
            }
            else {
                curDesc = wp.getCurWeatherDescTranslation(sysLang);
            }

            Log.i("http", "Current temperature: " + wp.getCurTemp_C());
            Log.i("http", "Current condition: " + curDesc);
            Log.i("http", "Current weather icon URL: " + wp.getCurWeatherIconUrl());
            Log.i("http", "Requested city: " + wp.getRequestCity());
            // 登录: http://www.worldweatheronline.com
            // 选择 FULL FORECAST 确认数据正确性
            Log.i("http", "Tomorrow date: " + wp.getNextNthDayDate(1));
            Log.i("http", "Tomorrow high: " + wp.getNextNthDayMaxTempC(1));
            Log.i("http", "Tomorrow low: "  + wp.getNextNthDayMinTempC(1));
            Log.i("http", "Tomorrow condition: "  + wp.getNextNthDayWeatherDesc(1));
            Log.i("http", "Day after tomorrow date: " + wp.getNextNthDayDate(2));
            Log.i("http", "Day after tomorrow high: " + wp.getNextNthDayMaxTempC(2));
            Log.i("http", "Day after tomorrow low: "  + wp.getNextNthDayMinTempC(2));
            Log.i("http", "Day after tomorrow condition: " + wp.getNextNthDayWeatherDesc(2));

            activity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            activity.setContentView(R.layout.activity_main);

                            // TextView
                            cityNameView = (TextView) activity.findViewById(R.id.cityName);
                            curTimeView = (TextView) activity.findViewById(R.id.curTime);
                            curTempCView = (TextView) activity.findViewById(R.id.curTempC);
                            curDescView = (TextView) activity.findViewById(R.id.curDesc);

                            nd1WeekView = (TextView) activity.findViewById(R.id.nextDay1Week);
                            nd1TempCView = (TextView) activity.findViewById(R.id.nextDay1TempC);

                            nd2WeekView = (TextView) activity.findViewById(R.id.nextDay2Week);
                            nd2TempCView = (TextView) activity.findViewById(R.id.nextDay2TempC);

                            nd3WeekView = (TextView) activity.findViewById(R.id.nextDay3Week);
                            nd3TempCView = (TextView) activity.findViewById(R.id.nextDay3TempC);

                            curDateView = (TextView) activity.findViewById(R.id.curDate);

                            // ImageView
                            // curDescIconView = (ImageView) activity.findViewById(R.id.curDescIcon);

                            // rfsIconView = (GifView) activity.findViewById(R.id.rfsIcon);

                            // ------------------------------------------------------------------

                            // Set Text
                            cityNameView.setText(cityName);
                            curTimeView.setText(WeatherUtils.getSysTime());
                            curTempCView.setText(curTempC + "°C");
                            curDescView.setText(curDesc);
                            curDateView.setText(WeatherUtils.getSysDate());

                            nd1TempCView.setText(nd1MinTempC + "~" + nd1MaxTempC + "°C");
                            nd2TempCView.setText(nd2MinTempC + "~" + nd2MaxTempC + "°C");
                            nd3TempCView.setText(nd3MinTempC + "~" + nd3MaxTempC + "°C");

                            // Set Image
                            // curDescIconView.setImageBitmap(curDescIcon);

                            // rfsIconView.setGifImage(R.mipmap.ic_launcher); // anything here can disable gif display
                        }
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
