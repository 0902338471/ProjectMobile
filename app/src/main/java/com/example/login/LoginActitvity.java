package com.example.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.data.datasource;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class LoginActitvity extends AppCompatActivity implements View.OnClickListener {

    CallbackManager callbackManager;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }


    TextInputEditText nusername;
    TextInputEditText npassword;
    Button signin;
    ProgressDialog mDialog;

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


        //printKeyHash();
        callbackManager=CallbackManager.Factory.create();
        LoginButton loginButton= findViewById(R.id.login_fb);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mDialog=new ProgressDialog(LoginActitvity.this);
                mDialog.setMessage("Retrieving data");
                mDialog.show();

                handleFBtoken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActitvity.this, "Failed to connect to Facebook", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActitvity.this, "Failed to connect to Facebook", Toast.LENGTH_SHORT).show();
            }
        });


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
                Intent intent = new Intent(LoginActitvity.this, Signup.class);
                startActivity(intent);
            }
        });

        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser=mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null){
                    Toast.makeText(LoginActitvity.this,"Sign in succesfully!",Toast.LENGTH_SHORT).show();
                    //put add note activity intent here
                }
                else{
                    Toast.makeText(LoginActitvity.this,"Wrong username or password",Toast.LENGTH_SHORT).show();
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
                    mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActitvity.this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(LoginActitvity.this,"Failed to sign in",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(LoginActitvity.this,"Welcome",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });


    }

    private void handleFBtoken(AccessToken accessToken) {
        mDialog.dismiss();
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFirebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser myuserobj=mFirebaseAuth.getCurrentUser();
                    Toast.makeText(LoginActitvity.this,"Welcome",Toast.LENGTH_SHORT).show();
                    //put the add note activity intent here
                }
                else {
                    Toast.makeText(LoginActitvity.this,"Login failed, pls try again",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void printKeyHash() {
        try{
            PackageInfo info=getPackageManager().getPackageInfo("com.example.login", PackageManager.GET_SIGNATURES);
            for (Signature signature:info.signatures){
                MessageDigest md= MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Key Hash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}