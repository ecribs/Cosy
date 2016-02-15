package net.azurewebsites.cosy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Books extends Activity
{
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://cosy.azurewebsites.net/";
    public static  String[] BookName;
    public static  String[] BookSubject;
    UserLocalStore userLocalStore;




    public void GetBooks(User user)
    {

        new GetBooksAsyncTask(user).execute();


    }

    public class GetBooksAsyncTask extends AsyncTask<Void, Void, Void>
    {

        User user;

        public GetBooksAsyncTask(User user)
        {
            this.user = user;
        }


        protected Void doInBackground(Void... params)
        {
            ArrayList<NameValuePair> dataToSend = new ArrayList();
            dataToSend.add(new BasicNameValuePair("username", user.username));


            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);

            HttpPost post = new HttpPost(SERVER_ADDRESS + "bookrequest.php");

            try {


                Log.v("posting", "2");

                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
                Log.v("getting response", "2");

                HttpEntity entity = httpResponse.getEntity();

                Log.v("covnverting to string", "2");

                String result = EntityUtils.toString(entity);
                JSONObject jsonObject = new JSONObject(result);

                // for getting booknames
                Log.v("getting a book name", "2");

                JSONArray jsonArray = jsonObject.getJSONArray("Bookname");
                BookName = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    BookName[i] = jsonArray.getString(i);
                }

// for getting subjectnames
                Log.v("we're getting a subject", "2");

                jsonArray = jsonObject.getJSONArray("SubjectName");
                BookSubject = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    BookSubject[i] = jsonArray.getString(i);
                }


                //return BookName, BookSubject};


            }
                catch (Exception e)
            {
                e.printStackTrace();
            }


            return null;
        }

        protected void onPostExecute()
        {
            if(BookName==null)
            {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Books.this);
                dialogBuilder.setMessage("ERROR: Books couldn't download");
                dialogBuilder.setPositiveButton("Try Again", null);
                dialogBuilder.show();


            }



        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        User user = userLocalStore.getLoggedInUser();

        GetBooks(user);



        // String[] books = {"matter of fact","AliveO", "Gaeilge" };
        //String[] subjects = {"science", "religion", "irish"};
        ListAdapter BookAdapter = new BookAdapter(this, BookName, BookSubject);
        ListView BookList = (ListView) findViewById(R.id.booklist);
        BookList.setAdapter(BookAdapter);

        BookList.setOnItemClickListener
                (
                        new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                String url = "https://cosy.azurewebsites.net/Books/Books/" + BookName + ".pdf";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                                //String books = String.valueOf(parent.getItemAtPosition(position));
                                // Toast.makeText(Books.this, books, Toast.LENGTH_LONG).show();
                            }
                        }

                );
    }


    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.menu_books, menu);










        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item)
    {


        switch(item.getItemId())
        {
            case R.id.addbook:
            {
                if(item.isChecked())
                {
                    item.setChecked(false);
                }

                else
                {
                    item.setChecked(true);
                }


                return true;


            }

        }
        return true;


    }

}
