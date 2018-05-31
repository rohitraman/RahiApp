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
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegisterActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;

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
                                            Snackbar.make(view, "Weak Password!", Snackbar.LENGTH_LONG).show();
                                        } catch(FirebaseAuthInvalidCredentialsException e) {
                                            Snackbar.make(view, "Invalid E-mail or Password", Snackbar.LENGTH_LONG).show();
                                        } catch(FirebaseAuthUserCollisionException e) {
                                            Snackbar.make(view, "User With This E-mail Already Exists!", Snackbar.LENGTH_LONG).show();
                                        } catch(FirebaseNetworkException e) {
                                            Snackbar.make(view, "Please Make Sure You Are Connected With Internet!", Snackbar.LENGTH_LONG).show();
                                        } catch(Exception e) {
                                            Snackbar.make(view, "Error: "+e.getMessage(), Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });
                }
                else {

                    Snackbar.make(view, "Please Enter Credentials", Snackbar.LENGTH_LONG).show();
                }
            }
        });




        //************************************GOOGLE SIGN IN************************************//

        SignInButton mGoogleBtn = (SignInButton) findViewById(R.id.googleBtn);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(RegisterActivity.this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(RegisterActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setTitle("Logging In...");
                mProgress.setMessage("Please wait while we login...");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();
                signIn();
            }
        });
    }




    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }else {
                mProgress.dismiss();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            mProgress.dismiss();
                            startActivity(new Intent(RegisterActivity.this,MainActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        } else {
                            mProgress.dismiss();
                            Toast.makeText(RegisterActivity.this, "There is some problem!", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}