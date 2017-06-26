package com.fromscratch.android.customerinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private FloatingActionButton add;
    private TextView noCustomers;
    private DatabaseReference mDatabaseReference;
    private ListView customer_list;
    private ArrayList<Customer>customers;
    private ArrayList<String>customersnames;
    private ArrayAdapter<String>customerArrayAdapter;
    private String branch_address;
    private boolean admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        noCustomers=(TextView)findViewById(R.id.noCustomers);

        add=(FloatingActionButton)findViewById(R.id.add_Customer);

        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("Customers");

        customer_list=(ListView)findViewById(R.id.list);
        customers=new ArrayList<Customer>();
        customersnames=new ArrayList<String>();
        customerArrayAdapter=new ArrayAdapter<String>(this,R.layout.customer_item,customersnames);
        customer_list.setAdapter(customerArrayAdapter);
        customer_list.setOnItemClickListener(this);
        Intent intent = getIntent();
        admin=intent.getBooleanExtra("admin",false);
        Log.d("ahmed", "onCreate cc: "+admin);
        if(admin==false)
        branch_address=intent.getStringExtra("branch_address");

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Customer mCustomer=dataSnapshot.getValue(Customer.class);
                mCustomer.key=dataSnapshot.getKey();
                customers.add(mCustomer);
                customersnames.add("الأسم: "+mCustomer.name+"\n"+
                "ت: "+mCustomer.phone+"\n"+
                "العنوان: "+mCustomer.address+"\n"+
                "الفرع: "+mCustomer.branch_address);
                customerArrayAdapter.notifyDataSetChanged();

                if(customers.isEmpty()==false)
                {
                    noCustomers.setVisibility(View.GONE);

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Customer mCustomer=dataSnapshot.getValue(Customer.class);
                mCustomer.key=dataSnapshot.getKey().toString();
                int i;
                for( i=0;i<customers.size();i++)
                    if(mCustomer.key.equals(customers.get(i).key))
                        break;
                //Log.d("ahmed", "onChildChanged: "+i);
                customers.remove(i);
                customers.add(i,mCustomer);
                customersnames.remove(i);
                customersnames.add(i,"الأسم: "+mCustomer.name+"\n"+
                        "ت: "+mCustomer.phone+"\n"+
                        "العنوان: "+mCustomer.address+"\n"+
                        "الفرع: "+mCustomer.branch_address);
                customerArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Customer mCustomer=dataSnapshot.getValue(Customer.class);
                mCustomer.key=dataSnapshot.getKey().toString();
                int i;
                for( i=0;i<customers.size();i++)
                    if(mCustomer.key.equals(customers.get(i).key))
                        break;
                customers.remove(i);
                customersnames.remove(i);
                customerArrayAdapter.notifyDataSetChanged();
                if(customers.isEmpty())
                {
                    noCustomers.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast s=Toast.makeText(getBaseContext(),"NO INTERNET",Toast.LENGTH_LONG);
                s.show();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if(noCustomers.getVisibility()==View.VISIBLE)
        {
            outState.putInt("noCustomersvis",1);
        }
        else
        {
            outState.putInt("noCustomersvis",0);
        }
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getInt("noCustomersvis", 0) == 1) {
            noCustomers.setVisibility(View.VISIBLE);
        } else {
            noCustomers.setVisibility(View.GONE);
        }
    }
    public void add_Customer(View view)
    {
        Intent intent = new Intent(getBaseContext(), NewCustomerActivity.class);

        intent.putExtra("update_and_delete",false);
        if(admin==true)
        intent.putExtra("admin",true);
        else
        {
            intent.putExtra("admin",false);
            intent.putExtra("branch_address",branch_address);
        }
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       // Log.d("ahmed", "onItemClick: ");
        if(admin) {

            Intent intent = new Intent(getBaseContext(), NewCustomerActivity.class);
            intent.putExtra("update_and_delete", true);
            intent.putExtra("Coustomertxt_key", customers.get(position).key);
            intent.putExtra("Coustomertxt_name", customers.get(position).name);
            intent.putExtra("Coustomertxt_phone", customers.get(position).phone);
            intent.putExtra("Coustomertxt_address", customers.get(position).address);
            intent.putExtra("Coustomertxt_branch_address", customers.get(position).branch_address);
            startActivity(intent);

        }

    }
}
