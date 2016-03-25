package net.azurewebsites.cosy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

public class Classroom extends ActionBarActivity {

    String[] BookName = {};
    String[] BookSubject = {};
    int[] WorksheetID = {};
    UserLocalStore userLocalStore;
    int SubjectID;
    String[] WorksheetName;
    int[] Num_Q={};
    String nothing =  "nothing to display";




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);

        userLocalStore = new UserLocalStore(this);
        JSONObject jsonObject = null;
        Bundle subjectdata = getIntent().getExtras();
        SubjectID = subjectdata.getInt("SubjectID");
        Log.v("got a seat", ""+SubjectID);

        try {

            jsonObject = new getBooks().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        Log.v("converting:", "getting books");
        JSONArray jsonArray = null;
        try {


            try {
                jsonArray = jsonObject.getJSONArray("BookName");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BookName = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    BookName[i] = jsonArray.getString(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e)
        {
            BookName[0]= "nothing to display";

        }

        Log.v("here is our book name:", BookName[0]);

        // for getting subjectnames
        Log.v("we're getting a subject", "we're getting a subject");

        try {
            jsonArray = jsonObject.getJSONArray("SubjectName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BookSubject = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                BookSubject[i] = jsonArray.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.v("Subject",BookSubject[0]);

        // for getting Worksheet
        Log.v("getting WorksheetID", "we're getting a WorksheetID");

    try {
        try {
            jsonArray = jsonObject.getJSONArray("WorksheetID");
            Log.v("Worked", "WorksheetID is in ");

        } catch (JSONException e) {
            Log.v("failed", "No WorksheetID");
            e.printStackTrace();


        }
        WorksheetID = new int[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                WorksheetID[i] = jsonArray.getInt(i);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v("failed", "cant do loop 2");
            }
        }
    }catch (Exception e)
    {

    }

        // for getting WorksheetName
        Log.v("we're getting Worksheet", "we're getting Worksheet");
try {
    try {
        jsonArray = jsonObject.getJSONArray("WorksheetName");
    } catch (JSONException e)
    {
        e.printStackTrace();

    }
    WorksheetName = new String[jsonArray.length()];
    for (int i = 0; i < jsonArray.length(); i++) {
        try {
            WorksheetName[i] = jsonArray.getString(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}catch (Exception e)
{
    WorksheetName[0] = nothing;

}

try {
    try {
        jsonArray = jsonObject.getJSONArray("Num_Q");
        Log.v("Num_Q", "Num_Q is in ");

    } catch (JSONException e) {
        Log.v("failed", "No WorksheetID");
        e.printStackTrace();
        Num_Q[0] = 0;

    }
    Num_Q = new int[jsonArray.length()];
    for (int i = 0; i < jsonArray.length(); i++) {

        try {
            Num_Q[i] = jsonArray.getInt(i);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("failed", "cant do loop 2");
        }
    }
}catch(Exception e)
{

}


        Log.v("here is the stuff:", WorksheetID[0] + "" + WorksheetName[0]);






        ListAdapter BookAdapter = new BookAdapter(this, BookName, BookSubject);
        ListView BookList = (ListView) findViewById(R.id.Lbooks);
        BookList.setAdapter(BookAdapter);

        Log.v("Book adapter works","Must be the next person");

        ListAdapter Worksheet = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, WorksheetName);
        ListView WorksheetList = (ListView) findViewById(R.id.LWork);
        WorksheetList.setAdapter(Worksheet);

        BookList.setOnItemClickListener
                (
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String url = "http://cosy.azurewebsites.net/Books/" + BookName[position] + ".pdf";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                                //String books = String.valueOf(parent.getItemAtPosition(position));
                                // Toast.makeText(Books.this, books, Toast.LENGTH_LONG).show();
                            }
                        }

                );

        WorksheetList.setOnItemClickListener
                (
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {

                                Intent i = new Intent(getApplicationContext(), StudentWorksheet.class);
                                i.putExtra("WorksheetID", WorksheetID[position]);
                                i.putExtra("Num_Q", Num_Q[position]);
                                i.putExtra("SubjectID",SubjectID);
                                startActivity(i);
                                //String books = String.valueOf(parent.getItemAtPosition(position));
                                // Toast.makeText(Books.this, books, Toast.LENGTH_LONG).show();
                            }
                        }

                );

    }


    private class getBooks extends AsyncTask<Void, Void, JSONObject> {
        //ResultSet Book;

        private ProgressDialog pdialog;

        @Override
        protected void onPreExecute() {
            Log.d("", "we are executing");
            pdialog = new ProgressDialog(Classroom.this);
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
                HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/classbooks.php");





                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("SubjectID", SubjectID+""));
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
}
