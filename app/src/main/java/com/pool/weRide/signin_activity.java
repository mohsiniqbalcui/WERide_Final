package com.pool.weRide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pool.uber.R;

public class signin_activity extends AppCompatActivity {
	
	private static final String TAG = "";
	private EditText inputEmail, inputPassword;
	private FirebaseAuth mAuth;
	private ProgressBar progressBar;
	
	public static String googleName = "";
	public static String googleEmail = "";
	
	
	private final static int RC_SIGN_IN = 123;
	GoogleSignInClient mGoogleSignInClient;
	FirebaseAuth.AuthStateListener mAuthListner;
	
	
	@Override
	protected void onStart() {
		Log.i("sign in start", " sign in");
		super.onStart();
		mAuth.addAuthStateListener(mAuthListner);
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		mAuth.removeAuthStateListener(mAuthListner);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_singin_activity);
		mAuth = FirebaseAuth.getInstance();
		
		//check the current user
		if (mAuth.getCurrentUser() != null) {
			startActivity(new Intent(signin_activity.this, PhoneNumberActivity.class));
			finish();
		}
		
		inputEmail = findViewById(R.id.LoginEmail);
		inputPassword = findViewById(R.id.LoginPassword);
		
		Button ahlogin = findViewById(R.id.btnsignin);
		Button btnsignup = findViewById(R.id.btnLoginRegister);
		
		progressBar = findViewById(R.id.progressBar);
		//TextView btnSignIn = (TextView) findViewById(R.id.btnsignin);
		com.shobhitpuri.custombuttons.GoogleSignInButton lGoogleSignInButton = findViewById(R.id.sign_in_button);
		
		lGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				signIn();// google sign in
			}
		});
		
		btnsignup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(signin_activity.this, signup_activity.class));
			}
		});
		
		
		mAuth = FirebaseAuth.getInstance();
		
		// Checking the email id and password is Empty
		ahlogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String email = inputEmail.getText().toString();
				final String password = inputPassword.getText().toString();
				
				if (TextUtils.isEmpty(email)) {
					Toast.makeText(getApplicationContext(), "Please enter email id", Toast.LENGTH_SHORT).show();
					return;
				}
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
					return;
				}
				
				progressBar.setVisibility(View.VISIBLE);
				
				//authenticate user
				mAuth.signInWithEmailAndPassword(email, password)
						.addOnCompleteListener(signin_activity.this, new OnCompleteListener<AuthResult>() {
							@Override
							public void onComplete(@NonNull Task<AuthResult> task) {
								
								progressBar.setVisibility(View.GONE);
								
								if (task.isSuccessful()) {
									// there was an error
									Log.d(TAG, "signInWithEmail:success");
									
									Intent intent = new Intent(signin_activity.this, PhoneNumberActivity.class);
									startActivity(intent);
									finish();
									
								} else {
									Log.d(TAG, "singInWithEmail:Fail");
									Toast.makeText(signin_activity.this, getString(R.string.failed), Toast.LENGTH_LONG).show();
								}
							}
							
						});
			}
			
		});
		
		String name = "";
		
		mAuthListner = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				if (firebaseAuth.getCurrentUser() != null) {
					
					FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
					googleEmail = user.getEmail();
					googleName = user.getDisplayName();
					Uri googleimageuri = user.getPhotoUrl();
					
			/*		NavigationView lNavigationView = findViewById(R.id.nav_view);
					View headerView = lNavigationView.getHeaderView(0);
					TextView navUsername = headerView.findViewById(R.id.username);
					TextView navEmail = headerView.findViewById(R.id.useremail);
					ImageView lImageView = headerView.findViewById(R.id.imageView);
					
					Picasso.get().load(googleimageuri).into(lImageView);
			*/
					
					
					SharedPreferences sharedpreferences = getSharedPreferences("user", MODE_PRIVATE);
					final SharedPreferences.Editor editor = sharedpreferences.edit();
					editor.putString("name", googleName);
					editor.putString("email", googleEmail);
					editor.apply();
					editor.commit();
					
					startActivity(new Intent(signin_activity.this, PhoneNumberActivity.class));
				}
				
			}
		};
		
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();
		
		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
		
	}
	
	
	private void signIn() {
		Intent signInIntent = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				// Google Sign In was successful, authenticate with Firebase
				GoogleSignInAccount account = task.getResult(ApiException.class);
				firebaseAuthWithGoogle(account);
			} catch (ApiException e) {
				// Google Sign In failed, update UI appropriately
				Log.w(TAG, "Google sign in failed", e);
				
			}
		}
	}
	
	private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
		
		AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							Log.d(TAG, "signInWithCredential:success");
							FirebaseUser user = mAuth.getCurrentUser();
							//updateUI(user);
						} else {
							// If sign in fails, display a message to the user.
							Log.w(TAG, "signInWithCredential:failure", task.getException());
							Toast.makeText(signin_activity.this, "Aut Fail", Toast.LENGTH_SHORT).show();
							//updateUI(null);
						}
						
						// ...
					}
				});
	}
	
}