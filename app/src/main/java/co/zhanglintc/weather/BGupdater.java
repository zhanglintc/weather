package co.zhanglintc.weather;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.ant.liao.GifView;

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
                            WeatherDisplay wd = new WeatherDisplay(activity);
                            wd.displayInfo(cityId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        GifView gif = (GifView) activity.findViewById(R.id.loc_ref_Icon);
                        // TODO: 2015/12/06 这里setBackgroundResource(0)表现不太稳定, 原因未知 => to zhanlgin
                        gif.setBackgroundResource(0);
                        // TODO: 2015/12/06 可以考虑把gif与静态图的转换挪到WeatherDisplay类中, 通过参数控制 => to zhanglin
                        gif.setGifImage(R.drawable.refresh);
                        gif.setGifImageType(GifView.GifImageType.SYNC_DECODER);
                    }
                }
        );
    }

    private void stopRefresh() {
        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        WeatherDisplay wd = new WeatherDisplay(activity);
                        wd.displayInfo(cityId);
                        GifView gif = (GifView) activity.findViewById(R.id.loc_ref_Icon);
                        gif.setBackgroundResource(R.drawable.pin_dot);
                    }
                }
        );
    }

    private void updateDatabase() {
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
            Log.i("http", "Getting weather data...");
            httpHandler http = new httpHandler();
            String weatherData = http.get(apiUrl);

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

            ArrayList<DayInfo> dayInfoList = new ArrayList<>();

            // 当天天气情况
            DayInfo dayInfo = new DayInfo();
            dayInfo.setCityId(cityId);
            dayInfo.setDate(sysDate);
            dayInfo.setTime(sysTime);
            dayInfo.setWeek(curWeek);
            dayInfo.setTempC(curTempC);
            dayInfo.setDesc(curDesc);
            dayInfoList.add(dayInfo);

            // 后一天天气情况
            dayInfo = new DayInfo();
            dayInfo.setCityId(cityId);
            dayInfo.setDate(sysDate);
            dayInfo.setTime(sysTime);
            dayInfo.setWeek(nd1Week);
            dayInfo.setTempC(nd1TempC);
            dayInfo.setDesc(nd1Desc);
            dayInfoList.add(dayInfo);

            // 后二天天气情况
            dayInfo = new DayInfo();
            dayInfo.setCityId(cityId);
            dayInfo.setDate(sysDate);
            dayInfo.setTime(sysTime);
            dayInfo.setWeek(nd2Week);
            dayInfo.setTempC(nd2TempC);
            dayInfo.setDesc(nd2Desc);
            dayInfoList.add(dayInfo);

            // 后三天天气情况
            dayInfo = new DayInfo();
            dayInfo.setCityId(cityId);
            dayInfo.setDate(sysDate);
            dayInfo.setTime(sysTime);
            dayInfo.setWeek(nd3Week);
            dayInfo.setTempC(nd3TempC);
            dayInfo.setDesc(nd3Desc);
            dayInfoList.add(dayInfo);

            DBManager dbMgr = new DBManager(activity);
            dbMgr.addDayInfo(dayInfoList);
            dbMgr.closeDB();

        } catch (IOException e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, activity.getString(R.string.netErr), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void run() {
//        String rawJsonData = activity.getResources().getString(R.string.rawJsonData);
        startRefresh();
        updateDatabase();
        stopRefresh();
    }
}
