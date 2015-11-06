package co.zhanglintc.weather;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import co.zhanglintc.weather.common.PullXMLTools;
import co.zhanglintc.weather.common.WeatherUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView todayTemp = (TextView) super.findViewById(R.id.todayTemp);
        TextView todayStatus = (TextView) super.findViewById(R.id.todayStatus);
        todayTemp.setText("Updating weather data...");
        todayStatus.setText("");

        BackgroundUpdateThread t = new BackgroundUpdateThread(this);
        t.start();
        new Thread(urlRun).start();
    }

    Runnable urlRun = new Runnable(){
        @Override
        public void run() {

            Looper.prepare();

            // TEST
            Map<String,Object> cityMap = new HashMap();

            try {
                // InputStream inputStream = WeatherUtils.getXML("http://115.29.192.240/language.xml");
                InputStream inputStream = getAssets().open("language.xml");
                cityMap = PullXMLTools.parseXML(inputStream, "UTF-8", WeatherUtils.getLanguge());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("xml", "city: " + cityMap.get("city"));

            Looper.loop();
        }
    };

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}