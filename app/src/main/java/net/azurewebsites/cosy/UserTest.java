package net.azurewebsites.cosy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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


public class UserTest extends ActionBarActivity
{
    TextView TVQUESTION,TVANSWER, TVSTUDENTANSWER, TVVerdict;
    int Question_Num = 1;
    Button NEXT, Submit;
    int Amount, WorksheetID;
    JSONArray jsonArray;
    JSONObject jsonObject;
    String[] Question, Answer,StudentAnswer;
    UserLocalStore userLocalStore;
    int score=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_test);

        userLocalStore = new UserLocalStore(this);


        TVQUESTION = (TextView) findViewById(R.id.TVQUESTION);
        TVANSWER = (TextView) findViewById(R.id.TVANSWER);
        TVSTUDENTANSWER = (TextView) findViewById(R.id.TVSTUDENTANSWER);
        TVVerdict = (TextView) findViewById(R.id.TVVERDICT);
        NEXT = (Button) findViewById(R.id.NEXT);
        Submit = (Button) findViewById(R.id.QSUBMIT);

        Bundle subjectdata = getIntent().getExtras();
        Amount = subjectdata.getInt("Amount");
        WorksheetID = subjectdata.getInt("WorksheetID");

        updatedisplay();

        NEXT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Question_Num++;
                updatedisplay();

            }


        });

        Submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), StudentWorksheetsResponse.class);
                i.putExtra("WorksheetID", WorksheetID);
                startActivity(i);

            }


        });






    }

    private void updatedisplay()
    {
        if(Question_Num<=Amount) {


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
            } catch (Exception e) {
                Question[0] = "Error Question not loading";
                if (Question_Num > Amount) {
                    Question[0] = "Complete";

                }

            }

            try {
                try {
                    assert jsonObject != null;
                    jsonArray = jsonObject.getJSONArray("Answer");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Answer[0] = "Error Question not Loading";
                }
                assert jsonArray != null;
                Answer = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        Answer[i] = jsonArray.getString(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Answer[0] = "Error Question not loading";

                    }
                }
            } catch (Exception e) {
                Answer[0] = "Error Question not loading";
                if (Question_Num > Amount) {
                    Answer[0] = "Complete";

                }

            }

            try {
                try {
                    assert jsonObject != null;
                    jsonArray = jsonObject.getJSONArray("Student_Answer");
                } catch (JSONException e) {
                    e.printStackTrace();
                    StudentAnswer[0] = "Error Question not Loading";
                }
                assert jsonArray != null;
                StudentAnswer = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        StudentAnswer[i] = jsonArray.getString(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        StudentAnswer[0] = "Error Question not loading";

                    }
                }
            } catch (Exception e)
            {
                Log.v("Error","Something went wrong");
            }

            if (Answer[0].equals(StudentAnswer[0]))
            {
                TVVerdict.setText("Correct");
                TVVerdict.setTextColor(Color.GREEN);
                score++;

            }
            else
            {
                TVVerdict.setText("incorrect");
                TVVerdict.setTextColor(Color.RED);

            }


            TVQUESTION.setText(Question_Num + "."+  Question[0]);
            TVANSWER.setText( "ANSWER = " + Answer[0]);
            TVSTUDENTANSWER.setText("Student Answer = "+ StudentAnswer[0]);
        }
        else
        {
            Log.v("in the else","setting variables");
            TVVerdict.setText("Final Score = " + score + "/" + Amount);
            TVQUESTION.setVisibility(View.INVISIBLE);
            TVSTUDENTANSWER.setVisibility(View.INVISIBLE);
            TVANSWER.setVisibility(View.INVISIBLE);

            Submit.setVisibility(View.VISIBLE);


        }






    }


    private class getQuestion extends AsyncTask<Void, Void, JSONObject>
    {
        //ResultSet Book;

        private ProgressDialog pdialog;

        @Override
        protected void onPreExecute()
        {
            Log.d("", "we are executing");
            pdialog = new ProgressDialog(UserTest.this);
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
                HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/answers.php");

                User user = userLocalStore.getLoggedInUser();


                String username = user.username;
                Log.v("user logged in is:",username);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair( "Username", username ));
                nameValuePairs.add(new BasicNameValuePair( "WorksheetID", WorksheetID+"" ));
                nameValuePairs.add(new BasicNameValuePair( "Question_Num", Question_Num+"" ));


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
