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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        DBManager dbMgr = new DBManager(this);
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
            WeatherDisplay wd = new WeatherDisplay(this);
            wd.displayInfo(1);

            return true;
        }

        if (id == R.id.action_beijing) {
            WeatherDisplay wd = new WeatherDisplay(this);
            wd.displayInfo(2);

            return true;
        }

        if (id == R.id.action_shanghai) {
            WeatherDisplay wd = new WeatherDisplay(this);
            wd.displayInfo(3);

            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}