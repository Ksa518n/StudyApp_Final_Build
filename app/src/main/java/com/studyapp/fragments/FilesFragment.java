package com.studyapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.studyapp.AppRepository;
import com.studyapp.R;
import com.studyapp.adapters.FileAdapter;
import com.studyapp.models.FileModel;
import com.studyapp.utils.FileUtils;

import java.util.List;

public class FilesFragment extends Fragment {

    private static final String ARG_SUBJECT_ID = "subject_id";
    private static final int PICK_FILE_REQUEST_CODE = 1001;

    private long subjectId;
    private AppRepository repository;
    private RecyclerView recyclerView;
    private FileAdapter adapter;

    public FilesFragment() {
        // Required empty public constructor
    }

    public static FilesFragment newInstance(long subjectId) {
        FilesFragment fragment = new FilesFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_SUBJECT_ID, subjectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            subjectId = getArguments().getLong(ARG_SUBJECT_ID);
        }
        repository = new AppRepository(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_files, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_files);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton fabAddFile = view.findViewById(R.id.fab_add_file);
        fabAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        loadFiles();
    }

    private void loadFiles() {
        List<FileModel> files = repository.getFilesBySubject(subjectId);
        adapter = new FileAdapter(getContext(), files);
        recyclerView.setAdapter(adapter);
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Allow all file types
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "اختر ملف للمادة"), PICK_FILE_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "لم يتم العثور على مدير ملفات", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                String fileName = getFileName(uri);
                String internalPath = FileUtils.copyFileToInternalStorage(getContext(), uri, fileName);

                if (internalPath != null) {
                    FileModel file = new FileModel();
                    file.setFileName(fileName);
                    file.setFilePath(internalPath);
                    file.setSubjectId(subjectId);

                    long id = repository.addFile(file);
                    if (id > 0) {
                        Toast.makeText(getContext(), "تم إضافة الملف بنجاح", Toast.LENGTH_SHORT).show();
                        loadFiles(); // Refresh list
                    } else {
                        Toast.makeText(getContext(), "فشل في حفظ بيانات الملف", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "فشل في نسخ الملف", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getFileName(Uri uri) {
        // Simple implementation: use the last segment of the URI path
        String path = uri.getPath();
        if (path != null) {
            int cut = path.lastIndexOf('/');
            if (cut != -1) {
                return path.substring(cut + 1);
            }
        }
        return "ملف_جديد";
    }
}
