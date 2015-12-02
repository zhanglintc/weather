package co.zhanglintc.weather.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yanbin on 2015/12/02.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS city_info (\n" +
                "  [city_id] CHAR(15) NOT NULL ON CONFLICT ROLLBACK REFERENCES [city_info]([city_id]), \n" +
                "  [city_Name] VARCHAR2(50) NOT NULL ON CONFLICT ROLLBACK, \n" +
                "  [city_nation] CHAR(50), \n" +
                "  CONSTRAINT [] PRIMARY KEY ([city_id]))" );

        db.execSQL("CREATE TABLE IF NOT EXISTS day_info" +
                "(  [city_id] CHAR(15) NOT NULL ON CONFLICT ROLLBACK, \n" +
                "  [day] CHAR(10) NOT NULL ON CONFLICT ROLLBACK, \n" +
                "  [time] CHAR(8), \n" +
                "  [week] VARCHAR2(9), \n" +
                "  [tempc] VARCHAR NOT NULL ON CONFLICT ROLLBACK, \n" +
                "  [desc] VARCHAR2(20), \n" +
                "  [updatetime] TIMESTAMP NOT NULL ON CONFLICT ROLLBACK)");
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
