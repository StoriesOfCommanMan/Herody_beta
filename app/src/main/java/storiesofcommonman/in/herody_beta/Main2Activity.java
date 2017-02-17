package storiesofcommonman.in.herody_beta;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,LocationListener,placeAddress {
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation=null;
    private static final int permissionCheck =5489;
    private boolean isGpsEnabled=false;
    private Marker marker=null;
    private FrameLayout framelayout2;
    private PlaceAutocompleteFragment autocompleteFragment;private PlaceAutocompleteFragment to;
    private GoogleMap.OnMapClickListener onMapClickListener;
    String pickupAddress="",dropAddress="";
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    private LatLng pickup,drop;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        framelayout2=(FrameLayout)findViewById(R.id.frameLayout2);framelayout2.setVisibility(View.GONE);

        onMapClickListener= new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        };
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(Main2Activity.this);
        to=(PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_2);
        to.setBoundsBias(BOUNDS_INDIA);
        to.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                drop = place.getLatLng();
                dropAddress = place.getAddress().toString();
            }

            @Override
            public void onError(Status status) {

            }
        });
        to.setHint("Destination");
        autocompleteFragment=(PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setBoundsBias(BOUNDS_INDIA);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                pickupAddress = place.getAddress().toString();
                pickup = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                changeMarker(pickup.latitude, pickup.longitude);
                framelayout2.setVisibility(View.VISIBLE);


            }

            @Override
            public void onError(Status status) {

            }
        });
        setHint("Getting Your Location...");
        askPermission();
        mLastLocation=checkGps1();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mGoogleApiClient!=null)
        {
            if(!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
        }
    }

    private void setHint(String x)
    {
        autocompleteFragment.setHint(x);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                mMap.setOnMapClickListener(onMapClickListener);
        if (mLastLocation != null) {

            changeMarker();

        }
    }
    private void changeMarker()
    {

        if(mLastLocation!=null) {

            LatLng now = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            if(marker!=null)marker.remove();
            marker= mMap.addMarker(new MarkerOptions().position(now).title("Your Position"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(now, (float) 16.0));
        }

    }
    private void changeMarker(double lat,double lon)
    {


        if(marker!=null)marker.remove();
        marker= mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title("Your Position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), (float) 16.0));


    }
    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            isGpsEnabled=false;

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permissionCheck);

        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case permissionCheck: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isGpsEnabled=true;
                } else {
                    isGpsEnabled=false;
                }

            }


        }
    }
    private Location checkGps1()
    {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;


        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);


        } catch(Exception ex) { Toast.makeText(getApplicationContext(),"Provider not permitted",Toast.LENGTH_SHORT).show();}


        if(!gps_enabled)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("GPS Not Available");
            alertDialog.setMessage("Please Enable GPS to set Your Pick Up Destination");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ENABLE",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),1);
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            isGpsEnabled=false;
                            setHint("Pick Up Location...");
                            dialog.cancel();
                        }
                    });
            alertDialog.show();


        }
        else
        {
            startgoogleApi();
            isGpsEnabled=true;
        }
        return null;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case 1:{

                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                boolean gps_enabled = false;


                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch(Exception ex) { Toast.makeText(getApplicationContext(),"Provider not permitted",Toast.LENGTH_SHORT).show();}

                if(!gps_enabled )
                {
                    setHint("Enter PickUp Location...");
                    isGpsEnabled=false;
                }
                else {

                    startgoogleApi();
                    isGpsEnabled=true;

                }
            }

        }
    }

    private void startgoogleApi()
    {
        if(!(checkWifi()||checkNetwork())) {

            Toast.makeText(getApplicationContext(),"Your Network seems Off,Enable it and Try Again",Toast.LENGTH_SHORT).show();
        }
        GoogleApiClient.ConnectionCallbacks connectionCallbacks=new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {

                Location location1=null;Location location=null;
                Toast.makeText(getApplicationContext(),"s",Toast.LENGTH_SHORT).show();
                try {



                    {
                        startLocationUpdates();
                        final LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);


                        Location locationg=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        Location locationn=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if(locationg!=null&&locationn!=null) {


                            if (locationg.getTime() > locationn.getTime()) {
                                location=locationg;
                            }
                            else
                            {
                                location=locationn;
                            }

                        }
                        else if(locationg!=null)
                        {
                            location=locationg;
                        }
                        else if(locationn!=null) {
                            location = locationn;
                        }

                    }
                    if((location1!=null)&&(location!=null))
                    {
                        if(location1.getTime()>location.getTime())
                        {
                            mLastLocation=location1;
                        }
                        else{
                            mLastLocation=location;
                        }

                    }

                }catch (SecurityException e)
                {

                    e.printStackTrace();
                }
                startLocationUpdates();
                setAddress(mLastLocation);


            }

            @Override
            public void onConnectionSuspended(int i) {

                Toast.makeText(getApplicationContext(),"S2",Toast.LENGTH_SHORT).show();
            }
        };
        GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener=new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(getApplicationContext(),"S3",Toast.LENGTH_SHORT).show();

            }
        };
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();startLocationUpdates();


    }
    protected void startLocationUpdates() { if (isGpsEnabled) {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,createLocationRequest(), this);

        }catch (SecurityException e)
        {
            isGpsEnabled=false;
        }
    }}
    protected LocationRequest createLocationRequest() {
        LocationRequest lrequest = new LocationRequest();
        lrequest.setInterval(1000000);
        lrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return lrequest;
    }
    private void setAddress (Location loc) {


        if(mLastLocation==null)return;
        else loc=mLastLocation;

        pickup=new LatLng(loc.getLatitude(),loc.getLongitude());
        parseAddress parse=new parseAddress(loc,getApplicationContext(),this,true);
        parse.startAddress(getSupportLoaderManager());

    }
    private boolean checkWifi()
    {

        WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }
    private boolean checkNetwork()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void placed(String Address,boolean isFrom) {
        if(isFrom)
        {
            autocompleteFragment.setText(Address);
            pickupAddress=Address;
            framelayout2.setVisibility(View.VISIBLE);
        }
        else
        {
            to.setText(Address);
        }
    }
    @Override
    public void onLocationChanged(Location location) {

        mLastLocation=location;
        setAddress(mLastLocation);
        setMap();
    }
    private void setMap()
    {
        if(checkWifi()||checkNetwork()) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        else {
            Toast.makeText(getApplicationContext(),"Your Network seems Off,Enable it and Try Again",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onPause()
    {
        super.onPause();;
        if(mGoogleApiClient!=null)
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
    }
    }
