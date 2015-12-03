package co.zhanglintc.weather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.zhanglintc.weather.common.WeatherUtils;

/**
 * Created by lane on 11/3/15.
 * Parse given raw JSON string data.
 */
public class WeatherParser {
    JSONObject json = null;

    // Constructors:
    /**
     * Constructor of WeatherParser.
     */
    public WeatherParser(String s) throws JSONException {
        json = new JSONObject(new JSONTokener(s));
    }

    // Today's data
    /**
     * Return current temperature celsius.
     * eg: 23°C
     */
    public String getCurTemp_C() throws JSONException {
        String tempC;

        tempC = this.json.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).getString("temp_C");
        return String.format("%s°C", tempC);
    }

    /**
     * Return current weather condition.
     * eg: 1. Clear 2. Overcast 3. Rainy
     *     4. 晴天(zh) 5. 暴雨(zh) 6. 所により曇り(ja)
     */
    public String getCurWeatherDesc(boolean translate) throws JSONException {
        String DescKey = translate ? "lang_" + WeatherUtils.getLanguge() : "weatherDesc";
        return this.json.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).getJSONArray(DescKey).getJSONObject(0).getString("value");
    }

    /**
     * Return icon URL of current weather condition.
     * eg: http://cdn.worldweatheronline.net/images/wsymbols01_png_64/wsymbol_0008_clear_sky_night.png
     */
    public String getCurWeatherIconUrl() throws JSONException {
        return this.json.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).getJSONArray("weatherIconUrl").getJSONObject(0).getString("value");
    }

    /**
     * Return requested city name.
     * eg: 1. Chongqing, China 2. Lanzhou, China
     */
    public String getRequestCity() throws  JSONException {
        return this.json.getJSONObject("data").getJSONArray("request").getJSONObject(0).getString("query");
    }

    // Future's data
    // Nth == 1 means tomorrow
    // Nth == 2 means the day after tomorrow
    // etc
    /**
     * Return the next Nth day's date.
     * eg: 2015-11-11
     */
    public String getNextNthDayDate(int Nth) throws JSONException {
        return this.json.getJSONObject("data").getJSONArray("weather").getJSONObject(Nth).getString("date");
    }

    /**
     * Return the next Nth day's max temperature celsius.
     * eg: 19
     */
    public String getNextNthDayMaxTempC(int Nth) throws JSONException {
        return this.json.getJSONObject("data").getJSONArray("weather").getJSONObject(Nth).getString("maxtempC");
    }


    /**
     * Return next Nth day's min temperature celsius.
     * eg: 16
     */
    public String getNextNthDayMinTempC(int Nth) throws JSONException {
        return this.json.getJSONObject("data").getJSONArray("weather").getJSONObject(Nth).getString("mintempC");
    }

    /**
     * Return next Nth day's temperature celsius.
     * eg: 16~19°C
     */
    public String getNextNthDayCompleteTempC(int Nth) throws JSONException {
        String maxC;
        String minC;

        maxC = this.getNextNthDayMaxTempC(Nth);
        minC = this.getNextNthDayMinTempC(Nth);

        return String.format("%s~%s°C", minC, maxC);
    }

    /**
     * Return next Nth day's weather condition.
     * 取出所有的可能的天气状态作为HashMap, 然后排序, 取出第0个数据(最大的数据, 可能性最大的数据)
     * eg: 1. Clear 2. Overcast 3. Rainy
     */
    public String getNextNthDayWeatherDesc(int Nth, boolean translate) throws JSONException {
        // HashMap & ArrayList
        HashMap<String, Integer> WeatherDesc = new HashMap<>();
        List<Map.Entry<String, Integer>> WeatherDescSorted;

        // Get hourly
        JSONArray hourly = this.json.getJSONObject("data").getJSONArray("weather").getJSONObject(Nth).getJSONArray("hourly");

        // Fill up WeatherDesc
        String key;
        String DescKey = translate ? "lang_" + WeatherUtils.getLanguge() : "weatherDesc";
        for (int i = 0; i < hourly.length(); i++) {
            Log.i("desc", hourly.getJSONObject(i).getJSONArray(DescKey).getJSONObject(0).getString("value"));
            key = hourly.getJSONObject(i).getJSONArray(DescKey).getJSONObject(0).getString("value");
            WeatherDesc.put(key, WeatherDesc.containsKey(key) ? WeatherDesc.get(key) + 1 : 1);
        }

        // Make HashMap sorted and change it to ArrayList
        WeatherDescSorted = new ArrayList<>(WeatherDesc.entrySet());
        Collections.sort(WeatherDescSorted, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                // Refer: http://www.cnblogs.com/lovebread/archive/2009/11/23/1609121.html
                // return o1.getKey().compareTo(o2.getKey());   // 按键排序
                return (o2.getValue() - o1.getValue());         // 按值排序
            }
        });

        return WeatherDescSorted.get(0).getKey();
    }
}
