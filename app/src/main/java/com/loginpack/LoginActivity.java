package com.loginpack;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Welcome.UserInformation;
import com.Welcome.Welcome;
import com.example.espina.momshie.MainActivity;
import com.example.espina.momshie.Profile;
import com.example.espina.momshie.R;
import com.example.espina.momshie.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {

    //VIEW AND WIDGETS
    Button loginBtn;
    EditText userEmailEdit, userPasswordEdit;

    //STRING FIELDS
    String userEmailString, userPasswordString;

    //FIREBASE AUTH FIELDS
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener nAuthlistener;

    DatabaseReference mDatabaseRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //ASSIGN IDS
        loginBtn = (Button) findViewById(R.id.loginBtn);
        userEmailEdit = (EditText) findViewById(R.id.loginEditText);
        userPasswordEdit = (EditText) findViewById(R.id.loginPasswordEditText);



        //ASSIGN INSTANCE
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");


        nAuthlistener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser User = firebaseAuth.getCurrentUser();

                if (User != null){

                    final String emailForVer = User.getEmail();

                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            checkUserValidation(dataSnapshot,emailForVer);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                }else{

                }


            }


        };

        //ONCLICK LISTENER
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Perform Login Operation
               userEmailString = userEmailEdit.getText().toString().trim();
               userPasswordString = userPasswordEdit.getText().toString().trim();

                if(!TextUtils.isEmpty(userEmailString) && !TextUtils.isEmpty(userPasswordString) && userPasswordString != null && userEmailString != null)
                {
                    mAuth.signInWithEmailAndPassword(userEmailString, userPasswordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {


                                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                            checkUserValidation(dataSnapshot, userEmailString);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }else
                            {
                                Toast.makeText(LoginActivity.this, "User Login Failed", Toast.LENGTH_LONG).show();
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }

                        }
                    });
                }
            }
        });

    }

    private void checkUserValidation(DataSnapshot dataSnapshot, String emailForVer) {
        Iterator iterator  = dataSnapshot.getChildren().iterator();

        while(iterator.hasNext())
        {
            DataSnapshot dataUser  = (DataSnapshot) iterator.next();
            if(String.valueOf(dataUser.child("emailUser").getValue()).equals(emailForVer) && dataUser.child("emailUser") != null) {
                if (String.valueOf(dataUser.child("isVerified").getValue()).equals("unverified") && dataUser.child("isVerified") != null) {
                    Intent in = new Intent(LoginActivity.this, Profile.class);
                    in.putExtra("USER_KEY", String.valueOf(dataUser.child("userKey").getValue()));
                    startActivity(in);
                } else {



                    Intent inte = new Intent(LoginActivity.this, Welcome.class);
                    inte.putExtra("USER_KEY", String.valueOf(dataUser.child("userKey").getValue()));
                    inte.putExtra("USER_ID", String.valueOf(dataUser.child("userID").getValue()));
                    inte.putExtra("USER_EMAIL", String.valueOf(dataUser.child("emailUser").getValue()));
                    inte.putExtra("USER_NAME", String.valueOf(dataUser.child("userName").getValue()));
                    inte.putExtra("USER_USERNAME", String.valueOf(dataUser.child("userUserName").getValue()));
                    inte.putExtra("USER_PHONE", String.valueOf(dataUser.child("userPhone").getValue()));
                    inte.putExtra("USER_BIRTH", String.valueOf(dataUser.child("userBirth").getValue()));
                    inte.putExtra("USER_AGE", String.valueOf(dataUser.child("userAge").getValue()));
                    startActivity(inte);

                }
            }


        }



    }








    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(nAuthlistener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(nAuthlistener);
    }
}
