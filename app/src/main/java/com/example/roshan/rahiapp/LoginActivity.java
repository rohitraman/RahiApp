package com.example.roshan.rahiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private EditText logEmail;
    private EditText logPass;
    private Button signInAccount;
    private Button backToRegBtn;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logEmail = (EditText)findViewById(R.id.login_email);
        logPass = (EditText)findViewById(R.id.login_password);
        signInAccount = (Button)findViewById(R.id.login_button);
        backToRegBtn = (Button)findViewById(R.id.login_to_register_btn);

        mAuth = FirebaseAuth.getInstance();

        backToRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        mProgress = new ProgressDialog(LoginActivity.this);

        signInAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if(!TextUtils.isEmpty(logEmail.getText().toString()) && !TextUtils.isEmpty(logPass.getText().toString()))
                {
                    mProgress.setTitle("Logging In...");
                    mProgress.setMessage("Please wait while we login...");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();

                    mAuth.signInWithEmailAndPassword(logEmail.getText().toString(), logPass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful())
                                    {
                                        mProgress.dismiss();
                                        startActivity(new Intent(LoginActivity.this,MainActivity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();
                                    }
                                    else {
                                        mProgress.dismiss();
                                        try {
                                            throw task.getException();
                                        }
                                        catch(FirebaseAuthInvalidUserException e) {
                                            Snackbar.make(view, "User Not Found, Create New Account!", Snackbar.LENGTH_SHORT).show();
                                        } catch(FirebaseAuthInvalidCredentialsException e) {
                                            Snackbar.make(view, "Invalid Password!", Snackbar.LENGTH_SHORT).show();
                                        } catch(FirebaseNetworkException e) {
                                            Snackbar.make(view, "Please Check Your Internet Connection!", Snackbar.LENGTH_SHORT).show();
                                        } catch(Exception e) {
                                            Snackbar.make(view, "Error: "+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
                else {

                    Snackbar.make(view, "Please Enter Credentials", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}