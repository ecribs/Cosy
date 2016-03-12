package net.azurewebsites.cosy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

public class worksheet_Subjects extends ActionBarActivity
    {

        UserLocalStore userLocalStore;
        String[] SubjectName= {};
        int[] SubjectID={};
        JSONObject jsonObject = null;




        @Override
            protected void onCreate(Bundle savedInstanceState)
        {

            userLocalStore = new UserLocalStore(this);
            JSONArray jsonArray = null;
            String nothing = "Nothing to display";


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_worksheet__subjects);


            try {

                jsonObject = new getSubjects().execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }



            Log.v("we're getting a subject", "we're getting a subject");

            try {
                assert jsonObject != null;
                jsonArray = jsonObject.getJSONArray("SubjectName");
            } catch (JSONException e) {
                e.printStackTrace();
                SubjectName[0]= nothing;
            }
            assert jsonArray != null;
            SubjectName = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++)
            {
                try {
                    SubjectName[i] = jsonArray.getString(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                    SubjectName[0]= nothing;

                }
            }

            Log.v("getting a subjectID", "SubjectID TIME");
            try {

                try {
                    jsonArray = jsonObject.getJSONArray("SubjectID");
                    Log.v("Worked", "SubjectID is in ");

                } catch (JSONException e) {
                    Log.v("failed", "No SubjectID");
                    e.printStackTrace();
                    SubjectName[0]= nothing;

                }
                SubjectID = new int[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    Log.v("failed", "cant do loop");

                    try {
                        SubjectID[i] = jsonArray.getInt(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.v("failed", "cant do loop 2");
                        SubjectName[0]= nothing;


                    }
                }
            }
            catch (Exception e)
            {
                SubjectName[0]= nothing;

            }




            Log.v("here is the subject:", SubjectName[1]);
            Log.v("here is the subjectID:", ""+SubjectID[1]);

            ListAdapter Subjectadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, SubjectName);
            ListView SubjectList = (ListView) findViewById(R.id.SubjectList);
            SubjectList.setAdapter(Subjectadapter);
            registerForContextMenu(SubjectList);


            SubjectList.setOnItemClickListener
                    (
                            new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String subject = String.valueOf(parent.getItemAtPosition(position));
                                    Intent i = new Intent(getApplicationContext(), Worksheet_Topic.class);
                                    i.putExtra("SubjectID", SubjectID[position]);
                                    startActivity(i);

                                    // Toast.makeText(Books.this, books, Toast.LENGTH_LONG).show();
                                }
                            }

                    );}



        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_worksheet__subjects, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent AddSubjectIntent = new Intent(this, AddSubject.class);
        startActivity(AddSubjectIntent);

    return true;

    }


        private class getSubjects extends AsyncTask<Void, Void, JSONObject>
        {
            //ResultSet Book;

            private ProgressDialog pdialog;

            @Override
            protected void onPreExecute()
            {
                Log.d("", "we are executing");
                pdialog = new ProgressDialog(worksheet_Subjects.this);
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
                    HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/getsubjects.php");

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
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo)
        {

            if (v.getId()==R.id.SubjectList)
            {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                menu.setHeaderTitle(SubjectName[info.position]);


                menu.add(0, v.getId(), 0, "Edit");
                menu.add(0, v.getId(), 0, "Delete");
                menu.add(0, v.getId(), 0, "Cancel");
            }
        }
        @Override
        public boolean onContextItemSelected(MenuItem item)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            String listitem=  SubjectName[info.position];

            if (item.getTitle() == "Delete") {
                try
                {

                    jsonObject = new deletesubject().execute(listitem).get();



                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e)
                {
                    e.printStackTrace();
                }

                Intent i = new Intent(getApplicationContext(), worksheet_Subjects.class);
                startActivity(i);
            }
            else if (item.getTitle() == "Edit")
            {
                Intent i = new Intent(getApplicationContext(), EditSubject.class);
                i.putExtra("SubjectName", listitem);
                startActivity(i);
            }
            else if (item.getTitle() == "Action 3") {
                Toast.makeText(this, "Action 3 invoked", Toast.LENGTH_SHORT).show();
            } else {
                return false;
            }
            return true;
        }


        private class deletesubject extends AsyncTask<String, Void, JSONObject>
        {
            //ResultSet Book;

            private ProgressDialog pdialog;

            @Override
            protected void onPreExecute()
            {
                Log.d("", "we are executing");
                pdialog = new ProgressDialog(worksheet_Subjects.this);
                pdialog.setCancelable(false);
                pdialog.setMessage("Getting Subjects...");
                showdialog();



            }


            @Override
            protected JSONObject doInBackground(String... params)

            {
                String item= params[0];
                User user = userLocalStore.getLoggedInUser();
                String username = user.username;



                String result = null;
                JSONObject jsonObject = null;

                try
                {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/deletesubject.php");



                    Log.v("user logged in is:",username);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair( "SubjectName", item ));
                    nameValuePairs.add(new BasicNameValuePair("Username", username ));

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
