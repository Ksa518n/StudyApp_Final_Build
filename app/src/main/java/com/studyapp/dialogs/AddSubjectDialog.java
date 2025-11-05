package com.studyapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.studyapp.AppRepository;
import com.studyapp.R;
import com.studyapp.models.SubjectModel;

public class AddSubjectDialog extends Dialog {

    private EditText etSubjectName;
    private EditText etSubjectGoal;
    private Button btnSave;
    private Button btnCancel;
    private AppRepository repository;
    private SubjectAddedListener listener;

    public interface SubjectAddedListener {
        void onSubjectAdded();
    }

    public AddSubjectDialog(Context context, SubjectAddedListener listener) {
        super(context);
        this.repository = new AppRepository(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_subject);

        etSubjectName = findViewById(R.id.et_subject_name);
        etSubjectGoal = findViewById(R.id.et_subject_goal);
        btnSave = findViewById(R.id.btn_save_subject);
        btnCancel = findViewById(R.id.btn_cancel_subject);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSubject();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void saveSubject() {
        String name = etSubjectName.getText().toString().trim();
        String goal = etSubjectGoal.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "الرجاء إدخال اسم المادة", Toast.LENGTH_SHORT).show();
            return;
        }

        SubjectModel subject = new SubjectModel();
        subject.setName(name);
        subject.setGoal(goal.isEmpty() ? "لا يوجد هدف محدد" : goal);

        long id = repository.addSubject(subject);

        if (id > 0) {
            Toast.makeText(getContext(), "تم إضافة المادة بنجاح", Toast.LENGTH_SHORT).show();
            if (listener != null) {
                listener.onSubjectAdded();
            }
            dismiss();
        } else {
            Toast.makeText(getContext(), "فشل في إضافة المادة. قد يكون الاسم مكررًا.", Toast.LENGTH_LONG).show();
        }
    }
}
