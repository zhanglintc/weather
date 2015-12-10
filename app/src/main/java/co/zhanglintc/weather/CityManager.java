package co.zhanglintc.weather;

import android.app.Activity;

import java.util.HashMap;

import co.zhanglintc.weather.common.WeatherUtils;

/**
 * Created by lane on 2015/12/08.
 * Singleton, lazy initialization
 */
public class CityManager {
    public static int curCityId = 1; // 暂定为显示重庆

    // TODO: 2015/12/08 需要一个数组或者哈希表管理所有线程, 每个线程(或者说每个城市)需要有一个是否处于更新中的状态, 用于决定切换到当前城市时图标状态 #1 => to zhanglin
    private static CityManager CM = null;

    private String cqURL = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=chongqing&num_of_days=4&format=json&lang=" + WeatherUtils.getLanguge();
    private String bjURL = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=beijing&num_of_days=4&format=json&lang=" + WeatherUtils.getLanguge();
    private String shURL = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=shanghai&num_of_days=4&format=json&lang=" + WeatherUtils.getLanguge();

    public static HashMap<Integer, Boolean> cityStatus = new HashMap<>();

    private CityManager(Activity activity) {
        cityStatus.put(1, false);
        cityStatus.put(2, false);
        cityStatus.put(3, false);
    }

    public static CityManager getInstance(Activity activity) {
        if (CM == null) {
            CM = new CityManager(activity);
        }

        return CM;
    }
}
