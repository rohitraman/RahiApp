package com.example.roshan.rahiapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//Format of response
// {"boardingPoint":"CLT","bookingDate":"Jul 1, 2018 12:00:00 AM","bookingFare":540,"chartStatus":"Chart Not Prepared","dateOfJourney":"Jul 22, 2018 12:00:00 AM","destinationStation":"MAQ","generatedTimeStamp":{"day":2,"hour":14,"minute":5,"month":7,"second":4,"year":2018},"informationMessage":["",""],"isWL":"N","journeyClass":"3A","numberOfpassenger":1,"passengerList":[{"bookingBerthCode":"LB","bookingBerthNo":57,"bookingCoachId":"B1","bookingStatus":"CNF","bookingStatusDetails":"CNF\/B1\/57\/LB","bookingStatusIndex":0,"childBerthFlag":false,"concessionOpted":false,"currentBerthNo":0,"currentCoachId":"","currentStatus":"CNF","currentStatusDetails":"CNF","currentStatusIndex":0,"forGoConcessionOpted":false,"passengerCoachPosition":0,"passengerIcardFlag":false,"passengerNationality":"IN","passengerQuota":"PQ","passengerSerialNumber":1,"waitListType":0}],"pnrNumber":"4246494122","quota":"PQ","reasonType":"S","reservationUpto":"MAQ","serverId":"aposnd02:instance3","sourceStation":"CLT","ticketFare":540,"ticketTypeInPrs":"E","timeStamp":"Jul 2, 2018 2:05:04 PM","trainName":"MANGALORE MAIL","trainNumber":"12601","waitListType":0}

public class PinActivity extends AppCompatActivity {
    PinLockView lockView;
    PinLockListener listener;
    IndicatorDots dots;
    String encryptedValue;
    DatabaseReference reference;
    ValueEventListener listener1;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        reference = FirebaseDatabase.getInstance().getReference();
        listener = new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                try {
                    encryptedValue = AESEncrypt.encrypt(pin);
                    listener1 = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                if (user != null) {
                                    if (user.getEmail().split("@")[0].equals(DBUser.user.getEmail().split("@")[0])) {
                                        if (user.getPin().equals(encryptedValue)) {
                                            startActivity(new Intent(PinActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    reference.addValueEventListener(listener1);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onEmpty() {
                Log.i("empty", "Empty");
            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
                Log.i("Pin Change", intermediatePin + pinLength);
            }
        };

        dots = (IndicatorDots) findViewById(R.id.indicator);

        lockView = (PinLockView) findViewById(R.id.pin_lockview);
        lockView.attachIndicatorDots(dots);
        lockView.setPinLockListener(listener);

    }

}
