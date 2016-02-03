package net.azurewebsites.cosy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookAdapter extends ArrayAdapter<String>
{
    BookAdapter(Context context, String[] books, String[] subjects)
    {
        super(context, R.layout.bookrow, books);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        LayoutInflater booksinflater = LayoutInflater.from(getContext());
        View customView = booksinflater.inflate(R.layout.bookrow, parent, false);

        String singleBookItem = getItem(position);
        String singleBookSubject = getItem(position);
        TextView bookName = (TextView) customView.findViewById(R.id.BookName);
        ImageView bookImage = (ImageView) customView.findViewById(R.id.bookimage);
        TextView bookSubject = (TextView) customView.findViewById(R.id.BookSubject);


        bookName.setText(singleBookItem);
        bookImage.setImageResource(R.drawable.matter_of_fact);
        bookSubject.setText(singleBookSubject);

        return customView;
    }
}
