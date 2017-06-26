package com.fromscratch.android.customerinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class dialog extends Activity {

    TextView pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        pass = (TextView) findViewById(R.id.textView);
        Intent intent = getIntent();
        pass.append(intent.getStringExtra("pass"));
    }

    public void cancel_dialog(View view) {
        this.finish();
    }
}
