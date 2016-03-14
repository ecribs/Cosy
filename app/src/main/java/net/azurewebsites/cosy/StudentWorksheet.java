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
import android.widget.TextView;

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

public class StudentWorksheet extends ActionBarActivity {
    TextView TVquestion;
    EditText ETAnswer;
    Button Submit, Next;
    int WorksheetID;
    UserLocalStore userLocalStore;
    int QuestionNum = 1;
    JSONObject jsonObject;
    String[] Question;
    JSONArray jsonArray;
    int amount;
    String Answer;
    int SubjectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_worksheet);
        userLocalStore = new UserLocalStore(this);


        TVquestion = (TextView) findViewById(R.id.TVQuestion);
        ETAnswer = (EditText) findViewById(R.id.ETAnswer);
        Submit = (Button) findViewById(R.id.QSUBMIT);
        Next = (Button) findViewById(R.id.NEXT);
        Bundle subjectdata = getIntent().getExtras();
        WorksheetID = subjectdata.getInt("WorksheetID");
        amount = subjectdata.getInt("Num_Q");
        SubjectID = subjectdata.getInt("SubjectID");


        updatedisplay();


        Next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Answer = ETAnswer.getText().toString();
                InsertAnswer InsertAnswer = new InsertAnswer();
                InsertAnswer.execute();

                ETAnswer.setText(" ");

                QuestionNum++;

                updatedisplay();
                if (QuestionNum > amount) {
                    Next.setVisibility(View.INVISIBLE);
                    Submit.setVisibility(View.VISIBLE);
                }


            }


        });

        Submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                Intent i = new Intent(getApplicationContext(), Classroom.class);
                i.putExtra("SubjectID", SubjectID);
                startActivity(i);


            }


        });




    }


    private void updatedisplay()
    {

        try {

            jsonObject = new getQuestion().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        try {
            try {
                assert jsonObject != null;
                jsonArray = jsonObject.getJSONArray("Question");
            } catch (JSONException e) {
                e.printStackTrace();
                Question[0] = "Error Question not Loading";
            }
            assert jsonArray != null;
            Question = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    Question[i] = jsonArray.getString(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Question[0] = "Error Question not loading";

                }
            }
        }catch (Exception e)
        {
            Question[0] = "Error Question not loading";
            if (QuestionNum>amount)
            {
                Question[0] = "Complete";

            }

        }

        TVquestion.setText(QuestionNum + "."+  Question[0]);



    }









    private class getQuestion extends AsyncTask<Void, Void, JSONObject>
    {
        //ResultSet Book;

        private ProgressDialog pdialog;

        @Override
        protected void onPreExecute()
        {
            Log.d("", "we are executing");
            pdialog = new ProgressDialog(StudentWorksheet.this);
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
                HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/studentquestions.php");

                User user = userLocalStore.getLoggedInUser();


                String username = user.username;
                Log.v("sending", "Worksheet:"+ WorksheetID+"Question:" + QuestionNum+"");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("WorksheetID", WorksheetID+""));
                nameValuePairs.add(new BasicNameValuePair("QuestionNum", QuestionNum+""));


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





    private class InsertAnswer extends AsyncTask<Void, Void, Void>
    {
        //ResultSet Book;

        private ProgressDialog pdialog;

        @Override
        protected void onPreExecute()
        {
            Log.d("", "we are executing");
            pdialog = new ProgressDialog(StudentWorksheet.this);
            pdialog.setCancelable(false);
            pdialog.setMessage("Inserting Subject...");
            showdialog();





        }


        @Override
        protected Void doInBackground(Void... params)

        {
            String result;
            JSONObject jsonObject;

            User user = userLocalStore.getLoggedInUser();

            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/insertanswer.php");



                Log.v("values being posted:", WorksheetID + " " + QuestionNum + " "+  user.username + " " + Answer);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair( "WorksheetID", WorksheetID+""));
                nameValuePairs.add(new BasicNameValuePair("Question_Num", QuestionNum+""));
                nameValuePairs.add(new BasicNameValuePair("Username", user.username));
                nameValuePairs.add(new BasicNameValuePair("Answer", Answer));


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
