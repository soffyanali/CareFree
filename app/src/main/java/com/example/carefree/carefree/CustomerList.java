package com.example.carefree.carefree;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;
import static nixer.nixer.IPAddress.ipadd;

public class CustomerList extends AppCompatActivity {

    public static final String PREFS_NAME = "loginusersdetails";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    JSONParser jParser = new JSONParser();
    HashMap<Integer,ArrayList<String>> messages=new HashMap<Integer,ArrayList<String>>();
    JSONArray users = null, users1 = null;
    private ArrayAdapter<String> listAdapter;
    ListView list;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showlistusers);

        try {

            if (Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            displayList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void displayList()
    {

        ArrayList<String> names=senderusersOnly();
        preferences = getSharedPreferences("loginusersdetails", MODE_PRIVATE);
        String sharedname = preferences.getString("name", null);

        Intent intent = getIntent();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        JSONObject json = jParser.makeHttpRequest(ipadd+"selectuserdetails.php", "GET", params);
        int count=0;
        try {
            users = json.optJSONArray("result");
            for (int i = 0; i < users.length(); i++) {
                JSONObject c = users.getJSONObject(i);
                String name1=c.getString("name");

                if(!sharedname.equals(name1))
                {
                    if(names.contains(name1))
                    {
                        count++;
                    }
                }
            }
        }catch (Exception e)
        {

        }

        // Check your log cat for JSON reponse
        users1 = json.optJSONArray("result");
        int cn=1;
        final String[] namelist = new String[count];
        final String[] userlatitutelist = new String[count];
        final String[] userlongitutelist = new String[count];
        final String[] professionlist = new String[count];
        final String[] phonenolist = new String[count];
        final String[] emailidlist = new String[count];
        final String[] loginvaluelist = new String[count];
        final String[] ratingvaluelist = new String[count];

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

                if(!sharedname.equals(name1))
                {
                    if(names.contains(name1))
                    {
                        namelist[countp]=name1;
                        userlatitutelist[countp]=userlatitute;
                        userlongitutelist[countp]=userlongitute;
                        professionlist[countp]=profession1;
                        phonenolist[countp]=phoneno;
                        emailidlist[countp]=emailid;
                        loginvaluelist[countp]=loginvalue;
                        ratingvaluelist[countp]=ratingvalue;

                        cn++;
                        countp++;
                    }
                }

            }
        }catch (Exception e)
        {

        }
        //end add map markers

        ChatCustomList adapter = new ChatCustomList(ChatUserListProf.this, namelist, ratingvaluelist);
        list=(ListView)findViewById(R.id.mainListViewmessage);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(ChatUserListProf.this, "You Clicked at " +namelist[+ position], Toast.LENGTH_SHORT).show();
                String stringText;

                Intent i=new Intent(ChatUserListProf.this,ChatApplication.class);
                i.putExtra("name", namelist[+ position]);
                i.putExtra("profession",professionlist[+position] );
                i.putExtra("phoneno",phonenolist[+position]);
                i.putExtra("emailid",emailidlist[+position]);
                i.putExtra("loginvalue",loginvaluelist[+position]);
                startActivity(i);
            }
        });
    }


    private ArrayList<String> senderusersOnly() {

        preferences = getSharedPreferences("loginusersdetails", MODE_PRIVATE);
        String profname = preferences.getString("name", null);
        // products JSONArray

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        JSONObject json = jParser.makeHttpRequest(ipadd + "selectmessages.php", "GET", params);

        // products JSONArray
        JSONArray users1 = null;
        ArrayList<String> names = new ArrayList<String>();
        // Check your log cat for JSON reponse
        users1 = json.optJSONArray("result");

        int countp = 0;
        try {

            for (int i = 0; i < users1.length(); i++) {
                JSONObject c = users1.getJSONObject(i);

                String touser = c.getString("touser");
                String fromuser = c.getString("fromuser");

                if (profname.equals(touser)) {
                    countp++;
                    names.add(fromuser);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }


}
