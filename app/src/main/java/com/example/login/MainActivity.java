package com.example.login;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.data.datasource;
import com.example.login.model.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    TextInputEditText nusername;
    TextInputEditText npassword;
    Button signin;
    datasource acc;
    //public Account account;

    FirebaseAuth mFirebaseAuth;

    public void onClick(View view)
        {
            Log.i("info","pressed");

    }
    public void SignInClicked(View view){
        Log.i("info","pressed");


    }
    private final String TAG = this.getClass().getSimpleName();
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth=FirebaseAuth.getInstance();

       // databaseReference=firebaseDatabase.getReferenceFromUrl("https://console.firebase.google.com/u/0/project/foodnoting-account/database/firestore/data~2FAccount~2FAccount")
       // acc=datasource.getInstance();
        //account = acc.getAccount();

        nusername= (TextInputEditText)findViewById(R.id.lg_username);
        npassword=(TextInputEditText) findViewById(R.id.lg_password);
        signin=findViewById(R.id.signin);



        TextView signup = findViewById(R.id.sign_up);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent);
            }
        });

        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser=mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null){
                    Toast.makeText(MainActivity.this,"Sign in succesfully!",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"Wrong username or password",Toast.LENGTH_SHORT).show();
                }

            }
        };

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=nusername.getText().toString();
                String password=npassword.getText().toString();
                if (email.isEmpty()){
                    nusername.setError("Email can not be empty!");
                    nusername.requestFocus();
                }
                else if (password.isEmpty()){
                    npassword.setError("Password can not be empty!");
                    npassword.requestFocus();
                }
                else
                {
                    mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"Failed to sign in",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MainActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });


    }
}