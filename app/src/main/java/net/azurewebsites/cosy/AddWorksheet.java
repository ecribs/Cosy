package net.azurewebsites.cosy;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class AddWorksheet extends ActionBarActivity
{
    Button SUBMIT;
    EditText ETWorksheetName, ETNum_Q, ETDate;
    Spinner SQ_Type;
    UserLocalStore userLocalStore;
    JSONObject jsonObject = null;
    JSONArray jsonArray = null;
    int[] WorksheetID;

    Calendar myCalendar = Calendar.getInstance();


    private static class Stuff
    {
        String WorksheetName, W_Type;
        int Num_Q, TopicID,WorksheetID;
        String W_Date;


        Stuff(String WorksheetName,String W_Type, int Num_Q, int TopicID, String W_Date, int WorksheetID )
        {
            this.WorksheetName = WorksheetName;
            this.W_Type = W_Type;
            this.Num_Q = Num_Q;
            this.TopicID = TopicID;
            this.W_Date = W_Date;
            this.WorksheetID= WorksheetID;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_worksheet);


        ETWorksheetName = (EditText) findViewById(R.id.WorksheetName);
        ETNum_Q = (EditText) findViewById(R.id.Num_Q);
        ETDate = (EditText) findViewById(R.id.W_Date);
        SQ_Type = (Spinner) findViewById( R.id.S_Ques);
        SUBMIT = (Button) findViewById(R.id.INSERT);

        Bundle subjectdata = getIntent().getExtras();
        final int TopicID = subjectdata.getInt("TopicID");
        Log.v("got a seat", ""+TopicID);
        Log.v("got this far", "");

        SUBMIT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String WorkshetName = ETWorksheetName.getText().toString();
                int Num_Q = Integer.parseInt(ETNum_Q.getText().toString());
                String W_Date =  ETDate.getText().toString();
                String W_Type = SQ_Type.getSelectedItem().toString();


                Stuff params = new Stuff(WorkshetName, W_Type, Num_Q, TopicID, W_Date, 0);


                try {
                    jsonObject = new InsertWorksheet().execute(params).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                try {

                    try {
                        jsonArray = jsonObject.getJSONArray("WorksheetID");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.v("nothing", "nothing");
                    }
                    assert jsonArray != null;
                    WorksheetID = new int[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            WorksheetID[i] = jsonArray.getInt(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("nothing", "nothing");

                        }
                    }
                } catch (Exception e) {
                    Log.v("nothing", "nothing");
                }

                Stuff Questions = new Stuff(WorkshetName, W_Type, Num_Q, TopicID, W_Date, WorksheetID[0]);

                gotoNewWorksheet(Questions);


            }

        });

        ETDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddWorksheet.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    }


    protected void gotoNewWorksheet(Stuff items)
    {
        String[] questionarray = new String[0];
        String[] answerarray = new String[0];

        Intent intent = new Intent(this, Questions.class);
        Bundle extras = new Bundle();
        extras.putInt("TopicID", items.TopicID);
        extras.putInt("WorksheetID", items.WorksheetID);
        extras.putInt("Num_Q", items.Num_Q);


        intent.putExtras(extras);
        startActivity(intent);


    }


    private class InsertWorksheet extends AsyncTask<Stuff, Void, JSONObject>
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
        protected JSONObject doInBackground(Stuff... params)

        {
            String result = null;
            JSONObject jsonObject = null;


            String WorksheetName = params[0].WorksheetName;
            String W_Type = params[0].W_Type;
            int TopicID = params[0].TopicID;
            int Num_Q = params[0].Num_Q;
            String W_Date = params[0].W_Date;
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

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject blah)
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

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
    {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth)
        {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel()
    {

        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        ETDate.setText(sdf.format(myCalendar.getTime()));
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
