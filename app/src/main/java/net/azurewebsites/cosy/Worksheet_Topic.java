package net.azurewebsites.cosy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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

public class Worksheet_Topic extends ActionBarActivity {
    UserLocalStore userLocalStore;
    int SubjectID;
    int[] TopicID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worksheet__topic);
        userLocalStore = new UserLocalStore(this);
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;

        Bundle subjectdata = getIntent().getExtras();
        SubjectID = subjectdata.getInt("SubjectID");
        Log.v("got a seat", ""+SubjectID);

        try {

            jsonObject = new getTopic().execute(SubjectID).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        String[] Topic = {""};
        String nothing ="Nothing To Display";

try {

    try {
        assert jsonObject != null;
        jsonArray = jsonObject.getJSONArray("TopicName");
    } catch (JSONException e) {
        e.printStackTrace();
        Log.v("nothing", "nothing");
        Topic[0] = nothing;
    }
    assert jsonArray != null;
    Topic = new String[jsonArray.length()];
    for (int i = 0; i < jsonArray.length(); i++) {
        try {
            Topic[i] = jsonArray.getString(i);
        } catch (JSONException e) {
            e.printStackTrace();
            Topic[0] = nothing;
            Log.v("nothing", "nothing");

        }
    }
}
catch (Exception e)
{
    Topic[0] = nothing;
    Log.v("nothing", "nothing");
}

        try {

            try {
                jsonArray = jsonObject.getJSONArray("TopicID");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v("nothing", "nothing");
                Topic[0] = nothing;
            }
            TopicID = new int[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    TopicID[i] = jsonArray.getInt(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Topic[0] = nothing;
                    Log.v("nothing", "nothing");

                }
            }
        }
        catch (Exception e)
        {
            Topic[0] = nothing;
            Log.v("nothing", "nothing");
        }






        ListAdapter TopicAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Topic);
        ListView TopicList = (ListView) findViewById(R.id.TopicList);
        TopicList.setAdapter(TopicAdapter);

        TopicList.setOnItemClickListener
                (
                        new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                String Topic = String.valueOf(parent.getItemAtPosition(position));
                                Intent i = new Intent(getApplicationContext(), Worksheet.class);
                                i.putExtra("TopicID", TopicID[position]);
                                startActivity(i);

                                // Toast.makeText(Books.this, books, Toast.LENGTH_LONG).show();
                            }
                        }

                );






    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_worksheet__topic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent i = new Intent(getApplicationContext(), AddTopic.class);
        i.putExtra("SubjectID", SubjectID);
        startActivity(i);

        return true;

    }

    private class getTopic extends AsyncTask<Integer, Void, JSONObject>
    {
        private ProgressDialog pdialog;

        @Override
        protected void onPreExecute()
        {
            Log.d("", "we are executing");
            pdialog = new ProgressDialog(Worksheet_Topic.this);
            pdialog.setCancelable(false);
            pdialog.setMessage("Getting Topics...");
            showdialog();



        }


        @Override
        protected JSONObject doInBackground(Integer... params)

        {
            int SubjectID= params[0];
            Log.v("passed in values", ""+SubjectID);

            String result = null;
            JSONObject jsonObject = null;

            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/topicrequest.php");

                User user = userLocalStore.getLoggedInUser();


                String username = user.username;
                Log.v("user logged in is:", username);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair( "SubjectID", SubjectID + ""));

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
