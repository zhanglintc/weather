package co.zhanglintc.weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ant.liao.GifView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_main);
        setContentView(R.layout.main_loading);

        GifView gifView = (GifView) super.findViewById(R.id.gifView);
        gifView.setGifImage(R.drawable.loading);
        gifView.setGifImageType(GifView.GifImageType.SYNC_DECODER);

        new BGupdater(this).start();
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
            View view = getWindow().getDecorView().findViewById(android.R.id.content);
            GifView rfsIconView = (GifView) view.findViewById(R.id.rfsIcon);
            rfsIconView.setGifImage(R.drawable.loading);
            rfsIconView.setGifImageType(GifView.GifImageType.SYNC_DECODER);

            TextView cityNameView = (TextView) findViewById(R.id.cityName);
            Log.i("iii", (String) cityNameView.getText());
            cityNameView.setText("updating");

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