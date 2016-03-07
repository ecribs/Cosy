package net.azurewebsites.cosy;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;

public class AddBook extends ActionBarActivity {
Button Browse;
    TextView ETFilePath;
    String returnFile;
    private static final int FILE_SELECT_CODE = 0;
    File input;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        Browse = (Button) findViewById(R.id.Browse);
        ETFilePath = (TextView) findViewById(R.id.ETFILEPATH);

        Browse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                getfile(v);
                new UploadBook().execute();

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                super.onActivityResult(requestCode, resultCode, data);
        }

    }

    class UploadBook extends AsyncTask<String, Void, Void>
    {



        @Override
        protected Void doInBackground(String... params)
        {
            Log.v("uploading file", returnFile);
            FTPClient con = null;


            try
            {
                con = new FTPClient();
                con.connect("23.101.51.87");

                if (con.login("cosy\\eocribin", "Ecribin1"))
                {
                    Log.v("connection:","Successful");
                    con.enterLocalPassiveMode(); // important!
                    con.setFileType(FTP.BINARY_FILE_TYPE);
                    String data = returnFile;

                    FileInputStream in = new FileInputStream(new File(data));
                    
                    boolean result = con.storeFile("test.pdf", in);
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


}
