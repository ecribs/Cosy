package net.azurewebsites.cosy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


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

public class Worksheet extends ActionBarActivity
{

    int TopicID;
    int[] WorksheetID;
    String[] WorksheetName={""};
    JSONObject jsonObject = null;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worksheet);
        JSONArray jsonArray = null;
        Bundle topicdata = getIntent().getExtras();
        TopicID = topicdata.getInt("TopicID");
        String nothing = "Nothing To Display";



            try {

                jsonObject = new getWorksheet().execute(TopicID).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        Log.v("temp converted","to string");




        assert jsonObject != null;
        try {

                try {
                    jsonArray = jsonObject.getJSONArray("WorksheetName");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.v("nothing", "nothing");
                    WorksheetName[0] = nothing;
                }
                assert jsonArray != null;
                WorksheetName = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        WorksheetName[i] = jsonArray.getString(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        WorksheetName[0] = nothing;
                        Log.v("nothing", "nothing");

                    }
                }
            } catch (Exception e) {
                WorksheetName[0] = nothing;
                Log.v("nothing", "nothing");
            }

            try {

                try {
                    jsonArray = jsonObject.getJSONArray("WorksheetID");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.v("nothing", "nothing");
                    WorksheetName[0] = nothing;
                }
                assert jsonArray != null;
                WorksheetID = new int[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        WorksheetID[i] = jsonArray.getInt(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        WorksheetName[0] = nothing;
                        Log.v("nothing", "nothing");

                    }
                }
            } catch (Exception e) {
                WorksheetName[0] = nothing;
                Log.v("nothing", "nothing");
            }








        ListAdapter WorksheetAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, WorksheetName);
        ListView WorksheetList = (ListView) findViewById(R.id.WorksheetList);
        WorksheetList.setAdapter(WorksheetAdapter);


        WorksheetList.setOnItemClickListener
                (
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Intent i = new Intent(getApplicationContext(), StudentWorksheetsResponse.class);
                                Log.v("sending:", WorksheetID[position]+"");
                                i.putExtra("WorksheetID", WorksheetID[position]);
                                startActivity(i);

                            }
                        }

                );



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.home:
                Intent homeintent = new Intent(this, MainActivity.class);
                Log.v("go home", "home intent pressed");
                startActivity(homeintent);
                return true;


            case R.id.add:
                Intent i = new Intent(getApplicationContext(), AddWorksheet.class);
                Log.v("go add","add intent pressed");

                i.putExtra("TopicID", TopicID);
                startActivity(i);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }
        private class getWorksheet extends AsyncTask<Integer, Void, JSONObject>
    {
        private ProgressDialog pdialog;

        @Override
        protected void onPreExecute()
        {
            Log.d("", "we are executing");
            pdialog = new ProgressDialog(Worksheet.this);
            pdialog.setCancelable(false);
            pdialog.setMessage("Getting Topics...");
            showdialog();



        }


        @Override
        protected JSONObject doInBackground(Integer... params)

        {
            int TopicID= params[0];
            Log.v("passed in values", ""+TopicID);

            String result = null;
            JSONObject jsonObject = null;

            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/worksheetrequest.php");




                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair( "TopicID", TopicID + ""));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();

                Log.d("","got a response");

                result = EntityUtils.toString(entity);
                Log.v("response",result);
                jsonObject = new JSONObject(result);




            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject)
        {

            hidedialog();
        }



        protected void showdialog()
        {
            if(pdialog.isShowing())
            {
                pdialog.show();

            }

        }

        private void hidedialog()
        {
            if(pdialog.isShowing())
            {
                pdialog.dismiss();

            }

        }

    }




    private class deletesubject extends AsyncTask<Integer, Void, JSONObject>
    {
        //ResultSet Book;

        private ProgressDialog pdialog;

        @Override
        protected void onPreExecute()
        {
            Log.d("", "we are executing");
            pdialog = new ProgressDialog(Worksheet.this);
            pdialog.setCancelable(false);
            pdialog.setMessage("Getting Subjects...");
            showdialog();



        }


        @Override
        protected JSONObject doInBackground(Integer... params)

        {
            int item= params[0];




            String result = null;
            JSONObject jsonObject = null;

            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/deleteWorksheet.php");


                Log.v("deleting...",item+"");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair( "WorksheetID", item+"" ));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();

                Log.d("","got a response");

                result = EntityUtils.toString(entity);
                Log.v("",result);
                jsonObject = new JSONObject(result);



            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject)
        {

            hidedialog();
        }



        protected void showdialog()
        {
            if(pdialog.isShowing())
            {
                pdialog.show();

            }

        }

        private void hidedialog()
        {
            if(pdialog.isShowing())
            {
                pdialog.dismiss();

            }

        }

    }








}
