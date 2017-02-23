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
 * Created by WIN on 2/23/2017.
 */

public  class Ola implements LoaderManager.LoaderCallbacks<String>{
    private Context context;
    private final int olaLoadId=4534;
    placeOla p;
    String u;
    public Ola(Context context1,placeOla pla)
    {
        context=context1;
        p=pla;
    }
    private final String XAPPTOKEN="48f37b30b5094f808718a248bb1d90de";

    public static String OlaEstimate(LatLng from,LatLng to)
    {

        return "https://sandbox-t1.olacabs.com/v1/products?pickup_lat="+from.latitude+"&pickup_lng="+from.longitude+"&drop_lat="+to.latitude+"&drop_lng="+to.longitude;

    }
    public void getOlaEstimate(LoaderManager loaderManager,String url)
    {

        Loader<String> olaLoader =loaderManager.getLoader(olaLoadId);

        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        if (olaLoader == null) {
            loaderManager.initLoader(olaLoadId, bundle, this);

        } else {
            loaderManager.restartLoader(olaLoadId,bundle, this);
        }
    }

    @Override
    public Loader onCreateLoader(int id, final Bundle args) {


        return new AsyncTaskLoader(context) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                StringBuilder stringBuilder=new StringBuilder();

                u=args.getString("url");
                if(u!=null) {
                    try
                    {

                        URL url = new URL(u);
                        HttpsURLConnection urlConnection=(HttpsURLConnection)url.openConnection();
                        urlConnection.setRequestProperty("X-APP-TOKEN ",XAPPTOKEN.toString());
                        BufferedReader reader;
                        int r=urlConnection.getResponseCode();


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
    public void onLoadFinished(Loader loader,String data) {

        List<String > categories= addOlaCategories(data);
        p.Olaplaced(categories,data,true);

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    public static OlaDetails getDetails(String json,String categoeis_string)
    {
        try {
            JSONObject reader = new JSONObject(json);

            JSONArray categoriesArray=reader.getJSONArray("categories");
            int eta=0;
            for(int i=0;i<categoriesArray.length();i++)
            {

                JSONObject first = categoriesArray.getJSONObject(i);
                eta=first.getInt("eta");


            }
            JSONArray rideEstimate=reader.getJSONArray("ride_estimate");

            OlaDetails olaDetails=null;
            for(int i=0;i<rideEstimate.length();i++)
            {
                JSONObject first=rideEstimate.getJSONObject(i);
                olaDetails=new OlaDetails();
                String x=first.getString("category");
                olaDetails.setEta(eta);
                if(x.equalsIgnoreCase(categoeis_string)) {
                    olaDetails.setCategory(x);
                    olaDetails.setDistance(first.getDouble("distance"));
                    olaDetails.setTravelTime(first.getInt("travel_time_in_minutes"));
                    olaDetails.setMinAmount(first.getString("amount_min"));
                    olaDetails.setMaxAmount(first.getString("amount_max"));
                    break;

                }

            }
            return olaDetails;
        }
        catch (Exception e)
        {
            e.printStackTrace();return  null;
        }



    }
    private List<String> addOlaCategories(String json)
    {

        List<String> categories=new ArrayList<>();

        try {
            JSONObject reader = new JSONObject(json);
            JSONArray ride_estimate = reader.getJSONArray("ride_estimate");
            for(int i=0;i<ride_estimate.length();i++)
            {
                JSONObject first=ride_estimate.getJSONObject(i);
                categories.add(i,first.getString("category"));


            }
            return categories;

        }catch (Exception e)
        {
            e.printStackTrace();return null;
        }

    }

}
