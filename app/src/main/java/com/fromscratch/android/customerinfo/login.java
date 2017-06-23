package com.fromscratch.android.customerinfo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class login extends Activity {

    TextView wrong;
    EditText email;
    EditText password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        wrong = (TextView) findViewById(R.id.wrong_log);

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }



    public void login_butt(View view) {


        //Log.d("ahmed", password.toString());
        if(password.getText().toString().length()<=6||email.getText().toString().length()==0)
        {
           /* Log.d("ahmed", "login_butt: ");*/
            wrong.setVisibility(View.VISIBLE);
        }
        else
        {
            mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                // Sign in success, update UI with the signed-in user's information
                                /*Log.d("ahmed", "onComplete: ");*/
                                FirebaseUser user = mAuth.getCurrentUser();
                                String id=user.getUid().toString().trim();


                            }
                            else
                            {
                                // If sign in fails, display a message to the user.
                                /*Log.d("ahmed", "onComplete:fail ");
                                Log.w("ahmed", "signInWithEmail:failure", task.getException());*/


                                wrong.setVisibility(View.VISIBLE);

                            }

                            // ...
                        }
                    });
        }
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
        if(savedInstanceState.getInt("wrongvis",0)==1)
        {
            wrong.setVisibility(View.VISIBLE);
        }
        else
        {
            wrong.setVisibility(View.GONE);
        }
    }
}
