package net.azurewebsites.cosy;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Questions extends ActionBarActivity {
    TextView TVQuestion;
    EditText ETQuestion, ETAnswer;
    Button Next, Previous;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        TVQuestion = (TextView) findViewById(R.id.TVQuestion);
        ETQuestion = (EditText) findViewById(R.id.ETQuestion);
        ETAnswer = (EditText) findViewById(R.id.ETAnswer);
        Next = (Button) findViewById(R.id.QNEXT);
        Previous = (Button) findViewById(R.id.QPrevious);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_questions, menu);
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
}
