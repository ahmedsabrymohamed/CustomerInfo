package com.fromscratch.android.customerinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BranchesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DatabaseReference mDatabaseReference;
    private ArrayList<Branch> mbranches;
    private ArrayList<String> branchesnames;
    private ArrayAdapter<String> branchesArrayAdapter;
    private ListView branches_list;
    private TextView noBranches;

    private boolean settings=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches);

        noBranches = (TextView) findViewById(R.id.noBranches);
        //
        // mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(mToolbar);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Branches");

        branches_list = (ListView) findViewById(R.id.branches_list);
        mbranches = new ArrayList<>();
        branchesnames = new ArrayList<>();
        branchesArrayAdapter = new ArrayAdapter<>(this, R.layout.branch_item, branchesnames);
        branches_list.setAdapter(branchesArrayAdapter);
        branches_list.setOnItemClickListener(this);

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Branch mBranch = dataSnapshot.getValue(Branch.class);
                mBranch.key = dataSnapshot.getKey();
                //Log.d("ahmed", "onChildRemoved: "+mBranch.key+"   &&   "+mBranch.address);
                mbranches.add(mBranch);
                branchesnames.add("العنوان: " + mBranch.address + "\n" +
                        "كلمة المرور: " + mBranch.password + "\n");
                branchesArrayAdapter.notifyDataSetChanged();

                if (mbranches.isEmpty() == false) {
                    noBranches.setVisibility(View.GONE);

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Branch mBranch = dataSnapshot.getValue(Branch.class);
                mBranch.key = dataSnapshot.getKey().toString();

                int i;
                for (i = 0; i < mbranches.size(); i++)
                    if (mBranch.key.equals(mbranches.get(i).key)) {
                        //Log.d("ahmed", "onChildChanged: "+mBranch.address);
                        break;

                    }
                mbranches.remove(i);
                branchesnames.remove(i);
                mbranches.add(i, mBranch);
                branchesnames.add(i, "العنوان: " + mBranch.address + "\n" +
                        "كلمة المرور: " + mBranch.password + "\n");
                branchesArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Branch mBranch = dataSnapshot.getValue(Branch.class);
                mBranch.key = dataSnapshot.getKey().toString();
               // Log.d("ahmed", "onChildRemoved: " + mBranch.key + "   &&   " + mBranch.address);
                int i;
                for (i = 0; i < mbranches.size(); i++)
                    if (mBranch.key.equals(mbranches.get(i).key))
                        break;
                mbranches.remove(i);
                branchesnames.remove(i);
                branchesArrayAdapter.notifyDataSetChanged();
                if (mbranches.isEmpty()) {
                    noBranches.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void add_Branch(View view) {
        Intent intent = new Intent(getBaseContext(), NewBranch.class);
        intent.putExtra("update_and_delete", false);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", mbranches);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Log.d("ahmed", "onItemClick: ");

        Intent intent;
        if (!settings) {
            intent = new Intent(getBaseContext(), NewBranch.class);
            intent.putExtra("update_and_delete", true);
            intent.putExtra("Branchtxt_password", mbranches.get(position).password);
            intent.putExtra("Branchtxt_address", mbranches.get(position).address);
        }
        else{

            intent = new Intent(getBaseContext(), CustomersActivity.class);

        }
        intent.putExtra("Branchtxt_key", mbranches.get(position).key);
        intent.putExtra("position", position);
        intent.putExtra("admin",true);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("data2", branchesnames);
        bundle.putParcelableArrayList("data2objects", mbranches);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_customers:
                // User chose the "Settings" item, show the app settings UI...
                settings = true;
                return true;

            case R.id.action_change:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                settings = false;
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                settings = true;
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("settings", settings);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        settings = savedInstanceState.getBoolean("settings", true);
        super.onRestoreInstanceState(savedInstanceState);
       // Log.d("ahmed", "onRestoreInstanceState: "+settings);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
