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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonLogIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            // start profile activity
            finish();
            startActivity( new Intent(getApplicationContext(), UserProfileInfoActivity.class));

        }

        buttonLogIn = (Button) findViewById(R.id.buttonLogIn);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);

        buttonLogIn.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

    }

    private void userLogIn(){
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

        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){

                        if(task.isSuccessful()){
                            //user is regestered
                            progressDialog.dismiss();
                            //Toast.makeText(getApplicationContext(),"Logged In successfully!",Toast.LENGTH_SHORT).show();
                            //finish current activity
                            finish();
                            //startActivity( new Intent(getApplicationContext(), UserProfileInfoActivity.class));
                            //startActivity( new Intent(getApplicationContext(), UserAccountActivity.class));
                            startActivity( new Intent(getApplicationContext(), ProfileMenu.class));
                             return;
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Log in failed! \n" + task.getException().getMessage() ,Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                });


    }
    @Override
    public void onClick(View v) {

        if(v == buttonLogIn){
            //userLogin
            userLogIn();
        }

        if (v == textViewSignUp ){
            //close current activity
            finish();
            //start new activity
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
