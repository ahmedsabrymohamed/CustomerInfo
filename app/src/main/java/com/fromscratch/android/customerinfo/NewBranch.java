package com.fromscratch.android.customerinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class NewBranch extends AppCompatActivity {

    private EditText branchaddress;
    private TextView noaddress;
    private Branch mbranch;
    private Button delete2;
    private String key;
    private String pass;
    private boolean update;
    private String dialog_pass;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_branch);
        branchaddress = (EditText) findViewById(R.id.branch_address);
        noaddress = (TextView) findViewById(R.id.noaddress_log);
        delete2=(Button)findViewById(R.id.NewBranch__delete_button);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Branches");
        Intent intent = getIntent();
        update=intent.getBooleanExtra("update_and_delete",false);

        if(update)
        {
            delete2.setVisibility(View.VISIBLE);
            branchaddress.setText(intent.getStringExtra("Branchtxt_address"));
            pass=intent.getStringExtra("Branchtxt_password");
            key=intent.getStringExtra("Branchtxt_key");
        }
    }

    public void create(View view) {
        if(branchaddress.getText().toString().trim().length()!=0){


            if(update)
            {
                mbranch=new Branch(branchaddress.getText().toString(),pass);
                mDatabase.child(key).setValue(mbranch);
            }
            else
            {
                pass = generatepass();
                mbranch = new Branch(branchaddress.getText().toString(), pass);

                mDatabase.push().setValue(mbranch);
            }

            branchaddress.getText().clear();

            Toast toast=Toast.makeText(this,"العمليه تمت بنجاح",Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(getBaseContext(), dialog.class);
            intent.putExtra("pass", pass);
            startActivity(intent);
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


    public void  delete2 (View view){
        mDatabase.child(key).removeValue();
        branchaddress.getText().clear();
        Toast toast=Toast.makeText(this,"العمليه تمت بنجاح",Toast.LENGTH_LONG);
        toast.show();

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
