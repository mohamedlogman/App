package com.example.mohamed.ibetu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class Messaging extends AppCompatActivity {
    //private ListView listViewMessages;
    private EditText editTextMessage;
    private TextView textViewMessages;
    private Button buttonSendMessage;
    //private ArrayList<String> bets = new ArrayList<>();
    //private ArrayAdapter<String> arrayAdapter;

    private String senderId,receiverId,rUserName,sUserName,betName;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceSender;
    private DatabaseReference databaseReferenceReceiver;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private String readMessage, readName;

    private String chatKeySender,chatKeyReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        buttonSendMessage = (Button) findViewById(R.id.buttonSendMessage);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        //listViewMessages = (ListView) findViewById(R.id.listViewMessages);
        textViewMessages = (TextView) findViewById(R.id.textViewMessages);

        senderId = getIntent().getExtras().get("senderId").toString();
        receiverId = getIntent().getExtras().get("receiverId").toString();
        rUserName = getIntent().getExtras().get("rUserName").toString();
        sUserName = getIntent().getExtras().get("sUserName").toString();
        betName = getIntent().getExtras().get("betName").toString();
        setTitle(rUserName);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceSender =  firebaseDatabase.getReference("bets").child(senderId).child(betName);
        databaseReferenceReceiver = firebaseDatabase.getReference("bets").child(receiverId).child(betName);


        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> messageMap = new HashMap<String, Object>();
                Map<String, Object> messageMapContent = new HashMap<String, Object>();
                //unique key value used for chatting
                chatKeySender = databaseReferenceSender.push().getKey();
                chatKeyReceiver = databaseReferenceReceiver.push().getKey();

                databaseReferenceSender.updateChildren(messageMap);
                databaseReferenceReceiver.updateChildren(messageMap);



                messageMapContent.put("Msg",editTextMessage.getText().toString());
                messageMapContent.put("senderName",sUserName);
                messageMapContent.put("receiverName",rUserName);

                //messageMapContent.put(sUserName,editTextMessage.getText().toString());

                databaseReferenceSender.child(chatKeySender).updateChildren(messageMapContent);
                databaseReferenceReceiver.child(chatKeyReceiver).updateChildren(messageMapContent);


            }
        });



        /*arrayAdapter = new ArrayAdapter<String>(
                //current activity
                //getActivity(),
                this,
                //layout used to create views that list view will show
                android.R.layout.simple_list_item_1,
                //array of strings
                bets
        );
        listViewMessages.setAdapter(arrayAdapter);*/


        //authentication instance
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        databaseReferenceSender.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("messagelist " + dataSnapshot.getKey() + " message is " + dataSnapshot.getValue());
                Map<String, Object> message = new HashMap<String, Object>();


                //message = dataSnapshot.getValue();
                Iterator i = dataSnapshot.getChildren().iterator();
                readMessage = (String)((DataSnapshot)i.next()).getValue();
                readName = (String)((DataSnapshot)i.next()).getValue();
                textViewMessages.append(readName+":"+readMessage+"\n");

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.println("messagelist2: " + dataSnapshot.getKey() + " message is2: " + dataSnapshot.getValue());
                Iterator i = dataSnapshot.getChildren().iterator();
                readMessage = (String)((DataSnapshot)i.next()).getValue();
                readName= (String)((DataSnapshot)i.next()).getValue();
                textViewMessages.append(readName+":"+readMessage+"\n");

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }
}
