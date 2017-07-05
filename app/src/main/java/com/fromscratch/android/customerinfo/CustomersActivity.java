package com.fromscratch.android.customerinfo;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.Map;

public class CustomersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private TextView noCustomers;
    private DatabaseReference mDatabaseReference;
    private ListView customer_list;
    private ArrayList<Customer>customers;
    private ArrayList<Customer>branchcustomers;
    private ArrayList<Branch>data2objects;
    private ArrayList<String> data2;
    private ArrayList<String>customersnames;
    private ArrayAdapter<String>customerArrayAdapter;
    private String branch_address;
    private boolean admin;
    private Map branches_map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        noCustomers=(TextView)findViewById(R.id.noCustomers);



        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("Customers");

        customer_list=(ListView)findViewById(R.id.list);

        branches_map=new HashMap();

        customers=new ArrayList<>();
        branchcustomers=new ArrayList<>();

        customersnames=new ArrayList<>();

        customerArrayAdapter=new ArrayAdapter<>(this,R.layout.customer_item,customersnames);

        customer_list.setAdapter(customerArrayAdapter);

        customer_list.setOnItemClickListener(this);

        Intent intent = getIntent();

        Bundle bundle = getIntent().getExtras();

        admin=intent.getBooleanExtra("admin",false);

        data2objects=bundle.getParcelableArrayList("data2objects");

        data2  = bundle.getStringArrayList("data2");

        for(int i=0;i<data2objects.size();i++)
        {
            branches_map.put(data2objects.get(i).key,data2objects.get(i).address);
           // Log.d("ahmed", "onCreate: "+data2objects.get(i).key+"       "+data2objects.get(i).address);
        }

  //  if(admin==false)
        branch_address=(admin?intent.getStringExtra("Branchtxt_key"):intent.getStringExtra("branch_address"));
        Log.d("ahmed", "onCreate: "+branch_address);
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Customer mCustomer=dataSnapshot.getValue(Customer.class);
                mCustomer.key=dataSnapshot.getKey();
                customers.add(mCustomer);
                //Log.d("ahmed", "onChildAdded: "+mCustomer.branch_address.trim()+"     "+branch_address.trim());
                if(branch_address.trim().equals(mCustomer.branch_address.trim()))
                {
                    branchcustomers.add(mCustomer);
                    customersnames.add("الأسم: "+mCustomer.name+"\n"+
                            "ت: "    +mCustomer.phone+"\n"+
                            "العنوان: "+mCustomer.address+"\n"+
                            "الفرع: "+branches_map.get(mCustomer.branch_address));
                }
                if(branches_map.get(mCustomer.branch_address)==null)
                    mDatabaseReference.child(mCustomer.key).removeValue();
              //  Log.d("ahmed", "onChildAdded: "+branchcustomers.size()+"       "+branch_address);

                customerArrayAdapter.notifyDataSetChanged();

                if(branchcustomers.isEmpty()==false)
                {
                    noCustomers.setVisibility(View.GONE);

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Customer mCustomer=dataSnapshot.getValue(Customer.class);
                mCustomer.key=dataSnapshot.getKey().toString();
                int i;
                //all data change
                Log.d("ahmed", "onChildChanged: "+branchcustomers.size()+"       "+branch_address);
                for( i=0;i<customers.size();i++)
                    if(mCustomer.key.equals(customers.get(i).key))
                    {
                        customers.remove(i);
                        customers.add(i,mCustomer);
                        break;
                    }



                //branch data change

                for( i=0;i<branchcustomers.size();i++)
                    if(mCustomer.key.equals(branchcustomers.get(i).key))
                    {
                        branchcustomers.remove(i);

                        customersnames.remove(i);
                        break;
                    }



                if(branch_address.trim().equals(mCustomer.branch_address.trim()))
                {
                    customersnames.add(i,"الأسم: "+mCustomer.name+"\n"+
                            "ت: "+mCustomer.phone+"\n"+
                            "العنوان: "+mCustomer.address+"\n"+
                            "الفرع: "+branches_map.get(mCustomer.branch_address));
                    branchcustomers.add(i,mCustomer);
                }

                customerArrayAdapter.notifyDataSetChanged();
                if(branchcustomers.isEmpty())
                {
                    noCustomers.setVisibility(View.VISIBLE);

                }
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

               // Log.d("ahmed", "onChildAdded: "+branchcustomers.size());
                for( i=0;i<branchcustomers.size();i++)
                    if(mCustomer.key.equals(branchcustomers.get(i).key))
                    {
                        branchcustomers.remove(i);
                        customersnames.remove(i);
                        break;
                    }


                customerArrayAdapter.notifyDataSetChanged();
                if(branchcustomers.isEmpty())
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

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", customers);
        bundle.putParcelableArrayList("data2objects", data2objects);
        bundle.putStringArrayList("data2",data2);
        intent.putExtras(bundle);
        intent.putExtra("update_and_delete",false);
        if(admin)
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
        if(admin) {

            Intent intent = new Intent(getBaseContext(), NewCustomerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("data", customers);
            bundle.putParcelableArrayList("data2objects", data2objects);
            bundle.putStringArrayList("data2",data2);

            intent.putExtras(bundle);
            intent.putExtra("update_and_delete", true);
            intent.putExtra("Coustomertxt_key", branchcustomers.get(position).key);
            intent.putExtra("Coustomertxt_name", branchcustomers.get(position).name);
            intent.putExtra("Coustomertxt_phone", branchcustomers.get(position).phone);
            intent.putExtra("Coustomertxt_address", branchcustomers.get(position).address);
            intent.putExtra("Coustomertxt_branch_address", branchcustomers.get(position).branch_address);
 //           Log.d("ahmed", "onItemClick: "+customers.get(position).branch_address);
            startActivity(intent);

        }

    }
}
