package storiesofcommonman.in.herody_beta;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //In this activity , first check whether GPS is enabled or not and internet Connection is enabled or not
        //Do your work in seperate method


    }
    private boolean checkGPS()
    {
        return true;

    }
    private boolean checkWifiNetwork()
    {


        WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return ( wifi.isWifiEnabled())||(activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }
}
