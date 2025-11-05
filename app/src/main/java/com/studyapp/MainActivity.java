package com.studyapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We will set the layout in the next step
        // setContentView(R.layout.activity_main);
        
        setContentView(R.layout.activity_main);

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
