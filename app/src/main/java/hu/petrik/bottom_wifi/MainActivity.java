package hu.petrik.bottom_wifi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.Format;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private TextView info;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.wifi_on:
                    //android 10 (api 29) és afelett
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                        info.setText("nincs jogosultság a wofo állapot módosytásásra");
                        Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
                        startActivityForResult(panelIntent, 0);
                    } else {
                        //CHANGE WIFI STATE ENGEDÉLY KELL, CSAK ANDROID 10 ALATT MŰKÖDIK
                        wifiManager.setWifiEnabled(true);
                        info.setText("wifi bekapcsolva");
                    }
                    break;
                case R.id.wifi_off:
                    //android 10 (api 29) és afelett
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                        info.setText("nincs jogosultság a wofo állapot módosytásásra");
                        Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                        startActivityForResult(panelIntent, 0);
                    } else {
                        //CHANGE WIFI STATE ENGEDÉLY KELL, CSAK ANDROID 10 ALATT MŰKÖDIK
                        wifiManager.setWifiEnabled(false);
                        info.setText("wifi kikapcsolva");
                    }
                    break;
                case R.id.wifi_info:
                    ConnectivityManager ConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = ConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (networkInfo.isConnected()) {
                        int ipnumber = wifiInfo.getIpAddress();
                        String ipaddress = Formatter.formatIpAddress(ipnumber);
                        info.setText("IP:" + ipaddress);
                    } else {
                        info.setText("new csatlakoztál wifi hálózathoz");
                    }
                    break;
            }

            return false;
        });
    }

    private void init() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        info = findViewById(R.id.info);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED || wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
                info.setText("wifi Bekapcsolva");
            } else if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED || wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
                info.setText("wifi Kikapcsolva");
            }
        }
    }
}