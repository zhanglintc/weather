package co.zhanglintc.weather;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import co.zhanglintc.weather.common.WeatherUtils;
import co.zhanglintc.weather.dao.DBManager;
import co.zhanglintc.weather.dao.DayInfo;

/**
 * Created by zhanglin on 2015/11/06.
 */
// TODO: 2015/12/05 这部分希望改造成为纯粹的取数据到数据库, 然后WeatherDisplay纯粹的从数据库取数据然后显示 => to zhanglin
public class BGupdater extends Thread {
    private Activity activity;
    private String apiUrl;
    private int cityId;

    public BGupdater(Activity activity, String apiUrl, int cityId) {
        this.activity = activity;
        this.apiUrl = apiUrl;
        this.cityId = cityId;
    }

    private void startRefresh() {
        activity.runOnUiThread(
            new Runnable() {
                @Override
                public void run() {
                    // TODO: 2015/12/06 这个try主要是应付数据库中完全没有数据的情况, 以后通过其他方式保证数据库不为空后就可以取消这个try => by zhanglin
                    try {
                        CityManager.cityStatus.put(cityId, true);
                        if (cityId == CityManager.curCityId) {
                            new WeatherDisplay(activity).displayInfo(cityId, WeatherUtils.DO_REFRESH);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        );
    }

    private void stopRefresh() {
        activity.runOnUiThread(
            new Runnable() {
                @Override
                public void run() {
                    CityManager.cityStatus.put(cityId, false);
                    if (cityId == CityManager.curCityId) {
                        new WeatherDisplay(activity).displayInfo(cityId, WeatherUtils.STOP_REFRESH);
                    }
                }
            }
        );
    }

    private String getWeatherData() {
        Log.i("http", "Getting weather data...");

        String weatherData = "";

        try {
            httpHandler http = new httpHandler();
            weatherData = http.get(apiUrl);

            return weatherData;

        } catch (IOException e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 如果获取不到数据, 则认为网络异常, Toast显示
                    Toast.makeText(activity, activity.getString(R.string.netErr), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return weatherData;
    }

    private void setDatabase(String weatherData) {
        String sysLang;

        String sysDate;
        String curTempC;
        String curDesc;
        String sysTime;
        String curWeek;

        String nd1Week,  nd2Week,  nd3Week;
        String nd1TempC, nd2TempC, nd3TempC;
        String nd1Desc,  nd2Desc,  nd3Desc;

        try {
            WeatherParser wp = new WeatherParser(weatherData);
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

            sysTime = WeatherUtils.getSysTime();
            curTempC = wp.getCurTemp_C();
            sysDate = WeatherUtils.getSysDate();
            curWeek = WeatherUtils.getWeek(0, WeatherUtils.LONG_FORMAT);

            nd1TempC = wp.getNextNthDayCompleteTempC(1);
            nd2TempC = wp.getNextNthDayCompleteTempC(2);
            nd3TempC = wp.getNextNthDayCompleteTempC(3);

            // 设置当前天气情况
            DayInfo dayInfo0 = new DayInfo();
            dayInfo0.setCityId(cityId);
            dayInfo0.setDate(sysDate);
            dayInfo0.setTime(sysTime);
            dayInfo0.setWeek(curWeek);
            dayInfo0.setTempC(curTempC);
            dayInfo0.setDesc(curDesc);

            // 设置后一天天气情况
            DayInfo dayInfo1 = new DayInfo();
            dayInfo1.setCityId(cityId);
            dayInfo1.setDate(sysDate);
            dayInfo1.setTime(sysTime);
            dayInfo1.setWeek(nd1Week);
            dayInfo1.setTempC(nd1TempC);
            dayInfo1.setDesc(nd1Desc);

            // 设置后二天天气情况
            DayInfo dayInfo2 = new DayInfo();
            dayInfo2.setCityId(cityId);
            dayInfo2.setDate(sysDate);
            dayInfo2.setTime(sysTime);
            dayInfo2.setWeek(nd2Week);
            dayInfo2.setTempC(nd2TempC);
            dayInfo2.setDesc(nd2Desc);

            // 设置后三天天气情况
            DayInfo dayInfo3 = new DayInfo();
            dayInfo3.setCityId(cityId);
            dayInfo3.setDate(sysDate);
            dayInfo3.setTime(sysTime);
            dayInfo3.setWeek(nd3Week);
            dayInfo3.setTempC(nd3TempC);
            dayInfo3.setDesc(nd3Desc);

            // 添加到dayInfoList
            ArrayList<DayInfo> dayInfoList = new ArrayList<>();
            dayInfoList.add(dayInfo0);
            dayInfoList.add(dayInfo1);
            dayInfoList.add(dayInfo2);
            dayInfoList.add(dayInfo3);

            // 存入数据库
            DBManager dbMgr = new DBManager(activity);
            dbMgr.addDayInfo(dayInfoList);
            dbMgr.closeDB();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void run() {
//        String rawJsonData = activity.getResources().getString(R.string.rawJsonData);
        startRefresh();
        setDatabase(getWeatherData());
        stopRefresh();
    }
}
