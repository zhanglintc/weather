package co.zhanglintc.weather;

import android.app.Activity;

import java.util.ArrayList;

import co.zhanglintc.weather.common.WeatherUtils;
import co.zhanglintc.weather.dao.DBManager;

/**
 * Created by lane on 2015/12/08.
 * Singleton, lazy initialization
 */
public class ThreadController {
    public static int curCityId;

    private static ThreadController tController = null;
    private String cqURL = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=chongqing&num_of_days=4&format=json&lang=" + WeatherUtils.getLanguge();
    private String bjURL = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=beijing&num_of_days=4&format=json&lang=" + WeatherUtils.getLanguge();
    private String shURL = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=shanghai&num_of_days=4&format=json&lang=" + WeatherUtils.getLanguge();

    private ThreadController(Activity activity) {
        DBManager dbMgr = new DBManager(activity);
        ArrayList<BGupdater> tList = new ArrayList<>();
        tList.add(new BGupdater(activity, cqURL, 1));
        tList.add(new BGupdater(activity, bjURL, 2));
        tList.add(new BGupdater(activity, shURL, 3));
    }

    public static ThreadController getInstance(Activity activity) {
        if (tController == null) {
            tController = new ThreadController(activity);
        }

        return tController;
    }
}
