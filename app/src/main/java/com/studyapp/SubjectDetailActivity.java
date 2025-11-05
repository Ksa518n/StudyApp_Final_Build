package com.studyapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import com.studyapp.adapters.SubjectDetailPagerAdapter;

public class SubjectDetailActivity extends AppCompatActivity {

    public static final String EXTRA_SUBJECT_ID = "subject_id";
    public static final String EXTRA_SUBJECT_NAME = "subject_name";
    public static final String EXTRA_SUBJECT_GOAL = "subject_goal";

    private long subjectId;
    private String subjectName;
    private String subjectGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_detail);

        // Get data from Intent
        subjectId = getIntent().getLongExtra(EXTRA_SUBJECT_ID, -1);
        subjectName = getIntent().getStringExtra(EXTRA_SUBJECT_NAME);
        subjectGoal = getIntent().getStringExtra(EXTRA_SUBJECT_GOAL);

        if (subjectId == -1) {
            Toast.makeText(this, "خطأ: لم يتم تحديد المادة", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set up Toolbar title
        TextView tvSubjectName = findViewById(R.id.tv_subject_name_detail);
        tvSubjectName.setText(subjectName);
        
        TextView tvSubjectGoal = findViewById(R.id.tv_subject_goal_detail);
        tvSubjectGoal.setText("الهدف: " + subjectGoal);

        // Set up ViewPager and TabLayout
        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        SubjectDetailPagerAdapter adapter = new SubjectDetailPagerAdapter(getSupportFragmentManager(), subjectId);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
