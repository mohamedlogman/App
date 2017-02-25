package com.example.mohamed.ibetu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import static java.security.AccessController.getContext;

public class ProfileMenu extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    //ArrayAdapter<String> listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



        //not needed
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_menu, menu);

        //search menu
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_friends_menu,menu);
        MenuItem item = menu.findItem(R.id.listViewFriends);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String queryn){
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                listViewAdapter.getFilter().filter(newText);
                return false;
            }
        });*/


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {

            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                final View rootView = inflater.inflate(R.layout.fragment_profile_menu, container, false);
                //View rootView = inflater.inflate(R.layout.fragment_profile_menu, container, false);
                //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));


                /////////////////////////////////////////
                final FirebaseDatabase firebaseDatabase;
                final DatabaseReference databaseReference;
                final FirebaseAuth firebaseAuth;
                final FirebaseUser user;

                TextView textViewUserEmail;
                final TextView textViewUserName;
                final TextView textViewFullName;
                final Button buttonLogOut;
                Button buttonAddFriend;
                final EditText editTextAddFriend;


                //authentication instance
                firebaseAuth = FirebaseAuth.getInstance();
                //get data base instance
                firebaseDatabase = FirebaseDatabase.getInstance();
                //refrence to top level of database
                databaseReference =  firebaseDatabase.getReference("users");

                //if i has a child, needed for accounts i think
                // databaseRefrence.child("xxxx").addListenerForSingleValueEvent();
                //databaseRefrence.child("xxxx").addListenerForSingleValueEvent();

                textViewFullName = (TextView) rootView.findViewById(R.id.textViewFullName);
                textViewUserName = (TextView) rootView.findViewById(R.id.textViewUserName);
                textViewUserEmail = (TextView) rootView.findViewById(R.id.textViewUserEmail);
                buttonLogOut = (Button) rootView.findViewById(R.id.buttonLogOut);
                buttonAddFriend  = (Button) rootView.findViewById(R.id.buttonAddFriend);
                editTextAddFriend = (EditText) rootView.findViewById(R.id.editTextAddFriend);

                user = firebaseAuth.getCurrentUser();
                System.out.println("duuuuuuuuuuuuuuuude emai:" + user.getEmail());
                textViewUserEmail.setText("Welcome: " + user.getEmail());


                //databaseRefrence.child(user.getUid()).addChildEventListener(new ChildEventListener() {
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    //this method will  be invoked anytime the data on the db changes

                    public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                        ProfileInformation userProfileValue = dataSnapshot.getValue(ProfileInformation.class);
                        System.out.println("The " + dataSnapshot.getKey() + " score is " + dataSnapshot.getValue());
                        System.out.println("testing:" + userProfileValue  + ":username:" + userProfileValue.userName  + " :Fullname:" + userProfileValue.fullName );
                        //if(dataSnapshot.getKey().equals("fullName")) {
                        if (dataSnapshot.getKey().equals(user.getUid())) {
                            textViewFullName.setText("Full name: " + userProfileValue.fullName);
                            //}
                            //if(dataSnapshot.getKey().equals("userName")) {
                            textViewUserName.setText("User name: " + userProfileValue.userName);
                            // }
                        };

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

                buttonAddFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String searcheEmail = editTextAddFriend.getText().toString().trim();
                        //databaseRefrence.orderByChild("userEmail").addChildEventListener(new ChildEventListener() {
                        //final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        //progressDialog.setMessage("Adding User...");

                        databaseReference.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                ProfileInformation userProfileValue = dataSnapshot.getValue(ProfileInformation.class);
                                System.out.println("queryKey:" + dataSnapshot.getKey() + " QueryValue " + dataSnapshot.getValue() + "  QueryParent ");
                                System.out.println("query:" + "userid:" + user.getUid()+ " Userprofiele:" +  userProfileValue  + ":username:" + userProfileValue.userName  + " :Fullname:" + userProfileValue.fullName + " :email:" + userProfileValue.userEmail);
                                System.out.println("The search email:" + searcheEmail + "ChildCount:" + dataSnapshot.getChildrenCount ());
                                //progressDialog.show();
                                if(searcheEmail.equals(userProfileValue.userEmail)){
                                    if(dataSnapshot.getKey().equals(user.getUid())){
                                        System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiEMAIL MATCH:" + searcheEmail);

                                        Toast.makeText(getContext(),"You cannot friend your self. Choose another user." ,Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getContext(),"User found." ,Toast.LENGTH_SHORT).show();
                                        Map<String, Object> Updates = new HashMap<String, Object>();
                                        Updates.put(user.getUid()+"/"+dataSnapshot.getKey(),userProfileValue.userName);
                                        System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeEMAIL MATCH:" + searcheEmail);
                                        firebaseDatabase.getReference("friends").updateChildren(Updates);
                                    }

                                }
                                //do toast to show that user is not found
                                /*else{
                                    if(!userFound[0]) {
                                        Toast.makeText(getContext(), "User not found. Try a diffrent email.", Toast.LENGTH_SHORT).show();
                                    }
                                    //progressDialog.dismiss();

                                }*/
                               // progressDialog.dismiss();
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

                            }

                        }
                        );

                       /* if (!userFound[0]) {
                            Toast.makeText(getContext(),"User not found. Try a diffrent email." ,Toast.LENGTH_SHORT).show();

                            //progressDialog.dismiss();
                            return;
                        }*/

                        System.out.println("ooooooooooooooooooooooooooooouuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuttttttttttttttttttttttttt");



                        //finish();
                        //start login activity
                        //startActivity( new Intent(getContext(), LoginActivity.class));
                    }
                });

                //add listener to button
                //buttonLogOut.setOnClickListener(this);
                buttonLogOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //logout of account
                        firebaseAuth.signOut();
                        //close current activity
                        //finish();
                        //start login activity
                        startActivity( new Intent(getContext(), LoginActivity.class));
                    }
                });


                ////////////////////////////////////////

                return rootView;
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                View rootView = inflater.inflate(R.layout.fragment_friends, container, false);


                final FirebaseDatabase firebaseDatabase;
                DatabaseReference databaseReference;
                final FirebaseAuth firebaseAuth;
                final FirebaseUser user;

                //authentication instance
                firebaseAuth = FirebaseAuth.getInstance();
                //get data base instance
                firebaseDatabase = FirebaseDatabase.getInstance();
                //refrence to top level of database
                databaseReference =  firebaseDatabase.getReference("friends");

                ListView listViewFriends = (ListView) rootView.findViewById(R.id.listViewFriends);
                final ArrayList<String> friendsNames = new ArrayList<>();
                //String[] friends = {"MoLogman", "AliBaker" , "MattJ" , "Bobthebuilder" , "SSGoku" , "FANboy247",
                //        "winner365", "Loser365", "JonFarmer" , "Fordtam", "BlueManGroup"};
                final ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                        //current activity
                        getActivity(),
                        //layout used to create views that list view will show
                        android.R.layout.simple_list_item_1,
                        //array of strings
                        friendsNames
                );

                listViewFriends.setAdapter(listViewAdapter);
                //get user profile
                user = firebaseAuth.getCurrentUser();

                databaseReference.child(user.getUid()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        System.out.println("friendlist " + dataSnapshot.getKey() + " friend is " + dataSnapshot.getValue());
                        //FriendListInformation friendsList = dataSnapshot.getValue(FriendListInformation.class);
                        //System.out.println("friendlistVALUE username: " + friendsList.userName);

                        /////value is not showing you are here

                        String username = dataSnapshot.getValue(String.class);
                        // only set information for logged in user
                        //if (dataSnapshot.getKey().equals(user.getUid())) {
                            //do not display current user's user name. friend list created current user information
                            if(!dataSnapshot.getKey().equals(user.getUid())) {
                                friendsNames.add(username);
                            }
                            //friendsNames.sort(String.class);
                            Collections.sort(friendsNames);
                            listViewAdapter.notifyDataSetChanged();
                       // /}


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

                    }
                });

                /////// Select value in list
                listViewFriends.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                //list.setItemChecked(0, true);

                listViewFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        Toast.makeText(getContext(), "position:" + position + "  id:" + id  + " Name:" + friendsNames.get(position),Toast.LENGTH_LONG).show();

                        Log.d("#################","position:" + position + "  id:" + id);
                        /*if(id ==0 ){
                            getActivity().finish();
                            //start new activity
                            startActivity(new Intent(getContext(),Arsenalgame.class));
                        }*/

                        final String[] friendId = new String[1];
                        final String[] userName = new String[1];
                        //get clicked user id from username
                        firebaseDatabase.getReference("friends").child(user.getUid()).addChildEventListener(new ChildEventListener() {


                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                System.out.println("friendlist-finder: " + dataSnapshot.getKey() + " friend is-finder: " + dataSnapshot.getValue());
                                //FriendListInformation friendsList = dataSnapshot.getValue(FriendListInformation.class);
                                //System.out.println("friendlistVALUE username: " + friendsList.userName);

                                /////value is not showing you are here
                                // only set information for logged in user
                                //if (dataSnapshot.getKey().equals(user.getUid())) {
                                //do not display current user's user name. friend list created current user information
                                if (dataSnapshot.getValue().equals(friendsNames.get(position))) {
                                    friendId[0] = dataSnapshot.getKey();

                                }

                                if (dataSnapshot.getKey().equals(user.getUid())) {
                                    userName[0] = dataSnapshot.getValue(String.class);
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

                            }
                        });

                        ///////////////////






                        //Get Bet Name////////
                        final String[] betName = new String[1];
                        final EditText enteredBetName = new EditText(getContext());

                        AlertDialog.Builder betBuilde = new AlertDialog.Builder(getContext());
                        betBuilde.setTitle("Enter Bet name:");

                        betBuilde.setView(enteredBetName);
                        betBuilde.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                betName[0] = enteredBetName.getText().toString();

                                //set message content for both users
                                DatabaseReference betsRef = firebaseDatabase.getReference("bets");
                                Map<String,Object> newBetmap = new HashMap<String,Object>();
                                newBetmap.put(betName[0],"");
                                betsRef.child(user.getUid()).updateChildren(newBetmap);
                                betsRef.child(friendId[0]).updateChildren(newBetmap);

                                Intent intent = new Intent(getContext(),Messaging.class);
                                intent.putExtra("senderId",user.getUid());
                                intent.putExtra("receiverId",friendId[0]);
                                intent.putExtra("rUserName",friendsNames.get(position));
                                intent.putExtra("sUserName",userName[0]);
                                intent.putExtra("betName",betName[0]);
                                startActivity(intent);

                            }
                        });

                        betBuilde.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        betBuilde.show();








                        
                    }
                });


                return rootView;
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                View rootView = inflater.inflate(R.layout.fragment_bets, container, false);

                /*ListView listViewBets = (ListView) rootView.findViewById(R.id.listViewBets);

                final String[] bets = {"Arsenal Game", "3pt contest" , "Super Bowl" , "Lift More" , "Highest Score", "Fastest"};
                ArrayAdapter<String> listViewAdapterBets = new ArrayAdapter<String>(
                        //current activity
                        getActivity(),
                        //layout used to create views that list view will show
                        android.R.layout.simple_list_item_1,
                        //array of strings
                        bets
                );

                listViewBets.setAdapter(listViewAdapterBets);

                /////// Select value in list
                listViewBets.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                //list.setItemChecked(0, true);
                listViewBets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getContext(), "position:" + position + "  id:" + id  + " Name:" + bets[position],Toast.LENGTH_LONG).show();

                        Log.d("#################","position:" + position + "  id:" + id);
                        if(id ==0 ){
                            getActivity().finish();
                            //start new activity
                            startActivity(new Intent(getContext(),Arsenalgame.class));
                        }
                    }
                });
                //////////
                */

                final FirebaseDatabase firebaseDatabase;
                DatabaseReference databaseRefrence;
                final FirebaseAuth firebaseAuth;
                final FirebaseUser user;

                //authentication instance
                firebaseAuth = FirebaseAuth.getInstance();
                //get data base instance
                firebaseDatabase = FirebaseDatabase.getInstance();
                //refrence to top level of database
                databaseRefrence =  firebaseDatabase.getReference("bets");

                ListView listViewBets = (ListView) rootView.findViewById(R.id.listViewBets);
                final ArrayList<String> bets = new ArrayList<>();
                final ArrayAdapter<String> betslistViewAdapter = new ArrayAdapter<String>(
                        //current activity
                        getActivity(),
                        //layout used to create views that list view will show
                        android.R.layout.simple_list_item_1,
                        //array of strings
                        bets
                );

                listViewBets.setAdapter(betslistViewAdapter);




                //get list of bets
                user = firebaseAuth.getCurrentUser();

                databaseRefrence.child(user.getUid()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        System.out.println("betlist " + dataSnapshot.getKey() + " bet is " + dataSnapshot.getValue());

                        String betName = dataSnapshot.getKey();
                        bets.add(betName);
                        Collections.sort(bets);
                        betslistViewAdapter.notifyDataSetChanged();
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

                    }
                });

                //get list of bets participants
                String friendId = new String();
                final String[] userName = new String[1];
                final String[] betName = new String[1];

                final String[] name1 = new String[1];
                final String[] name2 = new String[1];
                final String[] msg = new String[1];

                listViewBets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        Toast.makeText(getContext(), "position:" + position + "  id:" + id + " Name:" + bets.get(position), Toast.LENGTH_LONG).show();

                         betName[0] = bets.get(position);
                        ////get user names from messages
                        firebaseDatabase.getReference("bets").child(user.getUid()).child(bets.get(position)).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                System.out.println("bet-finder: " + dataSnapshot.getKey() + " bets is-finder: " + dataSnapshot.getValue());
                                Iterator i = dataSnapshot.getChildren().iterator();
                                //skip the message
                                msg[0] = (String)((DataSnapshot)i.next()).getValue();
                                name1[0] = (String)((DataSnapshot)i.next()).getValue();
                                name2[0] = (String)((DataSnapshot)i.next()).getValue();
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

                            }
                        });
                    }
                });


                System.out.println("ggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg1111111" );
                final String[] temp1ID = {""};
                final String[] temp2ID = {""};
                String friendsName = "";

                firebaseDatabase.getReference("friends").child(user.getUid()).addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        System.out.println("friendlist-finder: " + dataSnapshot.getKey() + " friend is-finder: " + dataSnapshot.getValue());
                        if (dataSnapshot.getValue().equals(name1[0])) {
                            temp1ID[0] = dataSnapshot.getKey();
                        }

                        if (dataSnapshot.getValue().equals(name2[0])) {
                            temp2ID[0] = dataSnapshot.getKey();
                        }

                        if (dataSnapshot.getKey().equals(user.getUid())) {
                            userName[0] = dataSnapshot.getValue(String.class);
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

                    }
                });

                System.out.println("name1[0]"=
                System.out.println("ggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg" );

                //get user names
                if(name1[0].equals(userName[0])){
                    friendsName = name2[0];
                    friendId = temp2ID[0];
                }else{
                    friendsName = name1[0];
                    friendId = temp1ID[0];
                }

                /*Intent intent = new Intent(getContext(),Messaging.class);
                intent.putExtra("senderId",user.getUid());
                intent.putExtra("receiverId",friendId);
                intent.putExtra("rUserName",friendsName);
                intent.putExtra("sUserName",userName[0]);
                intent.putExtra("betName",betName[0]);
                startActivity(intent);*/


                return rootView;
            }else{
                View rootView = inflater.inflate(R.layout.fragment_profile_menu, container, false);
                //View rootView = inflater.inflate(R.layout.fragment_profile_menu, container, false);
                //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                return rootView;
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "User Profile";
                case 1:
                    return "Friends";
                case 2:
                    return "Bets";
            }
            return null;
        }
    }
}
