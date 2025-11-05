package com.studyapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.studyapp.R;
import com.studyapp.models.TaskModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<TaskModel> taskList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

    public TaskAdapter(Context context, List<TaskModel> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task_card, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskModel task = taskList.get(position);
        holder.taskTitle.setText(task.getTitle());
        holder.taskType.setText("النوع: " + task.getType());
        holder.taskCheckbox.setChecked(task.isCompleted());
        
        // Format Due Date
        holder.taskDueDate.setText(dateFormat.format(new Date(task.getDueDate())));
        
        // Calculate Countdown
        long diff = task.getDueDate() - System.currentTimeMillis();
        String countdownText;
        if (diff < 0) {
            countdownText = "انتهى";
        } else {
            long days = TimeUnit.MILLISECONDS.toDays(diff);
            if (days > 0) {
                countdownText = days + " أيام";
            } else {
                long hours = TimeUnit.MILLISECONDS.toHours(diff);
                if (hours > 0) {
                    countdownText = hours + " ساعات";
                } else {
                    countdownText = "قريبًا جدًا";
                }
            }
        }
        holder.taskCountdown.setText(countdownText);
        
        // TODO: Implement Checkbox listener to update task completion status in DB
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle;
        TextView taskType;
        TextView taskDueDate;
        TextView taskCountdown;
        CheckBox taskCheckbox;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.task_title);
            taskType = itemView.findViewById(R.id.task_type);
            taskDueDate = itemView.findViewById(R.id.task_due_date);
            taskCountdown = itemView.findViewById(R.id.task_countdown);
            taskCheckbox = itemView.findViewById(R.id.task_checkbox);
        }
    }
}
