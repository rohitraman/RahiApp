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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegisterActivity extends AppCompatActivity {

    private EditText regEmail;
    private EditText regPass;
    private Button createAccount;
    private Button backToLoginBtn;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regEmail = (EditText)findViewById(R.id.register_email);
        regPass = (EditText)findViewById(R.id.register_password);
        createAccount = (Button)findViewById(R.id.register_button);
        backToLoginBtn = (Button)findViewById(R.id.register_to_login_btn);

        mAuth = FirebaseAuth.getInstance();

        backToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RegisterActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        mProgress = new ProgressDialog(RegisterActivity.this);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if(!TextUtils.isEmpty(regEmail.getText().toString()) && !TextUtils.isEmpty(regPass.getText().toString())){

                    mProgress.setTitle("Registering...");
                    mProgress.setMessage("Please wait while we register...");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();

                    mAuth.createUserWithEmailAndPassword(regEmail.getText().toString(), regPass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){

                                        mProgress.dismiss();
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();

                                    }
                                    else {

                                        mProgress.dismiss();
                                        try {
                                            throw task.getException();
                                        }
                                        catch(FirebaseAuthWeakPasswordException e) {
                                            Snackbar.make(view, "Weak Password!", Snackbar.LENGTH_SHORT).show();
                                        } catch(FirebaseAuthInvalidCredentialsException e) {
                                            Snackbar.make(view, "Invalid E-mail or Password", Snackbar.LENGTH_SHORT).show();
                                        } catch(FirebaseAuthUserCollisionException e) {
                                            Snackbar.make(view, "User With This E-mail Already Exists!", Snackbar.LENGTH_SHORT).show();
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