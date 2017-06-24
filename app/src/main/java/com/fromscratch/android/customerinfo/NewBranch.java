package com.fromscratch.android.customerinfo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class NewBranch extends Activity {

    EditText branchaddress;
    TextView noaddress;
    Branches mbranch;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_branch);
        branchaddress = (EditText) findViewById(R.id.branch_address);
        noaddress = (TextView) findViewById(R.id.noaddress_log);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Branches");

    }

    public void create(View view) {
        if(branchaddress.getText().toString().trim().length()!=0){

            mbranch=new Branches(branchaddress.getText().toString(),generatepass());

            mDatabase.push().setValue(mbranch);


        }
        else{
            noaddress.setVisibility(View.VISIBLE);
        }
    }

    public void cancel_login2(View view) {
        this.finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if (noaddress.getVisibility() == View.VISIBLE) {
            outState.putInt("noaddressvis", 1);
        } else {
            outState.putInt("noaddressvis", 0);
        }
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getInt("noaddressvis", 0) == 1) {
            noaddress.setVisibility(View.VISIBLE);
        } else {
            noaddress.setVisibility(View.GONE);
        }
    }
    protected String generatepass()
    {
        Log.d("ahmed", "4444");
        long x1=System.currentTimeMillis();
        String pass="";

        for(int i=0;i<2;i++)
        {
            Random rand = new Random();
            int  s = rand.nextInt((122 - 97) + 1) + 97;
            int  c = rand.nextInt((90 - 65) + 1) + 65;
            char cap=(char)(c);
            char small=(char)(s);

            if(i%2==0)
                pass+=cap;
            else
                pass+=small;
        }
        long x2=(x1%1000);
        pass+=Long.toString(x2);

        return pass;
    }
}
