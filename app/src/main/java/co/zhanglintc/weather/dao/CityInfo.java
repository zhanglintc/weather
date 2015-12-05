package co.zhanglintc.weather.dao;

/**
 * Created by yanbin on 2015/12/02.
 */
public class CityInfo {

    private int cityId;

    private String cityName;

    private String cityNation;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityNation() {
        return cityNation;
    }

    public void setCityNation(String cityNation) {
        this.cityNation = cityNation;
    }
}
