package com.example.roomi.roomi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SecurityRoomSettings extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private EditText nameInput;
    private EditText accessLevelInput;
    private Bundle extras;
    private String nameVal;
    private Button submitButton;

    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    RoomDatastructure data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_room_settings);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        extras = getIntent().getExtras();
        nameVal = extras.getString("name");

        //        TODO Hint in text field
//        nameInput.setHint("Current: " + nameVal);
//        accessLevelInput.setHint("Current: " + extras.getInt("accessLevel"));

        setTitle(extras.getString("name"));
        findViews();
        getDatabase();
        //      TODO Remove nav drawer???

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_hamburger);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        if (id == R.id.nav_home) {
                            Intent mAboutUs = new Intent(SecurityRoomSettings.this, HomeActivity.class);
                            startActivity(mAboutUs);
                        } else if (id == R.id.nav_security) {
                            // Goes to Security Activity
                        } else if (id == R.id.nav_settings) {
                            // Goes to Settings Page
                        } else if (id == R.id.nav_aboutus) {
                            // Displays the About Us page

                            Intent mAboutUs = new Intent(SecurityRoomSettings.this, AboutUs.class);
                            startActivity(mAboutUs);

                        } else if (id == R.id.nav_logout) {
                            // Logs out and displays the Log In Screen

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } else if (id == R.id.nav_exit) {
                            finishAffinity();
                        }

                        mDrawerLayout.closeDrawers();
                        return true;
                    }});

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()) {
                    String name = nameInput.getText().toString();
                    int accessLevel = Integer.parseInt(accessLevelInput.getText().toString());
                    dbRef.child("name").setValue(name);
                    dbRef.child("accessLevel").setValue(accessLevel);
                    Toast toast = Toast.makeText(getApplicationContext(), "Updated " + nameVal, Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void getDatabase() {
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("rooms/" + nameVal);
    }

    private void findViews() {
        accessLevelInput = findViewById(R.id.update_access_level_input);
        nameInput = findViewById(R.id.update_security_name_input);
        submitButton = findViewById(R.id.update_security_room_button);
    }

    private boolean validateData() {
        int accessLevel = 0;
        String name = nameInput.getText().toString();

        try {
            accessLevel = Integer.parseInt(accessLevelInput.getText().toString());
        } catch (Exception e) {
            Log.d("IntParse", e.toString());
        }

        if (name.length() < 0 || name.length() > 25) return false;
        if (accessLevel < 0 || accessLevel > 5) return false;
        return true;
    }
}