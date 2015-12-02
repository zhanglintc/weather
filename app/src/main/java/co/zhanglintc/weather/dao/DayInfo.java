package co.zhanglintc.weather.dao;

/**
 * Created by yanbin on 2015/12/02.
 */
public class DayInfo {

    private String cityId;

    private String day;

    private String time;

    private String week;

    private String tempc;

    private String desc;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTempc() {
        return tempc;
    }

    public void setTempc(String tempc) {
        this.tempc = tempc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
