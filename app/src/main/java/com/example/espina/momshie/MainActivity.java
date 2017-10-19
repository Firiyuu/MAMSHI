package com.example.espina.momshie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Welcome.Welcome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loginpack.LoginActivity;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    //VIEW AND WIDGETS
    Button createUser, moveToLoginBtn;
    EditText userEmailEdit, userPasswordEdit;
    private static final String TAG = "EmailPassword";

    //FIREBASE AUTH FIELDS
    FirebaseAuth nAuth;
    FirebaseAuth.AuthStateListener nAuthlistener;

    DatabaseReference mDatabaseRef, mUserCheckData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //ASSIGN ID
        createUser = (Button) findViewById(R.id.createUserBtn);
        moveToLoginBtn = (Button) findViewById(R.id.moveToLogin);
        userEmailEdit = (EditText) findViewById(R.id.emailEditTextCreate);
        userPasswordEdit = (EditText) findViewById(R.id.passEditTextCreate);

        //ASSIGN INSTANCE
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mUserCheckData =  FirebaseDatabase.getInstance().getReference().child("Users");
        nAuth = FirebaseAuth.getInstance();


        nAuthlistener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    final String emailForVer = user.getEmail();
                    mUserCheckData.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            checkUserValidation(dataSnapshot, emailForVer);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {


                }


            }

        };

        //ON CLICK LISTENER

        createUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


            final String userEmailString, userPassString;
            userEmailString = userEmailEdit.getText().toString();
            userPassString = userPasswordEdit.getText().toString();

             if (!TextUtils.isEmpty(userEmailString) && !TextUtils.isEmpty(userPassString))
             {
                 nAuth.createUserWithEmailAndPassword(userEmailString,userPassString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {


                            DatabaseReference mChildDatabase = mDatabaseRef.child("Users").push();
                            String key_user = mChildDatabase.getKey();
                            String user_id = nAuth.getCurrentUser().getUid();
                            mChildDatabase.child("userID").setValue(user_id);
                            mChildDatabase.child("isVerified").setValue("unverified");
                            mChildDatabase.child("userKey").setValue(key_user);
                            mChildDatabase.child("emailUser").setValue(userEmailString);
                            mChildDatabase.child("passWordUser").setValue(userPassString);
                            Toast.makeText(MainActivity.this, "User Account Created!", Toast.LENGTH_LONG).show();



                            startActivity(new Intent(MainActivity.this, Profile.class));
                        }
                        else
                        {

                            Log.w("TAG", "createUserWithEmail:failure",task.getException());
                            //Toast.makeText(MainActivity.this, "User Account Creation Fail", Toast.LENGTH_LONG).show();
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MainActivity.this, MainActivity.class));


                        }
                     }
                 });
             }
             else{
                 if (TextUtils.isEmpty(userEmailString)) {
                     userEmailEdit.setError("Required.");

                 } else {
                     userPasswordEdit.setError(null);
                 }
             }



            }
        });

        //MOVE TO LOGIN

        moveToLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

            startActivity(new Intent(MainActivity.this, LoginActivity.class));



            }
        });

    }

    private void checkUserValidation(DataSnapshot dataSnapshot, String emailForVer) {


        Iterator iterator = dataSnapshot.getChildren().iterator();

        while (iterator.hasNext())
        {

            DataSnapshot dataUser = (DataSnapshot) iterator.next();



            if(String.valueOf(dataUser.child("emailUser").getValue()).equals(emailForVer) &&  dataUser.child("emailUser") != null)
            {

                if(String.valueOf(dataUser.child("isVerified").getValue()).equals("unverified") && dataUser.child("isVerified") != null)
                {

                    Intent in = new Intent(MainActivity.this, Profile.class);
                    in.putExtra("USER_KEY" ,  String.valueOf(dataUser.child("userKey").getValue()));
                    in.putExtra("USER_EMAIL" ,  String.valueOf(dataUser.child("emailUser").getValue()));
                    in.putExtra("USER_PASS" ,  String.valueOf(dataUser.child("passWordUser").getValue()));
                    in.putExtra("USER_ID" ,  String.valueOf(dataUser.child("userID").getValue()));


                    startActivity(in);
                    //in.putExtra("NAME_KEY" ,  String.valueOf(dataUser.child("nameKey").getValue()));



                }else
                {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                }

            }

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
}
