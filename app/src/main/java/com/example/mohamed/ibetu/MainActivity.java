package com.example.mohamed.ibetu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    public int Totalusers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        //if user is already logged in, go to profile
        if(firebaseAuth.getCurrentUser() != null){
            // start profile activity
            finish();
            //startActivity( new Intent(getApplicationContext(), UserAccountActivity.class));
            startActivity( new Intent(getApplicationContext(), ProfileMenu.class));

        }

        //initialize
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        progressDialog = new ProgressDialog(this);



        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);

    }

    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password= editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter email!", Toast.LENGTH_SHORT).show();
            //stop execution
            return;
        }

        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Please enter password!", Toast.LENGTH_SHORT).show();
            //stop execution
            return;
        }

        progressDialog.setMessage("Registering...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){

                        if(task.isSuccessful()){
                            Totalusers++;
                            //user is regestered
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,"Registered successfully",Toast.LENGTH_SHORT).show();
                            finish();
                            //update profile information
                            startActivity( new Intent(getApplicationContext(), UserProfileInfoActivity.class));

                            return;
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,"Registration failed! \n" + task.getException().getMessage() ,Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                });


    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegister){
            registerUser();
        }

        if(v == textViewSignin){
            //close current activity
            finish();
            //start new activity, go to login page
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
