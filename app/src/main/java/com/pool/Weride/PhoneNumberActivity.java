package com.pool.Weride;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class PhoneNumberActivity extends AppCompatActivity {
	
	
	private EditText editTextMobile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_number);
		
		editTextMobile = findViewById(R.id.editTextMobile);
		
		SharedPreferences mSharedPreferences = getSharedPreferences("phone", MODE_PRIVATE);
		String verfied = "";
		String type = "";
		verfied = mSharedPreferences.getString("phone", "");
		
		SharedPreferences mSharedPreferences1 = getSharedPreferences("type", MODE_PRIVATE);
		type = mSharedPreferences1.getString("type", "");
		
		try {
			
			/* in case driver and rider both are already */
			if (verfied == "verify" || type == "driver") {
				startActivity(new Intent(getApplicationContext(), CustomerMapActivity.class));
				finish();
			}
			if (verfied == "verify" || type == "rider") {
				
				startActivity(new Intent(getApplicationContext(), DriverMapActivity.class));
				finish();
			}
			
		} catch (Exception e) {
			Toast.makeText(this, "shared Preference storage", Toast.LENGTH_SHORT).show();
		}
		
		
		findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String mobile = editTextMobile.getText().toString().trim();
				
				if (mobile.isEmpty() || mobile.length() < 10) {
					editTextMobile.setError("Enter a valid mobile");
					editTextMobile.requestFocus();
					return;
				}
				
				Intent intent = new Intent(PhoneNumberActivity.this, verifyPhoneActivity.class);
				intent.putExtra("mobile", mobile);
				startActivity(intent);
			}
		});
	}
	
}