package net.azurewebsites.cosy;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends ActionBarActivity implements View.OnClickListener
{

    Button bLogin;
    EditText etUsername, etPassword;

    UserLocalStore userLocalStore;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        userLocalStore = new UserLocalStore(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        bLogin = (Button) findViewById(R.id.bLogin);

        bLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.bLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if(username==null || password == null)
                {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
                    dialogBuilder.setMessage("ERROR: Please enter both username and password");
                    dialogBuilder.setPositiveButton("Try Again", null);
                    dialogBuilder.show();

                }

                else
                {
                    User user = new User(username, password);
                    authenticate(user);
                    break;
                }

        }



    }



    private void authenticate(User user)
    {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.fetchUserDataAsyncTask(user, new GetUserCallback()
        {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null)
                {
                    showErrorMessage();
                } else
                {
                    logUserIn(returnedUser);
                }
            }
        });
    }

    private void showErrorMessage()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("ERROR: Incorrect Username or Password");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void logUserIn(User returnedUser)
    {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        startActivity(new Intent(this, MainActivity.class));
    }

}