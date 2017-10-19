package com.Welcome;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.espina.momshie.MainActivity;
import com.example.espina.momshie.R;
import com.example.espina.momshie.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.loginpack.LoginActivity;

import java.util.ArrayList;
import java.util.Iterator;

public class Welcome extends AppCompatActivity {


    private static final String TAG = "ViewDatabase";
    //ADD FIREBASE STUFF



    //DECLARE FIELDS
    Button outBtn;
    TextView welcome;
    private DatabaseReference myRef, mDataRef, userRef;
    private FirebaseDatabase mFirebaseDatabase;
    private String userIDPassed;
    private String userID;
    private String userKey;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;




    private ListView mListView;

    //FIREBASE AUTH FIELDS
    private FirebaseAuth nAuth;
    private FirebaseAuth.AuthStateListener nAuthlistener;



    //GET USER KEY  FROM INTENT


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        //DRAWER LAYOUT
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);


        //ASSIGN IDS
        outBtn = (Button) findViewById(R.id.logoutBtn);
        welcome = (TextView) findViewById(R.id.WelcomeName);
        mListView = (ListView) findViewById(R.id.listview);


        //ASSIGN INSTANCE
        myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        nAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        userKey = getIntent().getStringExtra("USER_KEY");
        FirebaseUser User = nAuth.getCurrentUser();
        userID = User.getUid();


        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        nAuthlistener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser User = firebaseAuth.getCurrentUser();

                if (User != null){


                    Log.d(TAG, "onAuthStateChanged:signed_in:" + User.getUid());
                    Toast.makeText(Welcome.this, "Successfully signed in with: " + User.getEmail(), Toast.LENGTH_LONG).show();


                }else{

                    Log.d(TAG, "onAuthStateChanged:signed_out" + userID);
                    Toast.makeText(Welcome.this, "Successfully signed out.", Toast.LENGTH_LONG).show();
                }


            }

        };




        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange (DataSnapshot dataSnapshot){
                        showData(dataSnapshot);
                    }

                    @Override
                    public void onCancelled (DatabaseError databaseError){

                    }

        });



        outBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nAuth.signOut();
                finish();
                startActivity(new Intent(Welcome.this, MainActivity.class));
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {

        //GET USER KEY  FROM INTENT

        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        String  userPhone = getIntent().getStringExtra("USER_PHONE");
        String userBirth = getIntent().getStringExtra("USER_BIRTH");
        String  userUserName = getIntent().getStringExtra("USER_USERNAME");
        String userName = getIntent().getStringExtra("USER_NAME");
        String userAge = getIntent().getStringExtra("USER_AGE");


        mDataRef = userRef.child(userKey);


        if (!TextUtils.isEmpty(userKey) && !TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userID) && !TextUtils.isEmpty(userBirth) && !TextUtils.isEmpty(userUserName)) {




            for(DataSnapshot ds : dataSnapshot.getChildren()){

                Log.d(TAG, "showData: snapshot: " + ds);
                Log.d(TAG, "showData: snapshot: " + ds.child("Users"));


                //display all info taken


                Log.d(TAG, "showData: userName: " + userName);
                Log.d(TAG, "showData: userAge: " + userAge);
                Log.d(TAG, "showData: userBirth: " + userBirth);
                Log.d(TAG, "showData: userPhone: " + userPhone);
                Log.d(TAG, "showData: userUserName: " + userUserName);

                ArrayList<String> array = new ArrayList<>();
                array.add(userName);
                array.add(userAge);
                array.add(userBirth);
                array.add(userPhone);
                array.add(userUserName);
                ArrayAdapter adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,array);
                mListView.setAdapter(adapter);

            }


        } else {


        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        nAuth.addAuthStateListener(nAuthlistener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        nAuth.removeAuthStateListener(nAuthlistener);
    }





    //FOR NAVIGATION DRAWER


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    //Navigation Drawer End

}
