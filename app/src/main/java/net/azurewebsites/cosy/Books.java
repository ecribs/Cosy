package net.azurewebsites.cosy;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Books extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        String[] books = {"matter of fact","AliveO", "Gaeilge" };
        String[] subjects = {"science", "religion", "irish"};
        ListAdapter BookAdapter = new BookAdapter(this, books, subjects);
        ListView BookList = (ListView) findViewById(R.id.booklist);
        BookList.setAdapter(BookAdapter);

        BookList.setOnItemClickListener
                (
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        String books = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(Books.this, books, Toast.LENGTH_LONG).show();
                    }
                }

                );
    }

}
