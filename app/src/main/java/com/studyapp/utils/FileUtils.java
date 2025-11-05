package com.studyapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    private static final String AUTHORITY = "com.studyapp.fileprovider";

    /**
     * Copies a file from a source URI (e.g., from external storage) to the app's internal storage.
     * @param context Application context.
     * @param sourceUri The URI of the file to copy.
     * @param fileName The desired name for the new file.
     * @return The absolute path of the newly copied file in internal storage, or null on failure.
     */
    public static String copyFileToInternalStorage(Context context, Uri sourceUri, String fileName) {
        File internalDir = context.getFilesDir();
        File newFile = new File(internalDir, fileName);

        try (InputStream inputStream = context.getContentResolver().openInputStream(sourceUri);
             OutputStream outputStream = new FileOutputStream(newFile)) {

            if (inputStream == null) {
                return null;
            }

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            return newFile.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Opens a file securely using FileProvider.
     * @param context Application context.
     * @param filePath The absolute path of the file in internal storage.
     */
    public static void openFile(Context context, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        Uri fileUri = FileProvider.getUriForFile(context, AUTHORITY, file);
        String mimeType = getMimeType(file.getAbsolutePath());

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, mimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(intent);
        } catch (Exception e) {
            // Handle the case where no app can handle the file type
            e.printStackTrace();
        }
    }

    /**
     * Gets the MIME type of a file based on its extension.
     */
    private static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type != null ? type : "*/*";
    }
}
