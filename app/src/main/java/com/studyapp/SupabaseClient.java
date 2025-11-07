package com.studyapp;

import io.supabase.postgrest.PostgrestClient;
import io.supabase.gotrue.GoTrueClient;
import io.supabase.storage.StorageClient;

public class SupabaseClient {
    // NOTE: Replace with your actual Supabase URL and Anon Key
    // For security, these should ideally be loaded from a secure source or build config,
    // but for this example, we use placeholders.
    private static final String SUPABASE_URL = "https://erzofxlzvqzkeonuwibn.supabase.co";
    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVyem9meGx6dnF6a2VvbnV3aWJuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjI1MTI2MzMsImV4cCI6MjA3ODA4ODYzM30.xdl5-Y3htA8FmjJDnyWoMmNBZhd9Pdjn_mqykNup_6A";

    private static PostgrestClient postgrestClient;
    private static GoTrueClient goTrueClient;
    private static StorageClient storageClient;

    public static void initialize() {
        // Postgrest Client for database interaction
        postgrestClient = new PostgrestClient(SUPABASE_URL + "/rest/v1", SUPABASE_ANON_KEY);
        
        // GoTrue Client for authentication (optional for now, but good practice)
        goTrueClient = new GoTrueClient(SUPABASE_URL + "/auth/v1", SUPABASE_ANON_KEY);
        
        // Storage Client for file management
        storageClient = new StorageClient(SUPABASE_URL + "/storage/v1", SUPABASE_ANON_KEY);
    }

    public static PostgrestClient getPostgrestClient() {
        if (postgrestClient == null) {
            initialize();
        }
        return postgrestClient;
    }
    
    public static GoTrueClient getGoTrueClient() {
        if (goTrueClient == null) {
            initialize();
        }
        return goTrueClient;
    }
    
    public static StorageClient getStorageClient() {
        if (storageClient == null) {
            initialize();
        }
        return storageClient;
    }
}
