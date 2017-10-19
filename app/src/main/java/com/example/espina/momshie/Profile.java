package com.example.espina.momshie;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Welcome.Welcome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity {

    //DECLARE FIELDS

    EditText name,username, phone, age, birth;
    Button saveBtn;

    //FIREBASE REF
    DatabaseReference mDataRef, userRef;
    



    //STRING REF






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //FIREBASE DATABASE REF
        userRef = FirebaseDatabase.getInstance().getReference("Users");








        //ASSIGN ID's
        saveBtn = (Button) findViewById(R.id.profileBtn);
        name = (EditText) findViewById(R.id.profileName);
        username = (EditText) findViewById(R.id.profileUsername);
        age = (EditText) findViewById(R.id.profileAge);
        phone = (EditText) findViewById(R.id.profilePhone);
        birth = (EditText) findViewById(R.id.profileBirth);





        // SAVE BUTTON LOGIC

        saveBtn.setOnClickListener(new View.OnClickListener() {
           // @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {

                addUser();
            }
        });
    }

    private void addUser(){





        String userNameString = name.getText().toString();
        String userPhoneString  = phone.getText().toString();
        String userAgeString  = age.getText().toString();
        String userBirthString = birth.getText().toString();
        String userUserNameString = username.getText().toString();

        //GET USER KEY  FROM INTENT
        String userKey = getIntent().getStringExtra("USER_KEY");
        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        String userPass = getIntent().getStringExtra("USER_PASS");
        String userID = getIntent().getStringExtra("USER_ID");

        mDataRef = userRef.child(userKey);





        if (!TextUtils.isEmpty(userNameString) && !TextUtils.isEmpty(userPhoneString) && !TextUtils.isEmpty(userAgeString) && !TextUtils.isEmpty(userBirthString) && !TextUtils.isEmpty(userUserNameString)) {


            User user = new User(userID, userKey, userNameString, userPhoneString, userAgeString, userBirthString, userUserNameString);

            userRef.child(userKey).setValue(user);
            mDataRef.child("emailUser").setValue(userEmail);
            mDataRef.child("passWordUser").setValue(userPass);
            mDataRef.child("userID").setValue(userID);


            Toast.makeText(Profile.this, "Profile Details Saved!", Toast.LENGTH_LONG).show();


            Intent myIntent  = new Intent(Profile.this, Welcome.class);
            myIntent.putExtra("USER_KEY", userKey);

            Profile.this.startActivity(myIntent);


        } else {
            Toast.makeText(Profile.this, "Please Enter Correct Profile Details!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Profile.this, Profile.class));

        }
    }

}
