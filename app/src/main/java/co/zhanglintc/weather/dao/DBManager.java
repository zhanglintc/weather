package co.zhanglintc.weather.dao;

import android.content.ContentValues;
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
     * delete old dayInfo
     *
     * @param city
     */
    public void deleteDayInfo(String city) {
        db.delete("day_info", "city_id = ?", new String[]{String.valueOf(city)});
    }

    /**
     * query a cityInfo, return cityInfo
     *
     * @return cityInfo
     */
    public CityInfo queryCityInfo(String cityId) {
        Cursor c = db.rawQuery("SELECT * FROM city_info WHERE city_id = '" + cityId + "'", null);

        CityInfo cityInfo = new CityInfo();
        while (c.moveToNext()) {
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
            dayInfo.setDesc(c.getString(c.getColumnIndex("tempc")));
            dayInfo.setTime(c.getString(c.getColumnIndex("desc")));

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
