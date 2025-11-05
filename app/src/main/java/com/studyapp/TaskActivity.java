package com.studyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.studyapp.adapters.TaskAdapter;
import com.studyapp.models.TaskModel;

import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private AppRepository repository;
    private FloatingActionButton fabAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        repository = new AppRepository(this);

        recyclerView = findViewById(R.id.recycler_view_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAddTask = findViewById(R.id.fab_add_task);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement dialog or new activity to add task
                Toast.makeText(TaskActivity.this, "فتح شاشة إضافة مهمة", Toast.LENGTH_SHORT).show();
            }
        });

        loadTasks();
    }

    private void loadTasks() {
        List<TaskModel> tasks = repository.getUpcomingTasks();
        adapter = new TaskAdapter(this, tasks);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }
}
