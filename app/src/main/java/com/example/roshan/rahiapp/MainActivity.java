package com.example.roshan.rahiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button LogoutBtn;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogoutBtn = (Button)findViewById(R.id.logoutBtn);

        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(MainActivity.this);

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgress.setTitle("Logging Out...");
                mProgress.setMessage("Please wait while we logout...");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();

                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

                mProgress.dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            sendToLogin();
        }
    }

    private void sendToLogin()
    {
        Intent login_intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(login_intent);
        finish();
    }
}