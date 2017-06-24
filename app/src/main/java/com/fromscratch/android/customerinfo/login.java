package com.fromscratch.android.customerinfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class login extends Activity {

    TextView wrong;
    EditText password;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        password = (EditText) findViewById(R.id.password);
        wrong = (TextView) findViewById(R.id.wrong_log);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Branches");
    }


    @Override
    protected void onStart() {
        super.onStart();
    }



    public void login_butt(View view) {


    }

    public void cancel_login(View view) {
        this.finish();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if(wrong.getVisibility()==View.VISIBLE)
        {
            outState.putInt("wrongvis",1);
        }
        else
        {
            outState.putInt("wrongvis",0);
        }
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getInt("wrongvis", 0) == 1) {
            wrong.setVisibility(View.VISIBLE);
        } else {
            wrong.setVisibility(View.GONE);
        }
    }
}
