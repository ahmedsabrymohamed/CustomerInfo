package com.fromscratch.android.customerinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class login extends AppCompatActivity {

    TextView wrong;
    EditText password;
    private DatabaseReference mDatabase;
    private ArrayList<Branch> branches;
    boolean login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        password = (EditText) findViewById(R.id.password);
        wrong = (TextView) findViewById(R.id.wrong_log);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Branches");

    }


    @Override
    protected void onStart()
    {
        branches = new ArrayList<>();


        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Branch mBranch = dataSnapshot.getValue(Branch.class);
                mBranch.key = dataSnapshot.getKey();
                branches.add(mBranch);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        super.onStart();
    }


    public void login_butt(View view) {
        login = false;
        if (password.getText().toString().trim().equals("123abc")) {
            login = true;
            Intent intent = new Intent(getBaseContext(), BranchesActivity.class);
            startActivity(intent);
            //finish_();
        }

        for (Branch mBranch : branches)
            if (password.getText().toString().trim().equals(mBranch.password.toString().trim())) {
                //Log.d("ahmed", "onChildAdded: " + mBranch.password + "   " + password.getText().toString());
                login = true;
                Intent intent = new Intent(getBaseContext(), CustomersActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelableArrayList("data2objects", branches);
                intent.putExtras(mBundle);
                intent.putExtra("branch_address", mBranch.key);
                intent.putExtra("admin", false);
                startActivity(intent);
                wrong.setVisibility(View.GONE);
                //finish_();
            }
        if (login == false)
            wrong.setVisibility(View.VISIBLE);
    }

   /* public void cancel_login(View view) {
        this.finish();

    }*/

    public void finish_() {
        this.finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if (wrong.getVisibility() == View.VISIBLE) {
            outState.putInt("wrongvis", 1);
        } else {
            outState.putInt("wrongvis", 0);
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
