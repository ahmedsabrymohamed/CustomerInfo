package com.fromscratch.android.customerinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewCustomerActivity extends AppCompatActivity {

    private EditText Customer_phone;
    private EditText Customer_address;
    private EditText Customer_name;
    private EditText branch_address;
    private TextView Customer_phone_log;
    private TextView Customer_address_log;
    private TextView Customer_name_log;
    private TextView branch_address_log;

    Button delete;
    DatabaseReference mDatabaseReference;

    boolean update,admin;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("Customers");
        setContentView(R.layout.activity_new_customer);

        Customer_phone=(EditText)findViewById(R.id.addCustomer_phone);
        Customer_address=(EditText)findViewById(R.id.addCustomer_address);
        Customer_name=(EditText)findViewById(R.id.addCustomer__name);
        branch_address=(EditText)findViewById(R.id.addCustomer_branch_address);

        Customer_phone_log=(TextView)findViewById(R.id.noCustomer_phone_log);
        Customer_address_log=(TextView)findViewById(R.id.noCustomer_address_log);
        Customer_name_log=(TextView)findViewById(R.id.noCustomer_name_log);
        branch_address_log=(TextView)findViewById(R.id.noCustomer_branch_log);

        delete=(Button)findViewById(R.id.NewCustomer__delete_button);

        Intent intent = getIntent();
        update=intent.getBooleanExtra("update_and_delete",false);
        admin=intent.getBooleanExtra("admin",true);
        if(update)
        {
            key=intent.getStringExtra("Coustomertxt_key");
            Customer_name.setText(intent.getStringExtra("Coustomertxt_name"));
            Customer_address.setText(intent.getStringExtra("Coustomertxt_address"));
            Customer_phone.setText(intent.getStringExtra("Coustomertxt_phone"));
            branch_address.setText(intent.getStringExtra("Coustomertxt_branch_address"));
        }
        if(admin==false)
        {
            branch_address.setText(intent.getStringExtra("branch_address"));
            branch_address.setVisibility(View.GONE);

        }
        if(admin&&update)
            delete.setVisibility(View.VISIBLE);
    }

    public void create2(View view)
    {
        boolean v1=false,v2=false,v3=false,v4=false;
        if(Customer_phone.getText().toString().trim().isEmpty())
        {
            v1=false;
            Customer_phone_log.setVisibility(View.VISIBLE);
        }
        else
        {
            v1=true;
            Customer_phone_log.setVisibility(View.GONE);
        }
        if(Customer_address.getText().toString().trim().isEmpty())
        {
            v2=false;
            Customer_address_log.setVisibility(View.VISIBLE);
        }
        else
        {
            v2=true;
            Customer_address_log.setVisibility(View.GONE);
        }
        if(Customer_name.getText().toString().trim().isEmpty())
        {
            v3=false;
            Customer_name_log.setVisibility(View.VISIBLE);
        }
        else
        {
            v3=true;
            Customer_name_log.setVisibility(View.GONE);
        }
        if(branch_address.getText().toString().trim().isEmpty())
        {
            v4=false;
            branch_address_log.setVisibility(View.VISIBLE);
        }
        else
        {
            v4=true;
            branch_address_log.setVisibility(View.GONE);
        }
        if(v1&&v2&&v3&&v4)
        {
            Customer customer=new Customer(Customer_name.getText().toString(),
                    Customer_address.getText().toString(),
                    Customer_phone.getText().toString()
                    , branch_address.getText().toString());
            if(update)
                mDatabaseReference.child(key).setValue(customer);
            else
                mDatabaseReference.push().setValue(customer);
            Customer_name.getText().clear();;
            Customer_address.getText().clear();
            branch_address.getText().clear();
            Customer_phone.getText().clear();;
            Toast toast=Toast.makeText(this,"العمليه تمت بنجاح",Toast.LENGTH_LONG);
            toast.show();

        }
    }
    public void  delete (View view){
        mDatabaseReference.child(key).removeValue();
        Customer_name.getText().clear();;
        Customer_address.getText().clear();
        branch_address.getText().clear();
        Customer_phone.getText().clear();;
        Toast toast=Toast.makeText(this,"العمليه تمت بنجاح",Toast.LENGTH_LONG);
        toast.show();
    }
    public void cancel_login3(View view) {
        this.finish();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt("Customer_phone_log",((Customer_phone_log.getVisibility()==View.VISIBLE)?1:0));
        outState.putInt("Customer_address_log",((Customer_address_log.getVisibility()==View.VISIBLE)?1:0));
        outState.putInt("Customer_name_log",((Customer_name_log.getVisibility()==View.VISIBLE)?1:0));
        outState.putInt("branch_address_log",((branch_address_log.getVisibility()==View.VISIBLE)?1:0));
        outState.putInt("butt_delete",((delete.getVisibility()==View.VISIBLE)?1:0));
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Customer_phone_log.setVisibility(((savedInstanceState.getInt("Customer_phone_log", 0) == 1)?View.VISIBLE:View.GONE));
        Customer_address_log.setVisibility(((savedInstanceState.getInt("Customer_address_log", 0) == 1)?View.VISIBLE:View.GONE));
        Customer_name_log.setVisibility(((savedInstanceState.getInt("Customer_name_log", 0) == 1)?View.VISIBLE:View.GONE));
        branch_address_log.setVisibility(((savedInstanceState.getInt("branch_address_log", 0) == 1)?View.VISIBLE:View.GONE));
        delete.setVisibility(((savedInstanceState.getInt("butt_delete", 0) == 1)?View.VISIBLE:View.GONE));
    }
}
