package storiesofcommonman.in.herody_beta;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by WIN on 2/10/2017.
 */
public class parseAddress implements LoaderManager.LoaderCallbacks<String > {
    public String address="";
    private double latitude=0;private double longitude=0;
    private final int LoaderId=7586;
    private Context context;
    private placeAddress p;
    boolean isFrom=true;
    public parseAddress(Location location,Context context1,placeAddress place,boolean is)
    {
        latitude=location.getLatitude();longitude=location.getLongitude();
        p=place;
        context=context1;
        isFrom=is;

    }
    private String url()
    {
        return "http://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude;
    }
    public void startAddress(LoaderManager loaderManager)
    {

        Loader<String> olaLoader =loaderManager.getLoader(LoaderId);

        Bundle bundle=new Bundle();
        bundle.putString("url",url());
        if (olaLoader == null) {
            loaderManager.initLoader(LoaderId, bundle, this);

        } else {
            loaderManager.restartLoader(LoaderId,bundle, this);
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
                    {                        URL url = new URL(u);
                        HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                        BufferedReader reader=null;
                        try {
                            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        }catch (IOException e)
                        {   e.printStackTrace();
                            try {
                                reader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                            }catch (RuntimeException ex)
                            {
                                ex.printStackTrace();

                            }


                        }






                        if(reader!=null) {
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                stringBuilder.append(line + "\n");

                            }
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
        String address= formatAddress(data);
        p.placed(address, isFrom);




    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
    private  String formatAddress(String json)
    {

        String Address="";

        try
        {
            JSONObject reader = new JSONObject(json);
            JSONArray results=reader.getJSONArray("results");

            JSONObject results_object=results.getJSONObject(0);
            Address=results_object.getString("formatted_address");
            if(!Address.isEmpty()) {
                int x = Address.indexOf(",");
                Address = Address.substring(x+1);

                address = Address;
                return Address;
            }else return null;

        }
        catch (Exception e)
        {
            e.printStackTrace();return null;
        }
    }

}
