package com.studyapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.studyapp.AppRepository;
import com.studyapp.R;
import com.studyapp.models.NoteModel;

public class NotesFragment extends Fragment {

    private static final String ARG_SUBJECT_ID = "subject_id";
    private long subjectId;
    private AppRepository repository;
    private EditText etNotes;
    private FloatingActionButton fabSave;
    private NoteModel currentNote;

    public NotesFragment() {
        // Required empty public constructor
    }

    public static NotesFragment newInstance(long subjectId) {
        NotesFragment fragment = new NotesFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNotes = view.findViewById(R.id.et_notes_content);
        fabSave = view.findViewById(R.id.fab_save_notes);

        loadNotes();

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNotes();
            }
        });
    }

    private void loadNotes() {
        currentNote = repository.getNoteBySubjectId(subjectId);
        if (currentNote != null) {
            etNotes.setText(currentNote.getContent());
        } else {
            etNotes.setText("");
        }
    }

    private void saveNotes() {
        String content = etNotes.getText().toString();

        if (currentNote == null) {
            // New note
            currentNote = new NoteModel();
            currentNote.setSubjectId(subjectId);
            currentNote.setContent(content);
            long id = repository.addNote(currentNote);
            if (id > 0) {
                currentNote.setId(id);
                Toast.makeText(getContext(), "تم حفظ الملاحظات بنجاح", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "فشل في حفظ الملاحظات", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update existing note
            currentNote.setContent(content);
            int rows = repository.updateNote(currentNote);
            if (rows > 0) {
                Toast.makeText(getContext(), "تم تحديث الملاحظات بنجاح", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "فشل في تحديث الملاحظات", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
