package net.azurewebsites.cosy;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Books extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        String[] books = {"matter of fact","AliveO", "Gaeilge" };
        ListAdapter BookAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, books);
        ListView BookList = (ListView) findViewById(R.id.booklist);
        BookList.setAdapter(BookAdapter);
    }

}
