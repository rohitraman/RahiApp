package com.example.roshan.rahiapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etPin,etEmail;
    Button btnChangePass;
    DatabaseReference reference;
    ValueEventListener listener;
    String encryptedValue;
    TextView textView;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        etPin = (EditText) findViewById(R.id.et_forgot_pass);
        etEmail = (EditText)findViewById(R.id.et_email_id);
        btnChangePass = (Button) findViewById(R.id.btn_forgot_pass);
        reference = FirebaseDatabase.getInstance().getReference();
        textView = (TextView)findViewById(R.id.tv_forgot_pin);
        session = new Session(this);
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                try {
                    encryptedValue = AESEncrypt.encrypt(etPin.getText().toString());
                    Log.i("yeah",encryptedValue);
                    listener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot:dataSnapshot.getChildren())
                            {
                                User user = snapshot.getValue(User.class);
                                if (user != null) {
                                    Log.i("yeah",user.getEmail());
                                    if (user.getEmail().equals(etEmail.getText().toString())) {
                                        if (user.getPin().equals(encryptedValue)) {
                                                session.setLoggedIn(true);
                                                startActivity(new Intent(ForgotPasswordActivity.this,MainActivity.class));
                                        }

                                        else
                                        {
                                            Snackbar.make(view, "Wrong Pin!Try Again", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    reference.addValueEventListener(listener);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordActivity.this,ChangePasswordActivity.class);
                startActivity(intent);

            }
        });


    }
}
