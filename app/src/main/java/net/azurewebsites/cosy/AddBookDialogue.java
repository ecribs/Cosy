package net.azurewebsites.cosy;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class AddBookDialogue extends Dialog implements View.OnClickListener {


    public Activity c;
    public Dialog d;
    public Button browse, submit;

    public AddBookDialogue(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_book_dialogue);
        browse = (Button) findViewById(R.id.browse);
        submit = (Button) findViewById(R.id.submit);
        browse.setOnClickListener(this);
        submit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.browse:
                c.finish();
                break;
            case R.id.submit:
                dismiss();
                break;
            default:
                break;
        }


    }
}
