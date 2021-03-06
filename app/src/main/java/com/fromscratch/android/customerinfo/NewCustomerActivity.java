package com.fromscratch.android.customerinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NewCustomerActivity extends AppCompatActivity {

    private EditText Customer_phone;
    private EditText Customer_address;
    private EditText Customer_name;
    private Spinner branch_address;
    private String branch_address_value;
    private TextView Customer_phone_log;
    private TextView Customer_address_log;
    private TextView Customer_name_log;
    private TextView branch_address_log;

    private Button delete;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference2;
    private ArrayList<Customer> customerslist;
    private ArrayList<String> branchesnames;
    private ArrayList<Branch> branches_Arraylist;
    private ArrayAdapter<String> SpinnerAdapter;
    private boolean update, admin;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Customers");
        mDatabaseReference2 = FirebaseDatabase.getInstance().getReference().child("Branches");

        Customer_phone = (EditText) findViewById(R.id.addCustomer_phone);
        Customer_address = (EditText) findViewById(R.id.addCustomer_address);
        Customer_name = (EditText) findViewById(R.id.addCustomer__name);
        branch_address = (Spinner) findViewById(R.id.addCustomer_branch_address);

        Customer_phone_log = (TextView) findViewById(R.id.noCustomer_phone_log);
        Customer_address_log = (TextView) findViewById(R.id.noCustomer_address_log);
        Customer_name_log = (TextView) findViewById(R.id.noCustomer_name_log);
        branch_address_log = (TextView) findViewById(R.id.noCustomer_branch_log);

        delete = (Button) findViewById(R.id.NewCustomer__delete_button);
        customerslist = new ArrayList<>();
        branchesnames = new ArrayList<>();

        branchesnames.add(0, "عنوان الفرع");
        branches_Arraylist = new ArrayList<>();

        SpinnerAdapter = new ArrayAdapter<>(this, R.layout.customer_item, branchesnames);
        branch_address.setAdapter(SpinnerAdapter);
        SpinnerAdapter.notifyDataSetChanged();

        //fitch the data of the branches and the customers and updating it
    }

    public void create2(View view) {
        boolean v1, v2, v3, v4;
        if (Customer_phone.getText().toString().trim().isEmpty()) {
            v1 = false;
            Customer_phone_log.setVisibility(View.VISIBLE);
        } else {
            v1 = true;
            Customer_phone_log.setVisibility(View.GONE);
        }
        if (Customer_address.getText().toString().trim().isEmpty()) {
            v2 = false;
            Customer_address_log.setVisibility(View.VISIBLE);
        } else {
            v2 = true;
            Customer_address_log.setVisibility(View.GONE);
        }
        if (Customer_name.getText().toString().trim().isEmpty()) {
            v3 = false;
            Customer_name_log.setVisibility(View.VISIBLE);
        } else {
            v3 = true;
            Customer_name_log.setVisibility(View.GONE);
        }
        if (branch_address.getSelectedItemPosition() == 0) {
            v4 = false;
            branch_address_log.setVisibility(View.VISIBLE);
        } else {
            v4 = true;
            branch_address_log.setVisibility(View.GONE);
        }
        if (v1 && v2 && v3 && v4) {
            Customer customer = new Customer(Customer_name.getText().toString(),
                    Customer_address.getText().toString(),
                    Customer_phone.getText().toString()
                    , branches_Arraylist.get(branch_address.getSelectedItemPosition() - 1).key);
           // Log.d("ahmed", "create2: "+branches_Arraylist.get(branch_address.getSelectedItemPosition() - 1).key);
            if (customerslist != null) {
                for (Customer x : customerslist) {
                    boolean t1 = false, t2 = false, t3 = false;
                   // Log.d("ass", "create2: "+customerslist.size());
                    if (x.key.equals(key) == false) {
                        if (x.phone.trim().equals(Customer_phone.getText().toString().trim()))
                            t1 = true;
                        if (x.address.trim().equals(Customer_address.getText().toString().trim()))
                            t2 = true;
                        if (x.name.trim().equals(Customer_name.getText().toString().trim()))
                            t3 = true;
                        if ((t1 && t2) || (t2 && t3) || (t1 && t3)) {
                            Toast toast = Toast.makeText(this, "هذا العميل موجود من قبل", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                    }
                }
            } else {
                customerslist = new ArrayList<Customer>();
            }
            if (update) {
                mDatabaseReference.child(key).setValue(customer);
            } else {
                mDatabaseReference.push().setValue(customer);
            }
            mDatabaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Customer mCustomer = dataSnapshot.getValue(Customer.class);
                    mCustomer.key = dataSnapshot.getKey();
                    customerslist.add(mCustomer);
                    //mDatabaseReference.removeEventListener(this);
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

            //Customer_name.getText().clear();
            //Customer_address.getText().clear();
            //branch_address.getText().clear();
            //Customer_phone.getText().clear();

            Toast toast = Toast.makeText(this, "العمليه تمت بنجاح", Toast.LENGTH_SHORT);
            toast.show();

        }
    }

    /////////////////////////////////////////////DELETE///////////////////////////////////////////////////////
    public void delete(View view) {
        mDatabaseReference.child(key).removeValue();
        Customer_name.getText().clear();
        Customer_address.getText().clear();
        branch_address.setSelection(0);
        Customer_phone.getText().clear();
        Toast toast = Toast.makeText(this, "العمليه تمت بنجاح", Toast.LENGTH_SHORT);
        view.setVisibility(View.GONE);
        toast.show();
    }

    public void cancel_login3(View view) {
        this.finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt("Customer_phone_log", ((Customer_phone_log.getVisibility() == View.VISIBLE) ? 1 : 0));
        outState.putInt("Customer_address_log", ((Customer_address_log.getVisibility() == View.VISIBLE) ? 1 : 0));
        outState.putInt("Customer_name_log", ((Customer_name_log.getVisibility() == View.VISIBLE) ? 1 : 0));
        outState.putInt("branch_address_log", ((branch_address_log.getVisibility() == View.VISIBLE) ? 1 : 0));
        outState.putInt("butt_delete", ((delete.getVisibility() == View.VISIBLE) ? 1 : 0));
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Customer_phone_log.setVisibility(((savedInstanceState.getInt("Customer_phone_log", 0) == 1) ? View.VISIBLE : View.GONE));
        Customer_address_log.setVisibility(((savedInstanceState.getInt("Customer_address_log", 0) == 1) ? View.VISIBLE : View.GONE));
        Customer_name_log.setVisibility(((savedInstanceState.getInt("Customer_name_log", 0) == 1) ? View.VISIBLE : View.GONE));
        branch_address_log.setVisibility(((savedInstanceState.getInt("branch_address_log", 0) == 1) ? View.VISIBLE : View.GONE));
        delete.setVisibility(((savedInstanceState.getInt("butt_delete", 0) == 1) ? View.VISIBLE : View.GONE));
    }

    @Override
    protected void onStart() {
        Intent intent = getIntent();


        update = intent.getBooleanExtra("update_and_delete", false);
        admin = intent.getBooleanExtra("admin", true);
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Customer customer = dataSnapshot.getValue(Customer.class);
                customer.key = dataSnapshot.getKey();
              //  Log.d("ahmed", "onChildAdded: " + customer.key);
                customerslist.add(customer);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Customer mCustomer = dataSnapshot.getValue(Customer.class);
                mCustomer.key = dataSnapshot.getKey().toString();
                for (int i = 0; i < customerslist.size(); i++) {
                    //Log.d("ahmed", "onChildChanged: " + customerslist.size());

                        if (customerslist.get(i).key.equals(mCustomer.key)) {
                        customerslist.remove(i);
                        customerslist.add(i, mCustomer);
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for (int i = 0; i < customerslist.size(); i++) {
                 //   Log.d("ahmed", "onChildRemoved: " + customerslist.get(i).key);
                    if (customerslist.get(i).key.equals(dataSnapshot.getKey().trim())) {
                        customerslist.remove(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (update) {
            key = intent.getStringExtra("Coustomertxt_key");
            Customer_name.setText(intent.getStringExtra("Coustomertxt_name"));
            Customer_address.setText(intent.getStringExtra("Coustomertxt_address"));
            Customer_phone.setText(intent.getStringExtra("Coustomertxt_phone"));
            branch_address_value = intent.getStringExtra("Coustomertxt_branch_address");

        }
        if(admin==false)
            branch_address_value =intent.getStringExtra("branch_address");
        else
            branch_address_value="";
        mDatabaseReference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Branch mBranch = dataSnapshot.getValue(Branch.class);
                mBranch.key = dataSnapshot.getKey();

                branches_Arraylist.add(mBranch);
                branchesnames.add("العنوان: " + mBranch.address + "\n" +
                        "كلمة المرور: " + mBranch.password + "\n");
                if ( branch_address_value.equals(mBranch.key)) {
                    branch_address.setSelection(branches_Arraylist.size());
                }
                SpinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Branch mBranch = dataSnapshot.getValue(Branch.class);
                mBranch.key = dataSnapshot.getKey().toString();

                int i;
                for (i = 0; i < branches_Arraylist.size(); i++)
                    if (mBranch.key.equals(branches_Arraylist.get(i).key)) {

                        branches_Arraylist.remove(i);
                        branchesnames.remove(i);
                        branches_Arraylist.add(i, mBranch);
                        branchesnames.add(i, "العنوان: " + mBranch.address + "\n" +
                                "كلمة المرور: " + mBranch.password + "\n");
                        break;

                    }


                SpinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Branch mBranch = dataSnapshot.getValue(Branch.class);
                mBranch.key = dataSnapshot.getKey().toString();

                int i;
                for (i = 0; i < branches_Arraylist.size(); i++)
                    if (mBranch.key.equals(branches_Arraylist.get(i).key)) {
                        branches_Arraylist.remove(i);
                        branchesnames.remove(i);
                        break;
                    }

                SpinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if (admin && update)
            delete.setVisibility(View.VISIBLE);
        if(admin==false)
            branch_address.setVisibility(View.GONE);


        super.onStart();

    }
}
