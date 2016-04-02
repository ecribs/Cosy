package net.azurewebsites.cosy;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<String>
    {
        String[] books = {};
        String[] subjects = {};
        Context c;
        LayoutInflater inflater;

        public BookAdapter(Context context, String[] books, String[] subjects) {
        super(context,R.layout.bookrow, books);
            this.c=context;
            this.books = books;
            this.subjects = subjects;


    }

        public class ViewHolder
        {
            TextView subject;
            TextView name;

        }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    if(convertView==null)
    {
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.bookrow,null);

    }
        //LayoutInflater booksinflater = LayoutInflater.from(getContext());
        //View customView = booksinflater.inflate(R.layout.bookrow, parent, false);

        final ViewHolder holder = new ViewHolder();
        holder.name = (TextView) convertView.findViewById(R.id.BookName);
        holder.subject = (TextView) convertView.findViewById(R.id.BookSubject);

        holder.name.setText(books[position]);
        holder.subject.setText(subjects[position]);



        String singleBookItem = getItem(position);
       // String singleBookSubject = getItem(position);
       // TextView bookName = (TextView) customView.findViewById(R.id.BookName);
        //ImageView bookImage = (ImageView) customView.findViewById(R.id.bookimage);
        //TextView bookSubject = (TextView) customView.findViewById(R.id.BookSubject);


        //bookName.setText(singleBookItem);
        String urlstuff =  books[position].replace(" ", "%20");
        new DownloadImageTask((ImageView) convertView.findViewById(R.id.bookimage)).execute("http://cosy.azurewebsites.net/images/" +  urlstuff + ".jpg");
        //bookSubject.setText(singleBookSubject);

        return convertView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }


    }









}
