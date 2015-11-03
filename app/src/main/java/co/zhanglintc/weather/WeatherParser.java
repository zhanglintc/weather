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

    /**
     * Created by lane on 11/3/15.
     * Constructor of WeatherParser.
     */
    public WeatherParser(String s) throws JSONException {
            json = new JSONObject(new JSONTokener(s));
    }

    /**
     * Created by lane on 11/3/15.
     * Return current temperature celsius.
     * eg: 23
     */
    public String getCurTemp_C() throws JSONException {
        return this.json.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).getString("temp_C");
    }

    /**
     * Created by lane on 11/3/15.
     * Return current weather condition.
     * eg: 1. Clear 2. Overcast 3. Rainy
     */
    public String getCurWeatherDesc() throws JSONException {
        return this.json.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).getJSONArray("weatherDesc").getJSONObject(0).getString("value");
    }

    /**
     * Created by lane on 11/3/15.
     * Return icon URL of current weather condition.
     * eg: http://cdn.worldweatheronline.net/images/wsymbols01_png_64/wsymbol_0008_clear_sky_night.png
     */
    public String getCurWeatherIconUrl() throws JSONException {
        return this.json.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).getJSONArray("weatherIconUrl").getJSONObject(0).getString("value");
    }
}
