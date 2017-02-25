package com.example.mohamed.ibetu;

//

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

public class UserProfileInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button buttonLogOut;

    //data base
    private DatabaseReference databaseRefrence;
    private DatabaseReference userProfileRef;
    private EditText editTextFullName;
    private EditText editTextUserName;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        //Initialize database views
        databaseRefrence = FirebaseDatabase.getInstance().getReference();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            ///////////////WHY THIS VS getApplicationContext()
            startActivity(new Intent(this, LoginActivity.class));
        }

        //create a child refrence users -- to add if u want instance of all users xxx
        userProfileRef = databaseRefrence.child("users");

        //input content
        editTextFullName = (EditText) findViewById(R.id.editTextFullName);
        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        buttonSave = (Button) findViewById(R.id.buttonSave);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initialize logon views
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);

        //display logged in user email
        textViewUserEmail.setText("Welcome " + user.getEmail());

        //add listener to button
        buttonLogOut.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }

    private void saveProfileInfo(){
        String fullName = editTextFullName.getText().toString().trim();
        String userName = editTextUserName.getText().toString().trim();


        if(TextUtils.isEmpty(fullName)){
            //email is empty
            Toast.makeText(this, "Please enter Full Name!", Toast.LENGTH_SHORT).show();
            //stop execution
            return;
        }

        if(TextUtils.isEmpty(userName)){
            //password is empty
            Toast.makeText(this, "Please enter User Name!", Toast.LENGTH_SHORT).show();
            //stop execution
            return;
        }

        //get logged in user id
        FirebaseUser user = firebaseAuth.getCurrentUser();
        ProfileInformation profileInformation = new ProfileInformation(fullName,userName,user.getEmail());

        //based on current user uniqe id create a node inside firebase, and store info in node
        //save profile info to database under users
        userProfileRef.child(user.getUid()).setValue(profileInformation);

        //create users friend list
        Map<String, Object> userFriend = new HashMap<String, Object>();
        userFriend.put(user.getUid(),profileInformation.userName);
        databaseRefrence.child("friends").child(user.getUid()).setValue(userFriend);

        Toast.makeText(this,"Information Saved...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        //if logout button is clicked
        if (v ==buttonLogOut){
            //logout of account
            firebaseAuth.signOut();
            //close current activity
            finish();
            //start login activity
            startActivity( new Intent(this, LoginActivity.class));
        }

        if(v == buttonSave){
            saveProfileInfo();
            //close current activity
            finish();
            //start login activity
            startActivity( new Intent(this, ProfileMenu.class));

        }
    }
}
