package storiesofcommonman.in.herody_beta;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by WIN on 2/22/2017.
 */
public class Uber implements LoaderManager.LoaderCallbacks<String >
{
    private static String time_Data="";
    private static final int uberLoadId=3333;

    public static String UberJson="null";
    private Context context;
    placeUber p;
    public Uber(Context context1,placeUber pla)
    {
        context=context1;
        p=pla;
    }
    public static String getUberUrl(LatLng from,LatLng to)
    {
        return "https://api.uber.com/v1.2/estimates/price?start_latitude="+from.latitude+"&start_longitude="+from.longitude+"&end_latitude" +
                "="+to.latitude+"&end_longitude="+to.longitude;
    }
    public static String gettimeUberUrl(LatLng from,LatLng to)
    {
        return "https://api.uber.com/v1.2/estimates/time?start_latitude="+from.latitude+"&start_longitude="+from.longitude+"&end_latitude" +
                "="+to.latitude+"&end_longitude="+to.longitude;
    }
    public void getUberEstimate(LoaderManager loaderManager,String url)
    {

        Loader<String> olaLoader =loaderManager.getLoader(uberLoadId);

        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        if (olaLoader == null) {
            loaderManager.initLoader(uberLoadId, bundle, this);

        } else {
            loaderManager.restartLoader(uberLoadId,bundle, this);
        }
    }


    @Override
    public Loader<String> onCreateLoader(int id,final Bundle args) {

        return new AsyncTaskLoader<String>(context) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }
            @Override
            public String loadInBackground() {
                StringBuilder stringBuilder=new StringBuilder();

                String u="";
                u=args.getString("url");
                if(u!=null) {
                    try
                    {

                        String x="9jjnQQkw_ZQuZiXZ3vimFO9CqEK0fVqsXwAEoccX";
                        URL url = new URL(u);
                        HttpsURLConnection urlConnection=(HttpsURLConnection)url.openConnection();

                        urlConnection.setRequestProperty("Authorization","Token "+x);
                        //         urlConnection.setRequestProperty("Accept-Language","en_US");
                        // urlConnection.setRequestProperty("Content-Type","application/json");
                        BufferedReader reader;
                        //  int r=urlConnection.getResponseCode();



                        try {
                            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        }catch (IOException e)
                        {

                            e.printStackTrace();
                            reader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));


                        }








                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line + "\n");

                        }


                        {
                            String u_Time=u.replace("price","time");URL url_time = new URL(u_Time);

                            HttpsURLConnection urlConnection_time=(HttpsURLConnection)url_time.openConnection();

                            urlConnection_time.setRequestProperty("Authorization","Token "+x);
                            //         urlConnection.setRequestProperty("Accept-Language","en_US");
                            // urlConnection.setRequestProperty("Content-Type","application/json");
                            BufferedReader reader_time;
                            //  int r=urlConnection.getResponseCode();



                            try {
                                reader_time = new BufferedReader(new InputStreamReader(urlConnection_time.getInputStream()));
                            }catch (IOException e)
                            {

                                e.printStackTrace();
                                reader_time = new BufferedReader(new InputStreamReader(urlConnection_time.getErrorStream()));


                            }





                            StringBuilder s=new StringBuilder();
                            String line1 = null;
                            while ((line1 = reader_time.readLine()) != null) {
                                s.append(line1 + "\n");

                            }
                            time_Data=s.toString();



                        }

                        return stringBuilder.toString();

                    }
                    catch (IOException e)
                    {

                        e.printStackTrace();return  null;
                    }

                }
                else return null;




            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        List<String > categories= addUberCategories(data);
        p.Uberplaced(categories, data, false);

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
    private static List<String> addUberCategories(String json)
    {

        List<String> categories=new ArrayList<>();

        try
        {
            JSONObject reader = new JSONObject(json);
            JSONArray prices=reader.getJSONArray("prices");
            String x="";
            for(int i=0;i<prices.length();i++)
            {
                JSONObject prices_object=prices.getJSONObject(i);
                categories.add(i,prices_object.getString("display_name"));

            }
            return categories;

        }
        catch (Exception e)
        {
            e.printStackTrace();return null;
        }
    }
    public static uberDetails getUberDetails(String json,String categoeis_string)
    {
        try {
            JSONObject reader = new JSONObject(json);
            JSONArray rideEstimate=reader.getJSONArray("prices");
            uberDetails uberds=null;
            uberds=new uberDetails();
            for(int i=0;i<rideEstimate.length();i++)
            {
                JSONObject first=rideEstimate.getJSONObject(i);

                String x=first.getString("display_name");
                if(x.equalsIgnoreCase(categoeis_string)) {
                    uberds.setCategory(x);
                    uberds.setDistance(first.getDouble("distance"));
                    uberds.setTravelTime(first.getInt("duration") / 60);
                    uberds.setMinAmount(first.getString("low_estimate"));
                    uberds.setMaxAmount(first.getString("high_estimate"));
                    uberds.setCurrency(first.getString("currency_code"));
                    break;
                }
            }
            try
            {JSONObject readert = new JSONObject(time_Data);
                JSONArray times=readert.getJSONArray("times");
                for(int i=0;i<times.length();i++)
                {
                    JSONObject c=times.getJSONObject(i);
                    String x=c.getString("localized_display_name");
                    if(x.equalsIgnoreCase("uberGo"))
                    {
                        uberds.setEta(c.getString("estimate"));
                    }
                }

            }catch (Exception e)
            {

                e.printStackTrace();
            }
            return uberds;
        }
        catch (Exception e)
        {
            e.printStackTrace();return  null;
        }




    }
}
