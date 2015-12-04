package co.zhanglintc.weather;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import co.zhanglintc.weather.common.WeatherUtils;
import co.zhanglintc.weather.dao.CityInfo;
import co.zhanglintc.weather.dao.DBManager;
import co.zhanglintc.weather.dao.DayInfo;

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
    TextView curWeekView;
    TextView nd1WeekView;
    TextView nd1TempCView;
    TextView nd1DescView;
    TextView nd2WeekView;
    TextView nd2TempCView;
    TextView nd2DescView;
    TextView nd3WeekView;
    TextView nd3TempCView;
    TextView nd3DescView;

    String rawJsonData;
    String sysLang;
    String cqURL;
    String bjURL;
    String shURL;

    String curTempC;
    String curDesc;
    String cityName;
    String curWeek;

    String nd1Week;
    String nd2Week;
    String nd3Week;
    String nd1TempC;
    String nd2TempC;
    String nd3TempC;
    String nd1Desc;
    String nd2Desc;
    String nd3Desc;

    String sysDate;
    String sysTime;

    private DBManager mgr;

    BGupdater(Activity activity) {
        this.activity = activity;
        this.cqURL = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=chongqing&num_of_days=4&format=json&lang=" + WeatherUtils.getLanguge();
        this.bjURL = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=beijing&num_of_days=4&format=json&lang=" + WeatherUtils.getLanguge();
        this.shURL = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=shanghai&num_of_days=4&format=json&lang=" + WeatherUtils.getLanguge();
        rawJsonData = activity.getResources().getString(R.string.rawJsonData);

        mgr = new DBManager(activity);
    }

    public void run() {
        Log.i("http", "Getting weather data...");
        httpHandler http = new httpHandler();
        String cqWeather = http.get(cqURL);
//        String bjWeather = http.get(bjURL);
//        String shWeather = http.get(shURL);

        final ArrayList<DayInfo> dayInfoList = new ArrayList<DayInfo>();

        try {

            WeatherParser wp = new WeatherParser(cqWeather);

            sysLang = WeatherUtils.getLanguge();
            if ("en".equals(sysLang)) {
                // 如果是英语, 则取不翻译的数据, 而且未来星期使用[短格式]
                curDesc = wp.getCurWeatherDesc(WeatherUtils.DO_NOT_TRANSLATE);

                nd1Week = WeatherUtils.getWeek(1, WeatherUtils.SHORT_FORMAT);
                nd2Week = WeatherUtils.getWeek(2, WeatherUtils.SHORT_FORMAT);
                nd3Week = WeatherUtils.getWeek(3, WeatherUtils.SHORT_FORMAT);

                nd1Desc = wp.getNextNthDayWeatherDesc(1, WeatherUtils.DO_NOT_TRANSLATE);
                nd2Desc = wp.getNextNthDayWeatherDesc(2, WeatherUtils.DO_NOT_TRANSLATE);
                nd3Desc = wp.getNextNthDayWeatherDesc(3, WeatherUtils.DO_NOT_TRANSLATE);
            } else {
                // 如果是英语以外, 则取翻译过后的数据, 而且未来星期使用[长格式]
                curDesc = wp.getCurWeatherDesc(WeatherUtils.DO_TRANSLATE);

                nd1Week = WeatherUtils.getWeek(1, WeatherUtils.LONG_FORMAT);
                nd2Week = WeatherUtils.getWeek(2, WeatherUtils.LONG_FORMAT);
                nd3Week = WeatherUtils.getWeek(3, WeatherUtils.LONG_FORMAT);

                nd1Desc = wp.getNextNthDayWeatherDesc(1, WeatherUtils.DO_TRANSLATE);
                nd2Desc = wp.getNextNthDayWeatherDesc(2, WeatherUtils.DO_TRANSLATE);
                nd3Desc = wp.getNextNthDayWeatherDesc(3, WeatherUtils.DO_TRANSLATE);
            }

            cityName = wp.getRequestCity();
            sysTime = WeatherUtils.getSysTime();
            curTempC = wp.getCurTemp_C();
            sysDate = WeatherUtils.getSysDate();
            curWeek = WeatherUtils.getWeek(0, WeatherUtils.LONG_FORMAT);

            nd1TempC = wp.getNextNthDayCompleteTempC(1);
            nd2TempC = wp.getNextNthDayCompleteTempC(2);
            nd3TempC = wp.getNextNthDayCompleteTempC(3);

            CityInfo cityInfo = new CityInfo();
            cityInfo.setCityId(1);
            try {
                cityInfo.setCityName(cityName.split(",")[0].trim());
                cityInfo.setCityNation(cityName.split(",")[1].trim());
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.i("db_err", e.toString());
                cityInfo.setCityName(cityName.trim());
                cityInfo.setCityNation(""); // set a null string
            }
            Log.i("db", String.valueOf(cityInfo.getCityId()));
            Log.i("db", cityInfo.getCityName());
            Log.i("db", cityInfo.getCityNation());

            mgr.addCityInfo(cityInfo);
            mgr.deleteDayInfo(1);

            // 当天天气情况
            DayInfo dayInfo = new DayInfo();
            dayInfo.setCityId(1);
            dayInfo.setDate(sysDate);
            dayInfo.setTime(sysTime);
            dayInfo.setWeek(curWeek);
            dayInfo.setTempC(curTempC);
            dayInfo.setDesc(curDesc);
            dayInfoList.add(dayInfo);

            // 后一天天气情况
            dayInfo = new DayInfo();
            dayInfo.setCityId(1);
            dayInfo.setDate(sysDate);
            dayInfo.setTime(sysTime);
            dayInfo.setWeek(nd1Week);
            dayInfo.setTempC(nd1TempC);
            dayInfo.setDesc(nd1Desc);
            dayInfoList.add(dayInfo);

            // 后二天天气情况
            dayInfo = new DayInfo();
            dayInfo.setCityId(1);
            dayInfo.setDate(sysDate);
            dayInfo.setTime(sysTime);
            dayInfo.setWeek(nd2Week);
            dayInfo.setTempC(nd2TempC);
            dayInfo.setDesc(nd2Desc);
            dayInfoList.add(dayInfo);

            // 后三天天气情况
            dayInfo = new DayInfo();
            dayInfo.setCityId(1);
            dayInfo.setDate(sysDate);
            dayInfo.setTime(sysTime);
            dayInfo.setWeek(nd3Week);
            dayInfo.setTempC(nd3TempC);
            dayInfo.setDesc(nd3Desc);
            dayInfoList.add(dayInfo);

            Log.i("http", "Current temperature: " + wp.getCurTemp_C());
            Log.i("http", "Current condition: " + curDesc);
            Log.i("http", "Current weather icon URL: " + wp.getCurWeatherIconUrl());
            Log.i("http", "Requested city: " + wp.getRequestCity());
            // 登录: http://www.worldweatheronline.com
            // 选择 FULL FORECAST 确认数据正确性
            Log.i("http", "D1 date: " + wp.getNextNthDayDate(1));
            Log.i("http", "D1 high: " + wp.getNextNthDayMaxTempC(1));
            Log.i("http", "D1 low: " + wp.getNextNthDayMinTempC(1));
            Log.i("http", "D1 condition: " + wp.getNextNthDayWeatherDesc(1, WeatherUtils.DO_NOT_TRANSLATE));
            Log.i("http", "D2 date: " + wp.getNextNthDayDate(2));
            Log.i("http", "D2 high: " + wp.getNextNthDayMaxTempC(2));
            Log.i("http", "D2 low: " + wp.getNextNthDayMinTempC(2));
            Log.i("http", "D2 condition: " + wp.getNextNthDayWeatherDesc(2, WeatherUtils.DO_NOT_TRANSLATE));
            Log.i("http", "D3 date: " + wp.getNextNthDayDate(3));
            Log.i("http", "D3 high: " + wp.getNextNthDayMaxTempC(3));
            Log.i("http", "D3 low: " + wp.getNextNthDayMinTempC(3));
            Log.i("http", "D3 condition: " + wp.getNextNthDayWeatherDesc(3, WeatherUtils.DO_NOT_TRANSLATE));

            activity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            activity.setContentView(R.layout.activity_main);

                            setTextView(dayInfoList);
                        }
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTextView(ArrayList<DayInfo> dayInfoList){

        // TextView
        cityNameView = (TextView) activity.findViewById(R.id.cityName);
        curTimeView = (TextView) activity.findViewById(R.id.curTime);
        curTempCView = (TextView) activity.findViewById(R.id.curTempC);
        curDescView = (TextView) activity.findViewById(R.id.curDesc);
        curDateView = (TextView) activity.findViewById(R.id.curDate);
        curWeekView = (TextView) activity.findViewById(R.id.curWeek);

        nd1WeekView = (TextView) activity.findViewById(R.id.nd1Week);
        nd1TempCView = (TextView) activity.findViewById(R.id.nd1TempC);
        nd1DescView = (TextView) activity.findViewById(R.id.nd1Desc);

        nd2WeekView = (TextView) activity.findViewById(R.id.nd2Week);
        nd2TempCView = (TextView) activity.findViewById(R.id.nd2TempC);
        nd2DescView = (TextView) activity.findViewById(R.id.nd2Desc);

        nd3WeekView = (TextView) activity.findViewById(R.id.nd3Week);
        nd3TempCView = (TextView) activity.findViewById(R.id.nd3TempC);
        nd3DescView = (TextView) activity.findViewById(R.id.nd3Desc);

        // Set Text
        cityNameView.setText(cityName);
        curDateView.setText(sysDate);
        curTimeView.setText(activity.getString(R.string.updated) + " " + sysTime);

        // 当天天气状况
        curTempCView.setText(dayInfoList.get(0).getTempC());
        curDescView.setText(dayInfoList.get(0).getDesc());
        curWeekView.setText(dayInfoList.get(0).getWeek());

        // 后一天天气情况
        nd1TempCView.setText(dayInfoList.get(1).getTempC());
        nd1DescView.setText(dayInfoList.get(1).getDesc());
        nd1WeekView.setText(dayInfoList.get(1).getWeek());

        // 后二天天气情况
        nd2TempCView.setText(dayInfoList.get(2).getTempC());
        nd2DescView.setText(dayInfoList.get(2).getDesc());
        nd2WeekView.setText(dayInfoList.get(2).getWeek());

        // 后三天天气情况
        nd3TempCView.setText(dayInfoList.get(3).getTempC());
        nd3DescView.setText(dayInfoList.get(3).getDesc());
        nd3WeekView.setText(dayInfoList.get(3).getWeek());
    }
}
