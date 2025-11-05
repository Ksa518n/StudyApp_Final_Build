package com.studyapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.studyapp.R;
import com.studyapp.models.SubjectModel;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private Context context;
    private List<SubjectModel> subjectList;

    public SubjectAdapter(Context context, List<SubjectModel> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject_card, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        SubjectModel subject = subjectList.get(position);
        holder.subjectName.setText(subject.getName());
        holder.subjectGoal.setText("الهدف: " + subject.getGoal());
        
        // Handle click to open subject details
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SubjectDetailActivity.class);
                intent.putExtra(SubjectDetailActivity.EXTRA_SUBJECT_ID, subject.getId());
                intent.putExtra(SubjectDetailActivity.EXTRA_SUBJECT_NAME, subject.getName());
                intent.putExtra(SubjectDetailActivity.EXTRA_SUBJECT_GOAL, subject.getGoal());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        TextView subjectGoal;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subject_name);
            subjectGoal = itemView.findViewById(R.id.subject_goal);
        }
    }
}
