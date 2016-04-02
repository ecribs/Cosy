package net.azurewebsites.cosy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.concurrent.ExecutionException;

import javax.security.auth.Subject;

public class EditTopic extends ActionBarActivity {

    EditText ETTOPIC;
    Button Submit;
    String Topic;
    int TopicID, SubjectID;

    private static class var
    {
        int TopicID;
        String Topics, Topic1;


        var(int TopicID, String Topics, String Topic1)
        {
            this.TopicID = TopicID;
            this.Topics = Topics;
            this.Topic1 = Topic1;


        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        Bundle Topicstuff = getIntent().getExtras();
        TopicID = Topicstuff.getInt("TopicID");
        Topic = Topicstuff.getString("Topic");
        SubjectID = Topicstuff.getInt("SubjectID");

        ETTOPIC = (EditText) findViewById(R.id.ETTOPIC);
        Submit = (Button) findViewById(R.id.SUBMIT);
        ETTOPIC.setText(Topic);

        Submit.setText("Update");



        Submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String text = String.valueOf(ETTOPIC.getText());

                var params = new var(TopicID, Topic,text);

                edittopic edittopic = new edittopic();
                edittopic.execute(params);

                Intent i = new Intent(getApplicationContext(), Worksheet_Topic.class);
                i.putExtra("SubjectID", SubjectID);
                startActivity(i);



            }

        });


    }

    private class edittopic extends AsyncTask<var, Void, Void>
    {
        //ResultSet Book;

        private ProgressDialog pdialog;

        @Override
        protected void onPreExecute()
        {
            Log.d("", "we are executing");
            pdialog = new ProgressDialog(EditTopic.this);
            pdialog.setCancelable(false);
            pdialog.setMessage("Inserting Subject...");
            showdialog();



        }


        @Override
        protected Void doInBackground(var... params)

        {
            String result;
            JSONObject jsonObject;

            int TopicID1 = params[0].TopicID;
            String Topics = params[0].Topics;
            String Topics1 = params[0].Topic1;
            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/edittopic.php");




                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair( "TopicID", TopicID1+"" ));
                nameValuePairs.add(new BasicNameValuePair( "Topic", Topics+"" ));
                nameValuePairs.add(new BasicNameValuePair( "Topic1", Topics1+"" ));


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
        switch (item.getItemId()) {
            case R.id.home:
                Intent homeintent = new Intent(this, MainActivity.class);
                startActivity(homeintent);
            default:
                return super.onOptionsItemSelected(item);


        }
    }
}
