package net.azurewebsites.cosy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.security.auth.Subject;

public class AddBook extends ActionBarActivity {
Button Browse, image, Upload;
    TextView ETFilePath;
    String returnFile;
    ImageView imageToUpload;
    EditText ETBookName;
    Spinner Subject;
    String[] SubjectName= {};
    int[] SubjectID={};
    JSONObject jsonObject = null;
    JSONArray jsonArray = null;
    Uri selectedImage;
    String BookName;
    String imagefile;
    int num;
    String result;

    private static class Data
    {
       String BookName;
        int SubjectID;


        Data(int SubjectID, String BookName)
        {
            this.SubjectID = SubjectID;
            this.BookName = BookName;
        }
    }






    private static final int FILE_SELECT_CODE = 0;
    File input;
    private static final int RESULT_LOAD_IMAGE=1;
    UserLocalStore userLocalStore;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(this);
        User user = userLocalStore.getLoggedInUser();

        setContentView(R.layout.activity_add_book);

        Log.v("ClassID",user.ClassID + user.Role );

        Browse = (Button) findViewById(R.id.Browse);
        image = (Button) findViewById(R.id.imgbrowse);
        ETFilePath = (TextView) findViewById(R.id.ETFILEPATH);
        imageToUpload = (ImageView) findViewById(R.id.ivbook);
        ETBookName = (EditText) findViewById(R.id.ETBOOKNAME);
        Subject = (Spinner) findViewById(R.id.Subject);
        Upload = (Button) findViewById(R.id.Upload);

        String nothing = "nothing to display";

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


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SubjectName); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Subject.setAdapter(spinnerArrayAdapter);








        Browse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                getfile(v);
                //new UploadBook().execute();

            }

        });

        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }

        });

        Upload.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                BookName = ETBookName.getText().toString();
                String BookSubject = Subject.getSelectedItem().toString();
                int seletctedsub =  Subject.getSelectedItemPosition();
                imagefile = result;

                num = SubjectID[seletctedsub];

                Log.v("items being submitted:", BookName + BookSubject + SubjectID[seletctedsub] + image + returnFile );

                insertbook insertbook = new insertbook();
                insertbook.execute();
                try {

                    new UploadBook().execute(returnFile,"file").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                try {

                    new UploadBook().execute(imagefile,"image").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


                Intent i = new Intent(getApplicationContext(), Books.class);
                startActivity(i);


            }

        });





    }


    private class getSubjects extends AsyncTask<Void, Void, JSONObject>
    {
        //ResultSet Book;

        private ProgressDialog pdialog;

        @Override
        protected void onPreExecute()
        {
            Log.d("", "we are executing");
            pdialog = new ProgressDialog(AddBook.this);
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





    public void getfile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");      //all files
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // In case there is no File Manager (No phone with out file manager to test)
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == Activity.RESULT_OK)
                {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = uri.getPath();
                    input = new File(path);
                    File file = new File("file/path");
                    returnFile = file.getAbsoluteFile().getParent();
                    Toast.makeText(this, "" + input, Toast.LENGTH_LONG).show();

                    returnFile = input.toString();
                    //adds file location to edit text
                    ETFilePath.setText(returnFile);


                    break;
                }
            case RESULT_LOAD_IMAGE:
                super.onActivityResult(requestCode, resultCode, data);
                if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data !=null)
                {
                    selectedImage = data.getData();
                    Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
                    if (cursor == null) {
                        result = selectedImage.getPath();
                    } else
                    {
                        cursor.moveToFirst();
                        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        result = cursor.getString(idx);
                        cursor.close();
                        imageToUpload.setImageBitmap(BitmapFactory.decodeFile(result));

                    }


                }
        }

    }

    class UploadBook extends AsyncTask<String, Void, Void>
    {



        @Override
        protected Void doInBackground(String... params) {
            Log.v("uploading file", returnFile);
            FTPClient con = null;
            String file= params[0];
            String type = params[1];

            Log.v("passed in values",file+type);


            try
            {
                Boolean result;
                con = new FTPClient();
                con.connect("23.101.51.87");

                if (con.login("cosy\\eocribin", "Ecribin1"))
                {
                    Log.v("connection:","Successful");
                    con.enterLocalPassiveMode(); // important!
                    con.setFileType(FTP.BINARY_FILE_TYPE);
                    String data = file;

                    FileInputStream in = new FileInputStream(new File(data));
                    if(type=="image")
                    {
                        con.changeWorkingDirectory("site/wwwroot/images/");
                        result = con.storeFile(BookName + ".jpg", in);
                    }
                    if(type=="file")
                    {
                        con.changeWorkingDirectory("site/wwwroot/Books/");
                        result = con.storeFile(BookName + ".pdf", in);

                    }
                    else
                    {
                     result =false;
                    }
                    in.close();
                    if (result)
                        Log.v("upload result", "succeeded");
                    con.logout();
                    con.disconnect();
                }
            }
            catch (Exception e)
            {
                Log.v("connection:","failed");
                Log.e("HERE is what is wrong", String.valueOf(e));

                e.printStackTrace();
            }

            return null;


        }

        protected void onPostExecute(Void feed)
        {

        }


    }


    private class insertbook extends AsyncTask<Void, Void, Void>
    {
        //ResultSet Book;

        private ProgressDialog pdialog;



        @Override
        protected void onPreExecute()
        {
            Log.d("", "we are executing");
            pdialog = new ProgressDialog(AddBook.this);
            pdialog.setCancelable(false);
            pdialog.setMessage("Inserting Subject...");
            showdialog();



        }


        @Override
        protected Void doInBackground(Void... params)

        {
            String result;
            JSONObject jsonObject;



            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cosy.azurewebsites.net/insertbook.php");



                Log.v("values being posted", BookName +" "+ num);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair( "BookName", BookName));
                nameValuePairs.add(new BasicNameValuePair("SubjectID", num+""));
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


    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent homeintent = new Intent(this, MainActivity.class);
                startActivity(homeintent);
            default:
                return super.onOptionsItemSelected(item);


        }
    }



}
