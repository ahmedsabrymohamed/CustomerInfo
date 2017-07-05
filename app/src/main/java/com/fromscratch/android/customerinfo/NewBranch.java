package com.fromscratch.android.customerinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
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
    private ArrayList<Branch>data;
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
        Bundle bundle = getIntent().getExtras();
        data = bundle.getParcelableArrayList("data2objects");
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
                if(data!=null) {
                    for (Branch x : data)
                        if (!x.key.equals(key) && x.address.trim().equals(branchaddress.getText().toString().trim())) {
                            Toast toast = Toast.makeText(this, "هذا الفرع موجود من قبل", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                }
            else
                {
                    data=new ArrayList<>();
                }
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
                mDatabase.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Branch mBranch=dataSnapshot.getValue(Branch.class);
                        mBranch.key=dataSnapshot.getKey();
                        data.add(mBranch);
                        //Log.d("ahmed", "create22: "+mBranch.key);
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


            }

            //branchaddress.getText().clear();

            Toast toast=Toast.makeText(this,"العمليه تمت بنجاح",Toast.LENGTH_SHORT);
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
        view.setVisibility(View.GONE);
        Toast toast=Toast.makeText(this,"العمليه تمت بنجاح",Toast.LENGTH_SHORT);
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
        //Log.d("ahmed", "4444");
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
