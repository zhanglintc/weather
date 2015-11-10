package co.zhanglintc.weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ant.liao.GifView;

import co.zhanglintc.weather.common.WeatherUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.main_loading);
        GifView gifView = (GifView) super.findViewById(R.id.gifView);
        gifView.setGifImage(R.drawable.loading);
        gifView.setGifImageType(GifView.GifImageType.SYNC_DECODER);

        // To yanbin: 这里是暂时注释掉的
//        TextView todayTemp = (TextView) super.findViewById(R.id.todayTemp);
//        TextView todayStatus = (TextView) super.findViewById(R.id.todayStatus);
//        GifView gifView = (GifView) super.findViewById(R.id.gifView);
//        gifView.setGifImage(R.drawable.loading);
//        gifView.setGifImageType(GifView.GifImageType.SYNC_DECODER);
//        todayTemp.setText("Updating weather data...");
//        todayStatus.setText("");

//        new BackgroundUpdateThread(this).start();
//        WeatherUtils.urlRun(this);
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
            new BGupdater(this).start();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}