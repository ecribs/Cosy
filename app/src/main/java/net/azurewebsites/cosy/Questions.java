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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Questions extends ActionBarActivity {
    TextView TVQuestion;
    EditText ETQuestion, ETAnswer;
    Button Next, Previous, SUBMIT;
    String[] questionarray;
    String[] answerarray;
    int num;

    private static class var
    {
        String Question, Answer;
        int QuestionNum, WorksheetID;


        var(int QuestionNum, String Question, int WorksheetID, String Answer)
        {
            this.Question = Question;
            this.Answer = Answer;
            this.WorksheetID = WorksheetID;
            this.QuestionNum = QuestionNum;



        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Log.v("It is working","in QQuestions class");
        TVQuestion = (TextView) findViewById(R.id.TVQuestion);
        ETQuestion = (EditText) findViewById(R.id.ETQuestion);
        ETAnswer = (EditText) findViewById(R.id.ETAnswer);
        Next = (Button) findViewById(R.id.NEXT);
        SUBMIT = (Button) findViewById(R.id.QSUBMIT);

        final Bundle extras = getIntent().getExtras();
        final int WorksheetID = extras.getInt("WorksheetID");
        final int amount = extras.getInt("Num_Q");
        final int TopicID = extras.getInt("TopicID");
        answerarray = extras.getStringArray("arrayanswer");
        questionarray = extras.getStringArray("questionarray");
        num = 1;

        if(num<amount)
        {
            TVQuestion.setText("Question: " + num);
        }







        if(num>1)
        {
            Previous.setVisibility(View.VISIBLE);

        }


/*
        if(questionarray[num]!= null)
        {
            ETQuestion.setText(questionarray[num]);
            ETAnswer.setText(answerarray[num]);
        }
        */
        Log.v("Arrays", "Arrays created");


            Next.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {


                    String question = ETQuestion.getText().toString();
                    String answer = ETAnswer.getText().toString();


                    var params = new var(num, question, WorksheetID, answer);

                    InsertQuestion InsertQuestion = new InsertQuestion();
                    InsertQuestion.execute(params);
                    num++;
                    Log.v("num:", num + "");
                    ETQuestion.setText(" ");
                    ETAnswer.setText(" ");

                    if (num>amount)
                    {
                        Next.setVisibility(View.INVISIBLE);
                        SUBMIT.setVisibility(View.VISIBLE);
                        TVQuestion.setText("COMPLETE");
                    }
                    if(num<amount)
                    TVQuestion.setText("Question: "+num);


                }



            });




            SUBMIT.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent i = new Intent(getApplicationContext(), Worksheet.class);
                    i.putExtra("TopicID", TopicID);
                    startActivity(i);


                }

            });










    }


    private class InsertQuestion extends AsyncTask<var, Void, Void>
    {
        //ResultSet Book;

        private ProgressDialog pdialog;

        @Override
        protected void onPreExecute()
        {
            Log.d("", "we are executing");
            pdialog = new ProgressDialog(Questions.this);
            pdialog.setCancelable(false);
            pdialog.setMessage("Inserting Subject...");
            showdialog();





        }


        @Override
        protected Void doInBackground(var... params)

        {
            String result;
            JSONObject jsonObject;

            int WorksheetID = params[0].WorksheetID;
            int QuestionNum = params[0].QuestionNum;
            String Question = params[0].Question;
            String Answer = params[0].Answer;


            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/insertquestion.php");



                Log.v("values being posted:", WorksheetID + " " + QuestionNum + " "+  Question + " " + Answer);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair( "WorksheetID", WorksheetID+""));
                nameValuePairs.add(new BasicNameValuePair("QuestionNum", QuestionNum+""));
                nameValuePairs.add(new BasicNameValuePair("Question", Question));
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
