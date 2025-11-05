package com.studyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.studyapp.adapters.SubjectAdapter;
import com.studyapp.models.SubjectModel;

import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SubjectAdapter adapter; // Reusing SubjectAdapter to show subjects as folders
    private AppRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        repository = new AppRepository(this);

        recyclerView = findViewById(R.id.recycler_view_library_subjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // FAB is not needed here as files are added via Subject Details screen
        
        loadSubjects();
    }

    private void loadSubjects() {
        // Load all subjects to act as folders
        List<SubjectModel> subjects = repository.getAllSubjects();
        // We need a specialized adapter for the library view if we want different behavior,
        // but for now, we'll reuse SubjectAdapter and handle the click in the adapter.
        adapter = new SubjectAdapter(this, subjects); 
        recyclerView.setAdapter(adapter);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadSubjects();
    }
}
