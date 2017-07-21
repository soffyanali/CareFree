package com.example.carefree.carefree;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ITExpertOne extends AppCompatActivity {

    //Create Variables
    protected TextView textview_professionalname, txtvie_activenow, txtview_phoneno, textviewprofession, textView_emailid;
    Button submitbuttonrating, chatProfessional;
    double longitute;
    double latitude;
    SharedPreferences preferences;
    RatingBar ratingBar;
    JSONParser jParser = new JSONParser();
    SharedPreferences sharedpreferences;
    JSONArray usersrating1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oneITexpert);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String profession = intent.getStringExtra("profession");
        final String phoneno = intent.getStringExtra("phoneno");
        final String emailid = intent.getStringExtra("emailid");
        final String loginvalue = intent.getStringExtra("loginvalue");
        final String[] stringfeedbackrating = new String[1];

        textview_professionalname = (TextView) findViewById(R.id.textview_professionalname);
        textviewprofession = (TextView) findViewById(R.id.textviewprofession);
        txtview_phoneno = (TextView) findViewById(R.id.txtview_phoneno);
        textView_emailid = (TextView) findViewById(R.id.textView_emailid);
        txtvie_activenow = (TextView) findViewById(R.id.txtvie_activenow);
        submitbuttonrating = (Button) findViewById(R.id.submitbuttonrating);
        chatProfessional = (Button) findViewById(R.id.chatProfessional);

        textview_professionalname.setText(name);
        textviewprofession.setText(profession);
        txtview_phoneno.setText(phoneno);
        textView_emailid.setText(emailid);

        if (loginvalue.equals("y")) {
            txtvie_activenow.setText("Professional is active now");
        } else {
            txtvie_activenow.setText("Professional is not active now");
        }

        boolean flag=alreadyRated();
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(0.0f);

        if(flag==true)
        {
            submitbuttonrating.setText("Already Submitted");
            submitbuttonrating.setEnabled(false);
            ratingBar.setEnabled(false);
        }

        txtview_phoneno.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + String.valueOf(Integer.parseInt(phoneno))));
                if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });

        chatProfessional.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

            /*    Intent i = new Intent(OneProfessional.this, ChatApplication.class);
                i.putExtra("name", name);
                i.putExtra("profession", profession);
                i.putExtra("phoneno", phoneno);
                i.putExtra("emailid", emailid);
                i.putExtra("loginvalue", loginvalue);
                startActivity(i);*/
            }
        });


        textView_emailid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{textView_emailid.getText().toString()});
                i.putExtra(Intent.EXTRA_SUBJECT, "Add your subject");
                i.putExtra(Intent.EXTRA_TEXT, "Hi " + name + "," + "I am enquiring about your services, if you could contact me on;");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ITExpertOne.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }


            }
        });


        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                stringfeedbackrating[0] = String.valueOf(rating);

            }
        });

        submitbuttonrating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // insert feedback value with standarduser, professionaluser into userfeedback table

                preferences = getSharedPreferences("loginusersdetails", MODE_PRIVATE);
                String standardname = preferences.getString("name", null);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("professionalname", name));
                nameValuePairs.add(new BasicNameValuePair("standardname", standardname));
                nameValuePairs.add(new BasicNameValuePair("rating", stringfeedbackrating[0]));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(GlobalDomain.domainadd+ "updatefeedback.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error= " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                //


                //find average rating[get value from userfeedback table]

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject json = jParser.makeHttpRequest(GlobalDomain.domainadd + "selectuserratings.php", "GET", params);
                int count = 0;
                double sum = 0;
                double avg = 0;

                try {

                    Log.d("All Users: ", json.toString());

                    sharedpreferences = getSharedPreferences("loginusersdetails", Context.MODE_PRIVATE);
                    usersrating1 = json.optJSONArray("result");
                    // looping through All Products


                    for (int i = 0; i < usersrating1.length(); i++) {
                        JSONObject c = usersrating1.getJSONObject(i);

                        String tname = c.getString("professionalname").trim();
                        String urating = c.getString("rating");

                        if (tname.toString().trim().equals(name))   //count average of particular individual professional user
                        {
                            //if standard then ask for what they looking for or professional then show their current location
                            sum = sum + Double.parseDouble(urating);
                            count++;
                        }

                    }
                    avg = sum / count;
                } catch (Exception e) {

                }


                // update feedback value  into user table

                List<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>();
                nameValuePairs2.add(new BasicNameValuePair("name", name));
                nameValuePairs2.add(new BasicNameValuePair("avgrating", String.valueOf(avg)));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(GlobalDomain.domainadd  + "updateratings.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error= " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                //
                Toast.makeText(getApplicationContext(), "Feedback Submitted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ITExpertOne.this, CustomerMap.class);
                intent.putExtra("profession", profession);
                startActivity(intent);
                finish();
            }
        });

    }

    private boolean alreadyRated() {

        Intent intent = getIntent();
        final String profname = intent.getStringExtra("name");

        preferences = getSharedPreferences("loginusersdetails", MODE_PRIVATE);
        String stdname = preferences.getString("name", null);
        // products JSONArray

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        JSONObject json = jParser.makeHttpRequest(GlobalDomain.domainadd + "selectfeedback.php", "GET", params);

        // products JSONArray
        JSONArray users1 = null;

        // Check your log cat for JSON reponse
        users1 = json.optJSONArray("result");

        int countp = 0;
        try {

            for (int i = 0; i < users1.length(); i++) {
                JSONObject c = users1.getJSONObject(i);

                String professionalname = c.getString("professionalname");
                String standardname = c.getString("standardname");

                if (stdname.equals(standardname) && profname.equals(professionalname)) {
                    countp++;
                }

            }
        } catch (Exception e) {

        }

        if(countp>0)
            return true;
        else
            return false;

    }

}