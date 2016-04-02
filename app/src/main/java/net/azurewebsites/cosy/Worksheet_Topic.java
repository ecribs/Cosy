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

public class Worksheet_Topic extends ActionBarActivity {
    UserLocalStore userLocalStore;
    int SubjectID;
    int[] TopicID;
    String[] Topic = {""};
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



        registerForContextMenu(TopicList);


    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {

        if (v.getId()==R.id.TopicList)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(Topic[info.position]);


            menu.add(0, v.getId(), 0, "Edit");
            menu.add(0, v.getId(), 0, "Delete");
            menu.add(0, v.getId(), 0, "Cancel");
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int listitem=  TopicID[info.position];
        int num = info.position;

        if (item.getTitle() == "Delete") {
            try
            {

                JSONObject jsonObject = new deletesubject().execute(listitem).get();



            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e)
            {
                e.printStackTrace();
            }

            Intent i = new Intent(getApplicationContext(), Worksheet_Topic.class);
            i.putExtra("SubjectID", SubjectID);
            startActivity(i);

        }
        else if (item.getTitle() == "Edit")
        {
            Log.v("Button clicked:","Edit");
        Intent intent = new Intent(this, EditTopic.class);
        Bundle extras = new Bundle();
        extras.putInt("TopicID", listitem);
        extras.putInt("Topic", TopicID[num]);
            extras.putInt("SubjectID",SubjectID);
            Log.v("Passing through",listitem + TopicID[num] + "");


        intent.putExtras(extras);
        startActivity(intent);

        }
        else if (item.getTitle() == "Cancel") {
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
        } else {
            return false;
        }
        return true;
    }


    private class deletesubject extends AsyncTask<Integer, Void, JSONObject>
    {
        //ResultSet Book;

        private ProgressDialog pdialog;

        @Override
        protected void onPreExecute()
        {
            Log.d("", "we are executing");
            pdialog = new ProgressDialog(Worksheet_Topic.this);
            pdialog.setCancelable(false);
            pdialog.setMessage("Getting Subjects...");
            showdialog();



        }


        @Override
        protected JSONObject doInBackground(Integer... params)

        {
            int item= params[0];
            User user = userLocalStore.getLoggedInUser();
            String username = user.username;


            Log.v("Deleting...",item+ "");
            String result = null;
            JSONObject jsonObject = null;

            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/deletetopic.php");



                Log.v("user logged in is:", username);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair( "TopicID", item+"" ));

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                Intent i = new Intent(getApplicationContext(), AddTopic.class);
                i.putExtra("SubjectID", SubjectID);
                startActivity(i);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
