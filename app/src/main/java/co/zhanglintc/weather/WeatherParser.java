package co.zhanglintc.weather;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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

    // 1st day's data (today)
    /**
     * Return current temperature celsius.
     * eg: 23
     */
    public String getCurTemp_C() throws JSONException {
        return this.json.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).getString("temp_C");
    }

    /**
     * Return current weather condition.
     * eg: 1. Clear 2. Overcast 3. Rainy
     */
    public String getCurWeatherDesc() throws JSONException {
        return this.json.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).getJSONArray("weatherDesc").getJSONObject(0).getString("value");
    }

    /**
     * Return translated weather condition.
     * eg: 1. 晴天(zh) 2. 暴雨(zh) 3. 所により曇り(ja)
     */
    public String getCurWeatherDescTranslation(String language) throws JSONException {
        return this.json.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).getJSONArray("lang_" + language).getJSONObject(0).getString("value");
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

    // 2nd day's data (tomorrow)
    /**
     * Return tomorrow's date.
     * eg: 2015-11-11
     */
    public String get2ndDayDate() throws JSONException {
        return this.json.getJSONObject("data").getJSONArray("weather").getJSONObject(1).getString("date");
    }

    /**
     * Return tomorrow's max temperature celsius.
     * eg: 19
     */
    public String get2ndDayMaxTempC() throws JSONException {
        return this.json.getJSONObject("data").getJSONArray("weather").getJSONObject(1).getString("maxtempC");
    }

    /**
     * Return tomorrow's min temperature celsius.
     * eg: 16
     */
    public String get2ndDayMinTempC() throws JSONException {
        return this.json.getJSONObject("data").getJSONArray("weather").getJSONObject(2).getString("mintempC");
    }

    // 3rd day's data (the day after tomorrow)
    /**
     * Return tomorrow's date.
     * eg: 2015-11-12
     */
    public String get3rdDayDate() throws JSONException {
        return this.json.getJSONObject("data").getJSONArray("weather").getJSONObject(2).getString("date");
    }

    /**
     * Return tomorrow's max temperature celsius.
     * eg: 19
     */
    public String get3rdDayMaxTempC() throws JSONException {
        return this.json.getJSONObject("data").getJSONArray("weather").getJSONObject(2).getString("maxtempC");
    }

    /**
     * Return tomorrow's min temperature celsius.
     * eg: 16
     */
    public String get3rdDayMinTempC() throws JSONException {
        return this.json.getJSONObject("data").getJSONArray("weather").getJSONObject(2).getString("mintempC");
    }
}
