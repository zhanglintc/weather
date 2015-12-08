package co.zhanglintc.weather;

import android.app.Activity;
import android.widget.TextView;

import com.ant.liao.GifView;

import java.util.ArrayList;

import co.zhanglintc.weather.dao.CityInfo;
import co.zhanglintc.weather.dao.DBManager;
import co.zhanglintc.weather.dao.DayInfo;

/**
 * Created by lane on 2015/12/05.
 */
public class WeatherDisplay {

    private Activity activity;
    private boolean refreshing;

    WeatherDisplay(Activity activity) {
        this.activity = activity;
    }

    public void displayInfo(int cityId, boolean refreshing) {
        
        this.refreshing = refreshing;

        DBManager dbMgr = new DBManager(activity);

        CityInfo cityInfo = dbMgr.queryCityInfo(cityId);
        ArrayList<DayInfo> dayInfoList = dbMgr.queryDayInfo(cityId);

        activity.setContentView(R.layout.activity_main);
        setTextView(cityInfo, dayInfoList);

        dbMgr.closeDB();
    }

    private void setTextView(CityInfo cityInfo, ArrayList<DayInfo> dayInfoList){
        // TextView
        TextView cityNameView = (TextView) activity.findViewById(R.id.cityName);
        TextView curTimeView = (TextView) activity.findViewById(R.id.curTime);
        TextView curTempCView = (TextView) activity.findViewById(R.id.curTempC);
        TextView curDescView = (TextView) activity.findViewById(R.id.curDesc);
        TextView curDateView = (TextView) activity.findViewById(R.id.curDate);
        TextView curWeekView = (TextView) activity.findViewById(R.id.curWeek);

        TextView nd1WeekView = (TextView) activity.findViewById(R.id.nd1Week);
        TextView nd1TempCView = (TextView) activity.findViewById(R.id.nd1TempC);
        TextView nd1DescView = (TextView) activity.findViewById(R.id.nd1Desc);

        TextView nd2WeekView = (TextView) activity.findViewById(R.id.nd2Week);
        TextView nd2TempCView = (TextView) activity.findViewById(R.id.nd2TempC);
        TextView nd2DescView = (TextView) activity.findViewById(R.id.nd2Desc);

        TextView nd3WeekView = (TextView) activity.findViewById(R.id.nd3Week);
        TextView nd3TempCView = (TextView) activity.findViewById(R.id.nd3TempC);
        TextView nd3DescView = (TextView) activity.findViewById(R.id.nd3Desc);

        // refresh or not refresh
        GifView gif = (GifView) activity.findViewById(R.id.loc_ref_Icon);
        if (refreshing) {
            gif.setBackgroundResource(0);
            gif.setGifImage(R.drawable.refresh);
            gif.setGifImageType(GifView.GifImageType.SYNC_DECODER);
        } else {
            gif.setBackgroundResource(R.drawable.pin_dot);
        }

        String cityName = String.format("%s, %s", cityInfo.getCityName(), cityInfo.getCityNation());
        String sysDate = dayInfoList.get(0).getDate();
        String sysTime = String.format("%s %s", activity.getString(R.string.updated), dayInfoList.get(0).getTime());

        // Set Text
        cityNameView.setText(cityName);
        curDateView.setText(sysDate);
        curTimeView.setText(sysTime);

        // 当天天气状况
        curTempCView.setText(dayInfoList.get(0).getTempC());
        curDescView.setText(dayInfoList.get(0).getDesc());
        curWeekView.setText(dayInfoList.get(0).getWeek());

        // 后一天天气情况
        nd1TempCView.setText(dayInfoList.get(1).getTempC());
        nd1DescView.setText(dayInfoList.get(1).getDesc());
        nd1WeekView.setText(dayInfoList.get(1).getWeek());

        // 后二天天气情况
        nd2TempCView.setText(dayInfoList.get(2).getTempC());
        nd2DescView.setText(dayInfoList.get(2).getDesc());
        nd2WeekView.setText(dayInfoList.get(2).getWeek());

        // 后三天天气情况
        nd3TempCView.setText(dayInfoList.get(3).getTempC());
        nd3DescView.setText(dayInfoList.get(3).getDesc());
        nd3WeekView.setText(dayInfoList.get(3).getWeek());
    }
}
