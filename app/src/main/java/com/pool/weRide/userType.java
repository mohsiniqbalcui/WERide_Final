package com.pool.weRide;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.pool.uber.R;

public class userType extends AppCompatActivity {
	
	Button btn1;
	Button btn2;
	Intent intent;
	Editor editor;
	
	SharedPreferences mSharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_type);
		
		btn1 = findViewById(R.id.driverUser);
		btn2 = findViewById(R.id.riderUser);
		
		mSharedPreferences = getSharedPreferences("type", 0); // 0 - for private mode
		editor = mSharedPreferences.edit();
		
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		editor.commit(); // commit changes
	}
	
	public void driverClick(View view) {
		
		editor.putString("type", "driver"); // Storing string
		editor.apply();
		intent = new Intent(this, signin_activity.class);
		startActivity(intent);
		finish();
		
	}
	
	public void riderclick(View view) {
		
		editor.putString("usertype", "rider"); // Storing string
		editor.apply();
		intent = new Intent(this, signin_activity.class);
		startActivity(intent);
		finish();
		
		Log.i("user type", "before sign in");
		
	}
	
	
}
