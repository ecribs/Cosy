package net.azurewebsites.cosy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements View.OnClickListener
{

    Button worksheet, books, Class, admin;

    UserLocalStore userLocalStore;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        worksheet = (Button) findViewById(R.id.bWorksheet);
        books = (Button) findViewById(R.id.bBooks);
        Class = (Button) findViewById(R.id.Bclass);
        admin = (Button) findViewById(R.id.Badmin);

        userLocalStore = new UserLocalStore(this);


    }




    @Override
    protected void onStart() {
        super.onStart();
        if (authenticate() == true)
        {
            Log.v( "","hello");
        }
    }

    private boolean authenticate()
    {
        if (userLocalStore.getLoggedInUser() == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            return false;
        }
        return true;
    }





    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.bBooks:

                break;

            case R.id.bWorksheet:

                break;

            case R.id.Bclass:

                break;

            case R.id.Badmin:

                break;

        }


    }



}
