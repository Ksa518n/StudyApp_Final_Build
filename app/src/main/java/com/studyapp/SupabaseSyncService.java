package com.studyapp;

import android.content.Context;
import android.util.Log;
import com.studyapp.models.SubjectModel;
import io.github.jan_tennert.supabase.postgrest.Postgrest;
import io.github.jan_tennert.supabase.postgrest.PostgrestResult;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class SupabaseSyncService {
    private static final String TAG = "SupabaseSyncService";
    private final Context context;
    private final AppRepository appRepository;
    private final Postgrest supabaseClient;

    public SupabaseSyncService(Context context) {
        this.context = context;
        this.appRepository = new AppRepository(context);
        this.supabaseClient = SupabaseClient.getPostgrestClient();
    }

    /**
     * Syncs local changes to Supabase and pulls remote changes.
     * This is a simplified example focusing on Subjects table.
     */
    public void syncSubjects() {
        Log.d(TAG, "Starting Subjects synchronization...");
        
        // 1. Push local changes (simplified: push all local subjects)
        pushLocalSubjects();
        
        // 2. Pull remote changes
        pullRemoteSubjects();
        
        Log.d(TAG, "Subjects synchronization finished.");
    }

    private void pushLocalSubjects() {
        List<SubjectModel> localSubjects = appRepository.getAllSubjects();
        if (localSubjects.isEmpty()) {
            Log.d(TAG, "No local subjects to push.");
            return;
        }

        try {
            // Convert local subjects to JSON array for Supabase
            JSONArray jsonArray = new JSONArray();
            for (SubjectModel subject : localSubjects) {
                JSONObject jsonObject = new JSONObject();
                // Note: We use the local ID as a unique identifier for now. 
                // In a real-world scenario, you'd need a UUID and a 'synced' flag.
                jsonObject.put("local_id", subject.getId()); 
                jsonObject.put("name", subject.getName());
                jsonObject.put("goal", subject.getGoal());
                jsonArray.put(jsonObject);
            }

            // Upsert (Insert or Update) data to Supabase
            // Assuming a table named 'subjects' exists in Supabase with columns: local_id, name, goal
            PostgrestResult response = supabaseClient.from("subjects")
                    .upsert(jsonArray.toString())
                    .execute();

            if (response.getError() != null) {
                Log.e(TAG, "Error pushing subjects to Supabase: " + response.getError().getMessage());
            } else {
                Log.i(TAG, "Successfully pushed " + localSubjects.size() + " subjects to Supabase.");
            }

        } catch (Exception e) {
            Log.e(TAG, "Exception during local subject push: " + e.getMessage());
        }
    }

    private void pullRemoteSubjects() {
        try {
            // Select all subjects from Supabase
            PostgrestResult response = supabaseClient.from("subjects")
                    .select("*")
                    .execute();

            if (response.getError() != null) {
                Log.e(TAG, "Error pulling subjects from Supabase: " + response.getError().getMessage());
                return;
            }

            // Parse response and update local database (simplified: replace all local data)
            JSONArray remoteSubjects = new JSONArray(response.getBody());
            Log.i(TAG, "Pulled " + remoteSubjects.length() + " subjects from Supabase.");

            // In a real sync, you would compare timestamps/versions. 
            // For this example, we just log the pull success.
            
            // TODO: Implement actual merge logic here:
            // 1. Iterate through remoteSubjects
            // 2. Check if subject exists locally (by local_id or a new 'remote_id' column)
            // 3. Insert new or update existing local subjects.

        } catch (Exception e) {
            Log.e(TAG, "Exception during remote subject pull: " + e.getMessage());
        }
    }
    
    // TODO: Add sync methods for Tasks, Notes, and Files
}
