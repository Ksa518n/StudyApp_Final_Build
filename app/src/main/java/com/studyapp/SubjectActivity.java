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
import com.studyapp.dialogs.AddSubjectDialog;

import java.util.List;

public class SubjectActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SubjectAdapter adapter;
    private AppRepository repository;
    private FloatingActionButton fabAddSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

        repository = new AppRepository(this);

        recyclerView = findViewById(R.id.recycler_view_subjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAddSubject = findViewById(R.id.fab_add_subject);
        fabAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSubjectDialog dialog = new AddSubjectDialog(SubjectActivity.this, new AddSubjectDialog.SubjectAddedListener() {
                            @Override
            public void onSubjectAdded() {
                loadSubjects(); // Reload subjects after a new one is added
            }
        });
        dialog.show();
            }
        });

        loadSubjects();
    }

    private void loadSubjects() {
        List<SubjectModel> subjects = repository.getAllSubjects();
        adapter = new SubjectAdapter(this, subjects);
        recyclerView.setAdapter(adapter);
    }
    
    // This method will be called when a new subject is added or deleted
    @Override
    protected void onResume() {
        super.onResume();
        loadSubjects();
    }
}
