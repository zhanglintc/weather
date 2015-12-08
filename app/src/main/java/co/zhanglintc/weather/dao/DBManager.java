package co.zhanglintc.weather.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by yanbin on 2015/12/02.
 */
public class DBManager {
    // TODO: 2015/12/07 解决报错信息: 12-07 22:05:37.534 6252-6587/co.zhanglintc.weather E/SQLiteLog: (5) database is locked => to yanbin

    private SQLiteDatabase db;

    public DBManager(Context context) {
        DBHelper h = new DBHelper(context);
        db = h.getWritableDatabase();
    }

    /**
     * add dayInfoList
     *
     * @param dayInfoList
     */
    public void addDayInfo(ArrayList<DayInfo> dayInfoList) {
        Timestamp t = new Timestamp(new Date().getTime());
        db.beginTransaction();
        try {
            deleteDayInfo(dayInfoList.get(0).getCityId()); // 先清空该city_id的数据再存入
            for (DayInfo dayInfo : dayInfoList) {
                db.execSQL("INSERT INTO day_info VALUES(?, ?, ?, ?, ?, ?, ?)",
                        new Object[]{dayInfo.getCityId(), dayInfo.getDate(), dayInfo.getTime(), dayInfo.getWeek(), dayInfo.getTempC(), dayInfo.getDesc(), t});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * add cityInfo
     *
     * @param cityInfo
     */
    public void addCityInfo(CityInfo cityInfo) {
        db.beginTransaction();
        try {
            db.execSQL("INSERT INTO city_info VALUES( ?, ?, ?)",
                    new Object[]{cityInfo.getCityId(), cityInfo.getCityName(), cityInfo.getCityNation()});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * get MaxCityId
     */
    public int getMaxCityId() {
        int maxCityId = 0;

        Cursor c = db.rawQuery("SELECT MAX(city_id) AS max_city_id FROM city_info", null);
        while (c.moveToNext()) {
            maxCityId = c.getInt(c.getColumnIndex("max_city_id"));
        }

        c.close();

        return maxCityId;
    }

    /**
     * delete cityInfo
     *
     * @param cityId
     */
    public void deleteCityInfo(int cityId) {
        db.delete("city_info", "city_id = ?", new String[]{String.valueOf(cityId)});
    }

    /**
     * delete dayInfo
     *
     * @param cityId
     */
    public void deleteDayInfo(int cityId) {
        db.delete("day_info", "city_id = ?", new String[]{String.valueOf(cityId)});
    }

    /**
     * clear the whole city_info table
     */
    public void clearCityInfoAll() {
        db.execSQL("DELETE FROM city_info");
    }

    /**
     * clear the whole day_info table
     */
    public void clearDayInfoAll() {
        db.execSQL("DELETE FROM day_info");
    }

    /**
     * query a cityInfo, return cityInfo
     *
     * @return cityInfo
     */
    public CityInfo queryCityInfo(int cityId) {
        CityInfo cityInfo = new CityInfo();

        Cursor c = db.rawQuery(String.format("SELECT * FROM city_info WHERE city_id = '%s'", cityId), null);
        while (c.moveToNext()) {
            cityInfo.setCityId(cityId);
            cityInfo.setCityName(c.getString(c.getColumnIndex("city_name")));
            cityInfo.setCityNation(c.getString(c.getColumnIndex("city_nation")));
        }

        c.close();

        return cityInfo;
    }

    /**
     * query all cityInfoList, return list
     *
     * @return List<CityInfo>
     */
    public ArrayList<CityInfo> queryCityInfoList() {
        ArrayList<CityInfo> cityInfoList = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM city_info ORDER BY city_id", null);
        while (c.moveToNext()) {
            CityInfo cityInfo = new CityInfo();

            cityInfo.setCityId(c.getInt(c.getColumnIndex("city_id")));
            cityInfo.setCityName(c.getString(c.getColumnIndex("city_name")));
            cityInfo.setCityNation(c.getString(c.getColumnIndex("city_nation")));

            cityInfoList.add(cityInfo);
        }

        return cityInfoList;
    }

    /**
     * query all dayInfoList, return list
     *
     * @return List<DayInfo>
     */
    public ArrayList<DayInfo> queryDayInfoList(int cityId) {
        ArrayList<DayInfo> dayInfoList = new ArrayList<>();

        Cursor c = db.rawQuery(String.format("SELECT * FROM day_info WHERE city_id = %s ORDER BY updateTime DESC LIMIT 4", cityId), null);
        while (c.moveToNext()) {
            DayInfo dayInfo = new DayInfo();

            dayInfo.setCityId(cityId);
            dayInfo.setDate(c.getString(c.getColumnIndex("day")));
            dayInfo.setTime(c.getString(c.getColumnIndex("time")));
            dayInfo.setWeek(c.getString(c.getColumnIndex("week")));
            dayInfo.setDesc(c.getString(c.getColumnIndex("desc")));
            dayInfo.setTempC(c.getString(c.getColumnIndex("tempC")));

            dayInfoList.add(dayInfo);
        }

        c.close();

        return dayInfoList;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}
