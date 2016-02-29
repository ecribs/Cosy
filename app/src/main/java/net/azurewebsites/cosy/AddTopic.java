package net.azurewebsites.cosy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddTopic extends ActionBarActivity {
    Button SUBMIT;
    EditText ETTOPIC;
    UserLocalStore userLocalStore;


    private static class Stuff
    {
        String TopicName;
        int SubjectID;


        Stuff(int SubjectID, String TopicName)
        {
            this.SubjectID = SubjectID;
            this.TopicName = TopicName;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);

        userLocalStore = new UserLocalStore(this);

        ETTOPIC = (EditText) findViewById(R.id.ETTOPIC);
        SUBMIT = (Button) findViewById(R.id.SUBMIT);

        Bundle subjectdata = getIntent().getExtras();
        final int SubjectID = subjectdata.getInt("SubjectID");
        Log.v("got a seat", ""+SubjectID);

        SUBMIT.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String TopicName = ETTOPIC.getText().toString();


                    Stuff params = new Stuff(SubjectID, TopicName);
                    InsertTopic InsertTopic = new InsertTopic();
                    InsertTopic.execute(params);


                gototopic(SubjectID);


            }

        });



    }


    protected void gototopic(int SubjectID)
    {

        Intent i = new Intent(getApplicationContext(), Worksheet_Topic.class);
        i.putExtra("SubjectID", SubjectID);
        startActivity(i);


    }


    private class InsertTopic extends AsyncTask<Stuff, Void, Void>
    {
        //ResultSet Book;

        private ProgressDialog pdialog;

        @Override
        protected void onPreExecute()
        {
            Log.d("", "we are executing");
            pdialog = new ProgressDialog(AddTopic.this);
            pdialog.setCancelable(false);
            pdialog.setMessage("Inserting Subject...");
            showdialog();



        }


        @Override
        protected Void doInBackground(Stuff... params)

        {
            String result;
            JSONObject jsonObject;

            int SubjectID = params[0].SubjectID;
            String TopicName = params[0].TopicName;
            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/insertopic.php");



                Log.v("values being posted", TopicName+" "+ SubjectID);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair( "TopicName", TopicName));
                nameValuePairs.add(new BasicNameValuePair("SubjectID", SubjectID+""));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.v("Posting", "here we go");

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

            return null;
        }

        @Override
        protected void onPostExecute(Void blah)
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
