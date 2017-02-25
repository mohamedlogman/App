package com.example.mohamed.ibetu;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseRefrence;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private TextView textViewUserEmail;
    private TextView editTextUserName;
    private TextView editTextFullName;
    private Button buttonLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        //authentication instance
        firebaseAuth = FirebaseAuth.getInstance();
        //get data base instance
        firebaseDatabase = FirebaseDatabase.getInstance();
        //refrence to top level of database
        databaseRefrence =  firebaseDatabase.getReference("users");

        //if i has a child, needed for accounts i think
       // databaseRefrence.child("xxxx").addListenerForSingleValueEvent();
        //databaseRefrence.child("xxxx").addListenerForSingleValueEvent();

        editTextFullName = (TextView) findViewById(R.id.editTextFullName);
        editTextUserName = (TextView) findViewById(R.id.editTextUserName);
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);
        //add listener to button
        buttonLogOut.setOnClickListener(this);

        user = firebaseAuth.getCurrentUser();
        textViewUserEmail.setText("Welcome: " + user.getEmail());


        //databaseRefrence.child(user.getUid()).addChildEventListener(new ChildEventListener() {
        databaseRefrence.addChildEventListener(new ChildEventListener() {
            @Override
            //this method will  be invoked anytime the data on the db changes

            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                //get all children at same level
               /* Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child: children){
                    ProfileInformation userProfileValue = dataSnapshot.getValue(ProfileInformation.class);
                    System.out.println("userProfileValueeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee:" + userProfileValue);
                }*/
                /*ProfileInformation userProfileValue = dataSnapshot.getValue(ProfileInformation.class);
                if (userProfileValue == null){
                    System.out.println("ERROR NULL");
                }
               // editTextFullName.setText(userProfileValue.fullName);
                //editTextUserName.setText(userProfileValue.userName);
                //String value = dataSnapshot.getValue(String.class);
                System.out.println("userProfileValueeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee:" + userProfileValue);
                */
                ProfileInformation userProfileValue = dataSnapshot.getValue(ProfileInformation.class);
                System.out.println("The " + dataSnapshot.getKey() + " score is " + dataSnapshot.getValue());
                System.out.println("testing:" + userProfileValue  + ":username:" + userProfileValue.userName  + " :Fullnae:" + userProfileValue.fullName);
                //if(dataSnapshot.getKey().equals("fullName")) {
                if (dataSnapshot.getKey().equals(user.getUid())) {
                    editTextFullName.setText("Full name: " + userProfileValue.fullName);
                    //}
                    //if(dataSnapshot.getKey().equals("userName")) {
                    editTextUserName.setText("User name: " + userProfileValue.userName);
                    // }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    @Override
    public void onClick(View v) {
        //if logout button is clicked
        if (v == buttonLogOut){
            //logout of account
            firebaseAuth.signOut();
            //close current activity
            finish();
            //start login activity
            startActivity( new Intent(this, LoginActivity.class));
        }
    }
}
