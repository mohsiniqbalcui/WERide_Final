package com.pool.Weride;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.pool.Weride.fragments.ChatFragment;
import com.pool.Weride.fragments.MessageFragment;
import com.pool.Weride.fragments.ProfileFragment;


public class drawer_layout extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	private DrawerLayout drawer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer_layout);
		
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		drawer = findViewById(R.id.drawer_layout);
		NavigationView navigationView = findViewById(R.id.nav_view);
		
		navigationView.setNavigationItemSelectedListener(this);
		
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
				R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		
		toggle.syncState();
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
					new MessageFragment()).commit();
			
			navigationView.setCheckedItem(R.id.nav_home);
		}
	}
	
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.nav_home:
				getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
						new MessageFragment()).commit();
				break;
			case R.id.nav_rides:
				getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
						new ChatFragment()).commit();
				break;
			case R.id.nav_transaction:
				getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
						new ProfileFragment()).commit();
				break;
			case R.id.nav_share:
				Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_help:
				Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_settings:
				Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_howWork:
				Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_logout:
				Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
				break;
		}
		
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
}
