//References:-
// https://developers.google.com/maps/documentation/android-api/code-samples
// https://www.tutorialspoint.com/android/android_google_maps.htm
// https://www.androidhive.info/2013/08/android-working-with-google-maps-v2/
// https://dzone.com/articles/android-tutorial-how-parse

package com.example.carefree.carefree;


import android.annotation.TargetApi;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.IntentSender;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
import android.location.Location;
        import android.location.LocationManager;
import android.os.Build;
        import android.os.Bundle;
        import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AppCompatActivity;
import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;
import android.widget.Toast;
        import android.Manifest;

        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.location.LocationListener;
        import com.google.android.gms.location.LocationRequest;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.LocationSource;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
        import org.json.JSONObject;

import java.text.DecimalFormat;
        import java.util.ArrayList;
import java.util.List;

        import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class CustomerMap extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private LocationSource.OnLocationChangedListener mListener;
    double longitute;
    double latitude;
    JSONParser jParser = new JSONParser();
    JSONArray users = null, users1 = null;
    private ArrayAdapter<String> listAdapter;
    ListView list;
    Button chatStandard;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_map);

        try {

            setUpMapIfNeeded();

            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
            } else {
                showGPSDisabledAlertToUser();
            }


            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(1 * 1000)        // 10 seconds, in milliseconds
                    .setFastestInterval(500); // 1 second, in milliseconds
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }


    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapstandard)).
                    getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        mMap.getUiSettings().setZoomGesturesEnabled(true);
    }


    public void onConnected(Bundle bundle) {
        Location location = null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } else {
            // requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            Toast.makeText(this, "Should ask for the approval/denial here", Toast.LENGTH_LONG).show();
        }

        //Location location = FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void handleNewLocation(Location location) {
        final double currentLatitude = location.getLatitude();
        final double currentLongitude = location.getLongitude();

        preferences = getSharedPreferences("loginusersdetails", MODE_PRIVATE);
        String name = preferences.getString("name", null);

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(name.toUpperCase() + " you are here!");
        mMap.addMarker(options);
        float zoomLevel = 10.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        try {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
        }catch (Exception e)
        {}
        //Toast.makeText(WelcomeScreen.this,"Handle new Location = "+location.getLatitude()+" Langi= "+ location.getLongitude(),Toast.LENGTH_SHORT).show();
        longitute= location.getLongitude();
        latitude=location.getLatitude();

        /////Start Add Map Markers
        Intent intent = getIntent();
        String profession = intent.getStringExtra("profession");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        JSONObject json = jParser.makeHttpRequest(GlobalDomain.domainadd+"userdetails.php", "GET", params);
        int professionalcount=0;
        try {
            users = json.optJSONArray("result");
            for (int i = 0; i < users.length(); i++) {
                JSONObject c = users.getJSONObject(i);
                String profession1 = c.getString("profession");
                if (profession.equals(profession1)) {
                    professionalcount++;
                }
            }
        }catch (Exception e)
        {

        }

        // Check your log cat for JSON reponse
        users1 = json.optJSONArray("result");
        int cn=1;
        final String[] namelist = new String[professionalcount];
        final String[] userlatitutelist = new String[professionalcount];
        final String[] userlongitutelist = new String[professionalcount];
        final String[] professionlist = new String[professionalcount];
        final String[] phonenolist = new String[professionalcount];
        final String[] emailidlist = new String[professionalcount];
        final String[] loginvaluelist = new String[professionalcount];
        final String[] ratingvaluelist = new String[professionalcount];

        int countp=0;
        try {


            for (int i = 0; i < users1.length(); i++) {
                JSONObject c = users1.getJSONObject(i);

                String name1=c.getString("name");
                String password=c.getString("password");
                String accountype=c.getString("account_type");
                String profession1=c.getString("profession");
                String phoneno=c.getString("phoneno");
                String emailid=c.getString("emailid");
                String userlatitute=c.getString("userlatitute");
                String userlongitute=c.getString("userlongitute");
                String loginvalue=c.getString("login");
                String ratingvalue=c.getString("avgrating");

                if(profession.equals(profession1))
                {
                    namelist[countp]=name1;
                    userlatitutelist[countp]=userlatitute;
                    userlongitutelist[countp]=userlongitute;
                    professionlist[countp]=profession1;
                    phonenolist[countp]=phoneno;
                    emailidlist[countp]=emailid;
                    loginvaluelist[countp]=loginvalue;
                    ratingvaluelist[countp]=ratingvalue;

                    if(loginvalue.trim().equals("y")) //if user is active then pin will be painted HUE_GREEN
                    {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(userlatitute), Double.parseDouble(userlongitute)))
                                .title("" + cn + ". " + name1 + " " + profession)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    }else
                    {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(userlatitute), Double.parseDouble(userlongitute)))
                                .title("" + cn + ". " + name1 + " " + profession)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    }
                    cn++;
                    countp++;
                }

            }
        }catch (Exception e)
        {

        }
        if(countp==0)
        {
            Toast.makeText(getApplicationContext(),"No IT Expert available ...!!!",Toast.LENGTH_LONG).show();

        }
        //end add map markers
        final Float[] distance = new Float[countp];

        for(int j=0;j<countp;j++) {

            String clatitude=userlatitutelist[j];
            String clongitude=userlongitutelist[j];

            float[] results = new float[1];
            float result;
            Location.distanceBetween(Double.parseDouble(clatitude),
                    Double.parseDouble(clongitude),
                    latitude,
                    longitute,
                    results);
            result= Float.parseFloat(new DecimalFormat("##.##").format(results[0]));
            distance[j]=results[0];
        }

        ITExpertsList adapter = new ITExpertsList(CustomerMap.this, namelist, ratingvaluelist ,distance);
        list=(ListView)findViewById(R.id.mainListView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(CustomerMap.this, "You Clicked at " +namelist[+ position], Toast.LENGTH_SHORT).show();
                String stringText;

                Intent i=new Intent(CustomerMap.this,ITExpertOne.class);
                i.putExtra("name", namelist[+ position]);
                i.putExtra("profession",professionlist[+position] );
                i.putExtra("phoneno",phonenolist[+position]);
                i.putExtra("emailid",emailidlist[+position]);
                i.putExtra("loginvalue",loginvaluelist[+position]);
                startActivity(i);
            }
        });
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override

    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
