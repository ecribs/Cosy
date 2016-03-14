package net.azurewebsites.cosy;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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


public class TakeAttendence extends ActionBarActivity {
    ListView lv;
    String List[] ={"Android","Windows","IOS"};
    Button btn;
    UserLocalStore userLocalStore;
    String[] Users;
    JSONObject jsonObject;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendence);
        lv= (ListView) findViewById(R.id.LVCLASS);
        btn = (Button) findViewById(R.id.button);
        userLocalStore = new UserLocalStore(this);

        try {

            jsonObject = new getclass().execute().get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }


        Log.v("we're getting a User", "we're getting a User");
        try {
            try {
                assert jsonObject != null;
                jsonArray = jsonObject.getJSONArray("Username");
            } catch (JSONException e) {
                e.printStackTrace();
                Users[0] = "nothing";
            }
            assert jsonArray != null;
            Users = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    Users[i] = jsonArray.getString(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Users[0] = "nothing";

                }
            }
        }catch (Exception e)
        {
            Log.v("nothing works","nothing works");

        }


        ArrayAdapter<String> ard = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,Users);
        lv.setAdapter(ard);

        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                SparseBooleanArray sba= lv.getCheckedItemPositions();

                for(int i=0; i<sba.size(); i++)
                {
                    if(sba.valueAt(i))
                    {
                        Toast.makeText(getApplicationContext(),Users[sba.keyAt(i)], Toast.LENGTH_LONG).show();

                    }


                }


            }
        });





    }


    private class getclass extends AsyncTask<Void, Void, JSONObject>
    {
        //ResultSet Book;

        private ProgressDialog pdialog;

        @Override
        protected void onPreExecute()
        {
            Log.d("", "we are executing");
            pdialog = new ProgressDialog(TakeAttendence.this);
            pdialog.setCancelable(false);
            pdialog.setMessage("Getting Subjects...");
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
                HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/attendencelist.php");

                User user = userLocalStore.getLoggedInUser();


                String username = user.username;
                Log.v("user logged in is:",user.ClassID);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair( "ClassID", user.ClassID ));
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
