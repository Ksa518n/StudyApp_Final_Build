package com.studyapp.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.studyapp.R;

public class TaskWorker extends Worker {

    public static final String TASK_TITLE = "task_title";
    public static final String TASK_ID = "task_id";
    private static final String CHANNEL_ID = "study_app_channel";
    private static final String CHANNEL_NAME = "Study App Reminders";

    public TaskWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String title = getInputData().getString(TASK_TITLE);
        int taskId = getInputData().getInt(TASK_ID, 0);

        sendNotification(title, taskId);

        return Result.success();
    }

    private void sendNotification(String title, int taskId) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Create the NotificationChannel, but only on API 26+ because the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_calendar_today_24dp) // Use a relevant icon
                .setContentTitle("تذكير بمهمة/اختبار")
                .setContentText("لديك موعد مستحق: " + title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Use the task ID as the notification ID to allow unique notifications for each task
        notificationManager.notify(taskId, notificationBuilder.build());
    }
}
