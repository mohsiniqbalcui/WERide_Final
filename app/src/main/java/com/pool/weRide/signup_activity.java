package com.pool.weRide;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pool.uber.R;

public class signup_activity extends AppCompatActivity {
	
	private EditText name, email_id, passwordcheck;
	private FirebaseAuth mAuth;
	private static final String TAG = "";
	private ProgressBar progressBar;
	
	private final static int RC_SIGN_IN = 123;
	GoogleSignInClient mGoogleSignInClient;
	FirebaseAuth.AuthStateListener mAuthListner;
	
	
	@Override
	protected void onStart() {
		Log.i("sign in start", " sign in");
		super.onStart();
		mAuth.addAuthStateListener(mAuthListner);
	}
	
	//sign Up
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup_activity);
		
		mAuth = FirebaseAuth.getInstance();
		
		//check the current user
		if (mAuth.getCurrentUser() != null) {
			startActivity(new Intent(getApplicationContext(), PhoneNumberActivity.class));
			finish();
			return;
		}
		
		Button btnSignUp = findViewById(R.id.btnRegister);
		
		btnSignUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(signup_activity.this, signin_activity.class);
				startActivity(intent);
				finish();
			}
		});
		
		
		mAuth = FirebaseAuth.getInstance();
		
		email_id = findViewById(R.id.etFullName);
		progressBar = findViewById(R.id.progressBarsignup);
		passwordcheck = findViewById(R.id.etPassword);
		
		Button register = findViewById(R.id.btnRegisterLogin);
		
		
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String email = email_id.getText().toString();
				String password = passwordcheck.getText().toString();
				
				if (TextUtils.isEmpty(email)) {
					Toast.makeText(getApplicationContext(), "Enter Eamil Id", Toast.LENGTH_SHORT).show();
					return;
				}
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
					return;
				}
				progressBar.setVisibility(View.VISIBLE);
				
				mAuth.createUserWithEmailAndPassword(email, password)
						.addOnCompleteListener(signup_activity.this, new OnCompleteListener<AuthResult>() {
							@Override
							public void onComplete(@NonNull Task<AuthResult> task) {
								
								progressBar.setVisibility(View.GONE);
								
								if (task.isSuccessful()) {
									// Sign in success, update UI with the signed-in user's information
									Log.d(TAG, "createUserWithEmail:success");
									FirebaseUser user = mAuth.getCurrentUser();
									
									Intent intent = new Intent(signup_activity.this, PhoneNumberActivity.class);
									startActivity(intent);
									finish();
								} else {
									// If sign in fails, display a message to the user.
									Log.w(TAG, "createUserWithEmail:failure", task.getException());
									Toast.makeText(signup_activity.this, "Authentication failed.",
											Toast.LENGTH_SHORT).show();
								}
								
							}
							
							
						});
			}
		});
		
		
	}
}