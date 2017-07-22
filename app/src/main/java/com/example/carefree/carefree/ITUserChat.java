package com.example.carefree.carefree;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ITUserChat extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout main, matrix;
    Button sendButtonl;
    EditText sendText;
    private static Context context;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    public static final String PREFS_NAME= "loginusersdetails";
    HashMap<Integer,ArrayList<String>> messages=new HashMap<Integer,ArrayList<String>>();
    SharedPreferences preferences;

    // products JSONArray
    JSONArray users = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatactivity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Intent intent = getIntent();
        final String toname = intent.getStringExtra("name");

        preferences = getSharedPreferences("loginusersdetails", MODE_PRIVATE);
        final String fromname= preferences.getString("name", null);

        LinearLayout ll = (LinearLayout) findViewById(R.id.layout1);
        Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = 680;

        View view = findViewById(R.id.ll1);
        ImageButton sendButton = (ImageButton) view.findViewById(R.id.sendButton);
        final EditText sendText = (EditText) view.findViewById(R.id.sendText);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertToDatabase(toname,fromname,sendText.getText().toString().trim());
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            }
        });

        HashMap<Integer,ArrayList<String>> fmessages=new HashMap<Integer,ArrayList<String>>();
        messages=doInBackground1();
        int messagecounter=0;
        int j=0;
        for (int i = 0; i < messages.size(); i++)
        {
            ArrayList<String> newArr=messages.get(i);

            if((newArr.get(0).equals(fromname)&&newArr.get(1).equals(toname))||(newArr.get(0).equals(toname)&&newArr.get(1).equals(fromname)))
            {
                messagecounter++;
                fmessages.put(j,newArr);
                j++;
            }

        }

        for (int i = 0; i < messagecounter; i++) {
            LinearLayout l = new LinearLayout(this);
            l.setOrientation(LinearLayout.VERTICAL);
            EditText et = new EditText(this);
            ArrayList<String> newArr=fmessages.get(i);

            if(newArr.get(0).equals(toname))
            {
                et.setText(newArr.get(2));
                et.setPadding(30,5,30,5);
                et.setEnabled(false);
                et.setTextColor(Color.parseColor("#000000"));
                et.setBackgroundColor(Color.parseColor("#f0f0f0"));
                et.setBackgroundResource(R.drawable.rounded_corner1);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                l.addView(et, lp);
                lp.setMargins(380,20,5,5);
            }else
            {
                et.setText(newArr.get(2));
                et.setEnabled(false);
                et.setTextColor(Color.parseColor("#000000"));
                et.setPadding(30,5,30,5);
                et.setBackgroundColor(Color.parseColor("#dddddd"));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                l.addView(et, lp);
                lp.setMargins(10,20,5,5);
                et.setBackgroundResource(R.drawable.rounded_corner);
            }

            ll.addView(l);
        }
    }
    public static Context getAppContext() {
        return ITUserChat.context;
    }

    @Override
    public void onClick(View view) {

    }

    protected HashMap<Integer, ArrayList<String>> doInBackground1()
    {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(GlobalDomain.domainadd+"selectmessages.php", "GET", params);

        // Check your log cat for JSON reponse

        try {

            Log.d("All Users: ", json.toString());

            //sharedpreferences = getSharedPreferences("loginusersdetails", Context.MODE_PRIVATE);
            users = json.optJSONArray("result");
            // looping through All Products
            for (int i = 0; i < users.length(); i++) {
                JSONObject c = users.getJSONObject(i);

                ArrayList<String> newArr=new ArrayList<String>();

                String touser = c.getString("touser");
                String fromuser = c.getString("fromuser");
                String messagetext = c.getString("messagetext");
                newArr.add(0,touser);
                newArr.add(1,fromuser);
                newArr.add(2,messagetext);

                messages.put(i,newArr);
            }

        }catch (Exception e)
        {

        }
        return messages;
    }

    private void insertToDatabase(final String touser, final String fromuser, final String messagetext) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("touser", touser));
                nameValuePairs.add(new BasicNameValuePair("fromuser", fromuser));
                nameValuePairs.add(new BasicNameValuePair("messagetext", messagetext));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(GlobalDomain.domainadd+"insertmessage.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                } catch (ClientProtocolException e) {

                } catch (Exception e) {

                }
                return "success";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(messagetext,"","","","","");
    }

}