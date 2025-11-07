package com.studyapp;

import io.supabase.postgrest.PostgrestClient;
import io.supabase.gotrue.GoTrueClient;
import io.supabase.storage.StorageClient;

public class SupabaseClient {
    // NOTE: Replace with your actual Supabase URL and Anon Key
    // For security, these should ideally be loaded from a secure source or build config,
    // but for this example, we use placeholders.
    private static final String SUPABASE_URL = "YOUR_SUPABASE_URL";
    private static final String SUPABASE_ANON_KEY = "YOUR_SUPABASE_ANON_KEY";

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
