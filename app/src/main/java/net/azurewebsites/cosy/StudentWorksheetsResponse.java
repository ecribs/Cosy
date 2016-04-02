package net.azurewebsites.cosy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StudentWorksheetsResponse extends ActionBarActivity
{
    int[] Amount = {};
    ListView StudentList;
    String[] Users ={};
    int WorksheetID;
    JSONObject jsonObject;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_worksheets_response);

        StudentList = (ListView) findViewById(R.id.StudentWorksheet);
        Bundle topicdata = getIntent().getExtras();
        WorksheetID = topicdata.getInt("WorksheetID");
        Log.v("recieved:", WorksheetID+"");

        try {

            jsonObject = new studentlist().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        try {


            try {
                jsonArray = jsonObject.getJSONArray("Username");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Users = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    Users[i] = jsonArray.getString(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e)
        {
            Users[0]= "nothing to display";

        }

        try {


            try {
                jsonArray = jsonObject.getJSONArray("Num_Q");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Amount = new int[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    Amount[i] = jsonArray.getInt(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e)
        {
            Amount[0]= 0;

        }


        ListAdapter Worksheet = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Users);
        StudentList.setAdapter(Worksheet);

        StudentList.setOnItemClickListener
                (
                        new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                Intent i = new Intent(getApplicationContext(), UserTest.class);
                                i.putExtra("WorksheetID", WorksheetID);
                                i.putExtra("Amount", Amount[position]);
                                i.putExtra("Username", Users[position]);

                                startActivity(i);
                            }
                        }

                );



    }


    private class studentlist extends AsyncTask<Void, Void, JSONObject> {
        //ResultSet Book;

        private ProgressDialog pdialog;

        @Override
        protected void onPreExecute() {
            Log.d("", "we are executing");
            pdialog = new ProgressDialog(StudentWorksheetsResponse.this);
            pdialog.setCancelable(false);
            pdialog.setMessage("Getting Books...");
            showdialog();



        }


        @Override
        protected JSONObject doInBackground(Void... params)

        {
            String result = null;
            JSONObject jsonObject = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/studentswork.php");



                Log.v("values being sent:", WorksheetID+"");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("WorksheetID", WorksheetID+""));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();

                Log.d("", "got a response");

                result = EntityUtils.toString(entity);
                Log.v("", result);
                jsonObject = new JSONObject(result);
                // JSONObject jsonObject = obj.getJSONObject(0);


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            hidedialog();
        }


        protected void showdialog() {
            if (pdialog.isShowing()) {
                pdialog.show();

            }

        }

        private void hidedialog() {
            if (pdialog.isShowing()) {
                pdialog.dismiss();

            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.home:
                Intent homeintent = new Intent(this, MainActivity.class);
                startActivity(homeintent);
            default:
                return super.onOptionsItemSelected(item);


        }


    }


}
