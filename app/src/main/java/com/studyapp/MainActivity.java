package com.studyapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import com.google.android.material.button.MaterialButton;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppRepository appRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We will set the layout in the next step
        // setContentView(R.layout.activity_main);
        
        setContentView(R.layout.activity_main);

        // Initialize AppRepository (This is a common crash point if DB is corrupted or tables are missing)
        try {
            appRepository = new AppRepository(this);
            // Optional: Call a simple read operation to force DB creation/check
            appRepository.getAllSubjects(); 
        } catch (Exception e) {
            // Log the error to the console (Logcat)
            Log.e("MainActivity", "Database initialization failed: " + e.getMessage());
            // Show a user-friendly error message and exit gracefully
            Toast.makeText(this, "Database Error: Cannot start application.", Toast.LENGTH_LONG).show();
            finish();
            return; // Exit onCreate
        }

        // Initialize UI components and set up quick access button
        MaterialButton btnQuickAccess = findViewById(R.id.btn_quick_access_subjects);
        btnQuickAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubjectActivity.class);
                startActivity(intent);
            }
        });
    }
}
