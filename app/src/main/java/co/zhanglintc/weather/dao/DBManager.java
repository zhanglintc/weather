package co.zhanglintc.weather.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yanbin on 2015/12/02.
 */
public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add dayInfoList
     *
     * @param dayInfoList
     */
    public void addDayInfo(List<DayInfo> dayInfoList) {
        Timestamp t = new Timestamp(new Date().getTime());
        db.beginTransaction();  //开始事务
        try {
            for (DayInfo dayInfo : dayInfoList) {
                db.execSQL("INSERT INTO day_info VALUES(?, ?, ?, ?, ?, ?, ?)",
                        new Object[]{dayInfo.getCityId(), dayInfo.getDate(), dayInfo.getTime(), dayInfo.getWeek(), dayInfo.getTempC(), dayInfo.getDesc(), t});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * add cityInfo
     *
     * @param cityInfo
     */
    public void addCityInfo(CityInfo cityInfo) {
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("INSERT INTO city_info VALUES( ?, ?, ?)",
                    new Object[]{cityInfo.getCityId(), cityInfo.getCityName(), cityInfo.getCityNation()});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * get MaxCityId
     */
    public int selectMaxCityId() {
        Cursor c = db.rawQuery("SELECT MAX(city_id) AS max_city_id FROM city_info", null);

        int maxCityId = 0;
        while (c.moveToNext()) {
            maxCityId = c.getInt(c.getColumnIndex("max_city_id"));
        }

        return maxCityId;
    }

    /**
     * delete cityInfo
     *
     * @param cityId
     */
    public void deleteCityInfo(String cityId) {
        db.delete("city_info", "city_id = ?", new String[]{String.valueOf(cityId)});
    }

    /**
     * delete dayInfo
     *
     * @param cityId
     */
    public void deleteDayInfo(String cityId) {
        db.delete("day_info", "city_id = ?", new String[]{String.valueOf(cityId)});
    }

    /**
     * query a cityInfo, return cityInfo
     *
     * @return cityInfo
     */
    public CityInfo queryCityInfo(String cityId) {
        CityInfo cityInfo = new CityInfo();

        Cursor c = db.rawQuery("SELECT * FROM city_info WHERE city_id = '" + cityId + "'", null);

        while (c.moveToNext()) {
            cityInfo.setCityId(cityId);
            cityInfo.setCityName(c.getString(c.getColumnIndex("city_name")));
            cityInfo.setCityNation(c.getString(c.getColumnIndex("city_nation")));
        }

        c.close();

        return cityInfo;
    }

    /**
     * query all dayInfoList, return list
     *
     * @return List<DayInfo>
     */
    public List<DayInfo> queryDayInfo() {
        ArrayList<DayInfo> dayInfoList = new ArrayList<DayInfo>();
        Cursor c = db.rawQuery("SELECT * FROM day_info ORDER BY updatetime DESC LIMIT 4", null);
        while (c.moveToNext()) {
            DayInfo dayInfo = new DayInfo();

            dayInfo.setCityId(c.getString(c.getColumnIndex("city_id")));
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
