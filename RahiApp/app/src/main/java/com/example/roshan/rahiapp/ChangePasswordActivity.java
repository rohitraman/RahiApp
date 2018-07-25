package com.example.roshan.rahiapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText etEmail;
    Button button;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        etEmail = (EditText)findViewById(R.id.et_email);
        button = (Button)findViewById(R.id.btn_send_verification);
        mAuth = FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(etEmail.getText().toString()))
                {
                    Snackbar.make(view,"Email Empty!", Snackbar.LENGTH_SHORT).show();
                }

                else
                {
                    String email = etEmail.getText().toString();
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ChangePasswordActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ChangePasswordActivity.this,LoginActivity.class));
                            }

                            else
                            {
                                Toast.makeText(ChangePasswordActivity.this, "Email not sent!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
