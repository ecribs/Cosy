package net.azurewebsites.cosy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AddWorksheet extends ActionBarActivity
{
    Button SUBMIT;
    EditText ETWorksheetName, ETNum_Q, ETDate;
    Spinner SQ_Type;
    UserLocalStore userLocalStore;


    private static class Stuff
    {
        String WorksheetName, W_Type;
        int Num_Q, TopicID;
        Date W_Date;


        Stuff(String WorksheetName,String W_Type, int Num_Q, int TopicID, Date W_Date )
        {
            this.WorksheetName = WorksheetName;
            this.W_Type = W_Type;
            this.Num_Q = Num_Q;
            this.TopicID = TopicID;
            this.W_Date = W_Date;


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_worksheet);


        ETWorksheetName = (EditText) findViewById(R.id.WorksheetName);
        ETNum_Q = (EditText) findViewById(R.id.Num_Q);
        ETDate = (EditText) findViewById(R.id.W_Date);
        SQ_Type = (Spinner) findViewById( R.id.Q_Type);
        SUBMIT = (Button) findViewById(R.id.INSERT);

        Bundle subjectdata = getIntent().getExtras();
        final int TopicID = subjectdata.getInt("TopicID");
        Log.v("got a seat", ""+TopicID);
        Log.v("got this far", "");

        SUBMIT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                String WorkshetName = ETWorksheetName.getText().toString();
                int Num_Q = Integer.parseInt(ETNum_Q.getText().toString());
                Date W_Date = Date.valueOf(ETDate.getText().toString());
                String W_Type = SQ_Type.getSelectedItem().toString();




                Stuff params = new Stuff(WorkshetName, W_Type, TopicID, Num_Q, W_Date);
                InsertWorksheet InsertWorksheet = new InsertWorksheet();
                try {
                    InsertWorksheet.execute(params).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }





                gotoNewWorksheet(params);


            }

        });



    }


    protected void gotoNewWorksheet(Stuff items)
    {
        Intent intent = new Intent(this, Worksheet.class);
        Bundle extras = new Bundle();
        extras.putInt("TopicID",items.TopicID);
        //extras.putString("EXTRA_PASSWORD","my_password");
        intent.putExtras(extras);
        startActivity(intent);


    }


    private class InsertWorksheet extends AsyncTask<Stuff, Void, Void>
    {
        //ResultSet Book;

        private ProgressDialog pdialog;

        @Override
        protected void onPreExecute()
        {
            Log.d("", "we are executing");
            pdialog = new ProgressDialog(AddWorksheet.this);
            pdialog.setCancelable(false);
            pdialog.setMessage("Inserting Subject...");
            showdialog();



        }


        @Override
        protected Void doInBackground(Stuff... params)

        {
            String result;
            JSONObject jsonObject;


            String WorksheetName = params[0].WorksheetName;
            String W_Type = params[0].W_Type;
            int TopicID = params[0].TopicID;
            int Num_Q = params[0].Num_Q;
            Date W_Date = params[0].W_Date;
            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/insertworksheet.php");



                Log.v("values being posted", WorksheetName + W_Type+ TopicID + Num_Q+ W_Date);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair( "WorksheetName", WorksheetName));
                nameValuePairs.add(new BasicNameValuePair("W_Type", W_Type));
                nameValuePairs.add(new BasicNameValuePair("TopicID", TopicID+""));
                nameValuePairs.add(new BasicNameValuePair("Num_Q", Num_Q+""));
                nameValuePairs.add(new BasicNameValuePair("W_Date", W_Date+""));



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