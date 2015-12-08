package co.zhanglintc.weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ant.liao.GifView;

import co.zhanglintc.weather.common.WeatherUtils;
import co.zhanglintc.weather.dao.CityInfo;
import co.zhanglintc.weather.dao.DBManager;

public class MainActivity extends AppCompatActivity {

    String cqURL = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=chongqing&num_of_days=4&format=json&lang=" + WeatherUtils.getLanguge();
    String bjURL = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=beijing&num_of_days=4&format=json&lang=" + WeatherUtils.getLanguge();
    String shURL = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=shanghai&num_of_days=4&format=json&lang=" + WeatherUtils.getLanguge();

    private void firstRun() {
        setContentView(R.layout.main_loading);
        GifView gif = (GifView) findViewById(R.id.gifView);
        gif.setGifImage(R.drawable.loading);
        gif.setGifImageType(GifView.GifImageType.SYNC_DECODER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO: 2015/12/07 考虑activity_main.xml中, todayInfo和futureInfo不再使用weight来分配比例 => to zhanglin
        super.onCreate(savedInstanceState);

        DBManager dbMgr = new DBManager(this);
        if (dbMgr.queryCityInfoList().size() == 0) {
            Log.i("db", "db not exist");

            firstRun();

            dbMgr.clearCityInfoAll();

            CityInfo cityCQ = new CityInfo();
            CityInfo cityBJ = new CityInfo();
            CityInfo citySH = new CityInfo();

            cityCQ.setCityId(1);
            cityCQ.setCityName("Chongqing");
            cityCQ.setCityNation("China");

            cityBJ.setCityId(2);
            cityBJ.setCityName("Beijing");
            cityBJ.setCityNation("China");

            citySH.setCityId(3);
            citySH.setCityName("Shanghai");
            citySH.setCityNation("China");

            dbMgr.addCityInfo(cityCQ);
            dbMgr.addCityInfo(cityBJ);
            dbMgr.addCityInfo(citySH);
        }
        dbMgr.closeDB();

        new BGupdater(this, cqURL, 1).start();
        new BGupdater(this, bjURL, 2).start();
        new BGupdater(this, shURL, 3).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            // View view = getWindow().getDecorView().findViewById(android.R.id.content);

            TextView cityNameView = (TextView) findViewById(R.id.cityName);
            Log.i("menu", (String) cityNameView.getText());

            new BGupdater(this, cqURL, 1).start();
            new BGupdater(this, bjURL, 2).start();
            new BGupdater(this, shURL, 3).start();

            return true;
        }

        if (id == R.id.action_chongqing) {
            ThreadController.curCityId = 1;
            WeatherDisplay wd = new WeatherDisplay(this);
            // TODO: 2015/12/08 这里将不能够再一刀切的设置为STOP_REFRESH, 而应该根据每个城市的状态来决定, 状态由ThreadController来管理 #1 => to zhanglin
            wd.displayInfo(1, WeatherUtils.STOP_REFRESH);

            return true;
        }

        if (id == R.id.action_beijing) {
            ThreadController.curCityId = 2;
            WeatherDisplay wd = new WeatherDisplay(this);
            wd.displayInfo(2, WeatherUtils.STOP_REFRESH);

            return true;
        }

        if (id == R.id.action_shanghai) {
            ThreadController.curCityId = 3;
            WeatherDisplay wd = new WeatherDisplay(this);
            wd.displayInfo(3, WeatherUtils.STOP_REFRESH);

            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}