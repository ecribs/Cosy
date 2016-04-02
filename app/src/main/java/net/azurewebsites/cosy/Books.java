package net.azurewebsites.cosy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Books extends Activity
{

    String[] BookName= {};
    String[] BookSubject= {};
    UserLocalStore userLocalStore;

    //public String[] BookName;
   // public String[] BookSubject;







    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        userLocalStore = new UserLocalStore(this);
        JSONObject jsonObject = null;


        try
        {

             jsonObject = new getBooks().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        Log.v("converting:", "getting books");
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray("Bookname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BookName = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++)
        {
            try {
                BookName[i] = jsonArray.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.v("here is our book name:", BookName[2]);

        // for getting subjectnames
        Log.v("we're getting a subject", "we're getting a subject");

        try {
            jsonArray = jsonObject.getJSONArray("SubjectName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BookSubject = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++)
        {
            try {
                BookSubject[i] = jsonArray.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.v("here is the subject:", BookSubject[1]);



        setContentView(R.layout.activity_books);

        super.onCreate(savedInstanceState);
       // User user = userLocalStore.getLoggedInUser();


       // String[] BookName = {"hello"};
       // String[] BookSubject= {"from the other side"};



        ListAdapter BookAdapter = new BookAdapter(this,BookName, BookSubject);
        ListView BookList = (ListView) findViewById(R.id.booklist);
        BookList.setAdapter(BookAdapter);

        BookList.setOnItemClickListener
                (
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String url = "http://cosy.azurewebsites.net/Books/" + BookName[position]+ ".pdf";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                                //String books = String.valueOf(parent.getItemAtPosition(position));
                                // Toast.makeText(Books.this, books, Toast.LENGTH_LONG).show();
                            }
                        }

                );
    }






    private class getBooks extends AsyncTask<Void, Void, JSONObject>
    {
        //ResultSet Book;

        private ProgressDialog pdialog;

        @Override
        protected void onPreExecute()
        {
            Log.d("","we are executing");
            pdialog = new ProgressDialog(Books.this);
            pdialog.setCancelable(false);
            pdialog.setMessage("Getting Books...");
            showdialog();



        }


        @Override
        protected JSONObject doInBackground(Void... params)

        {
            String result = null;
            JSONObject jsonObject = null;

            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/bookrequest.php");

                User user = userLocalStore.getLoggedInUser();


                String username = user.username;
                Log.v("user logged in is:",username);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair( "username", username ));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();

                Log.d("","got a response");

                result = EntityUtils.toString(entity);
                Log.v("",result);
                jsonObject = new JSONObject(result);
               // JSONObject jsonObject = obj.getJSONObject(0);



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
                Intent i = new Intent(getApplicationContext(), AddBook.class);
                Log.v("go add","add intent pressed");


                startActivity(i);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
