package co.zhanglintc.weather;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String APIurl = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=chongqing&nu%20m_of_days=3&format=json&lang=zh";

        /********************************************
        Network data cannot be handle in MAIN thread.
        Solutions:
          1. Use StrictMode.
          2. Use another thread.
        See: http://www.cnblogs.com/purediy/p/3225093.html
        /********************************************/
        StrictMode.setThreadPolicy(
            new StrictMode.ThreadPolicy.Builder()
            .detectDiskReads()
            .detectDiskWrites()
            .detectNetwork()
            .penaltyLog()
            .build()
        );
        StrictMode.setVmPolicy(
            new StrictMode.VmPolicy.Builder()
            .detectLeakedSqlLiteObjects()
            .detectLeakedClosableObjects()
            .penaltyLog()
            .penaltyDeath().build()
        );

        httpHandler http = new httpHandler();
        String s = http.get(APIurl);
        Log.i("http", s);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
