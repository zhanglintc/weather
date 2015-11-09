package co.zhanglintc.weather;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by zhanglin on 2015/11/06.
 */
public class GeoUpdater {
    Activity activity;

    GeoUpdater(Activity activity) {
        this.activity = activity;
    }

    // not available right now
    // do not call this method
    public void updateLoc() {
        // GPS related -S
        // To yanbin: 虽然取到值了, 但是完全不能理解这是什么鬼
        double latitude, longitude =0.0;
        // TextView lat = (TextView) activity.findViewById(R.id.lat);
        // TextView lng = (TextView) activity.findViewById(R.id.lng);

        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        // if enabled
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                latitude = location.getLatitude(); //经度
                longitude = location.getLongitude(); //纬度
                // lat.setText("lat: " + latitude);
                // lng.setText("lng: " + longitude);
            }
        }
        // if not enabled
        else {
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) { //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                    if (location != null) {
                        Log.i("gps", "Location changed : Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
                    }
                }
                public void onProviderDisabled(String provider) {
                    // Provider被disable时触发此函数，比如GPS被关闭
                }
                public void onProviderEnabled(String provider) {
                    // Provider被enable时触发此函数，比如GPS被打开
                }
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    // Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                }
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000, 0,locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null){
                latitude = location.getLatitude(); //经度
                longitude = location.getLongitude(); //纬度
                // lat.setText("lat: " + latitude);
                // lng.setText("lng: " + longitude);
            }
        }
        // GPS related -E
    }
}
