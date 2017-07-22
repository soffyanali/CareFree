package com.example.carefree.carefree;

import android.annotation.TargetApi;
        import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.IntentSender;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.location.Location;
        import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.StrictMode;
        import android.support.annotation.Nullable;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.TextView;
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

        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.NameValuePair;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.message.BasicNameValuePair;
        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.io.BufferedInputStream;
        import java.io.BufferedOutputStream;
        import java.io.BufferedReader;
        import java.io.ByteArrayOutputStream;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.HashMap;
        import java.util.List;

        import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class ITExpertMap extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationSource.OnLocationChangedListener mListener;
    double longitute;
    double latitude;
    Button chatProfpp;
    SharedPreferences preferences;
    public static final String PREFS_NAME = "loginusersdetails";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    JSONParser jParser = new JSONParser();
    HashMap<Integer,ArrayList<String>> messages=new HashMap<Integer,ArrayList<String>>();
    JSONArray users = null, users1 = null;
    private ArrayAdapter<String> listAdapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itexpertmap);

        try {

            setUpMapIfNeeded();
            updateLocation();
            if(haveNetworkConnection())
            {
            }else
            {
                Toast.makeText(getApplicationContext(),"Please turn your internet connection on...!!!",Toast.LENGTH_LONG).show();
            }

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

        // Update login value it states that user is active

        preferences = getSharedPreferences("loginusersdetails", MODE_PRIVATE);
        String name = preferences.getString("name", null);
        String password = preferences.getString("password", null);
        String login = "y";

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("name", name));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        nameValuePairs.add(new BasicNameValuePair("login", login));

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(GlobalDomain.domainadd+"updatelogindetails.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            // Toast.makeText(getApplicationContext(), "Logout Successfully", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error= " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //
        displayList();

        chatProfpp= (Button) findViewById(R.id.chatProfpp);
        chatProfpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = getSharedPreferences("loginusersdetails", MODE_PRIVATE);
                String name = preferences.getString("name", null);
                Intent i=new Intent(ITExpertMap.this,ChatUserListProf.class);
                i.putExtra("sharename",name);
                startActivity(i);
                finish();
            }
        });

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void updateLocation()
    {
        preferences = getSharedPreferences("loginusersdetails", MODE_PRIVATE);
        String name = preferences.getString("name", null);
        String password = preferences.getString("password", null);


        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("name", name));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        nameValuePairs.add(new BasicNameValuePair("userlatitute", String.valueOf(latitude)));
        nameValuePairs.add(new BasicNameValuePair("userlongitute", String.valueOf(longitute)));

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(GlobalDomain.domainadd+"updatecurrentlocation.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error= " + e.getMessage(), Toast.LENGTH_LONG).show();
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
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.itexpertmapfrag)).
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
