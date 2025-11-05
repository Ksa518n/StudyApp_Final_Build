package com.studyapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.studyapp.AppRepository;
import com.studyapp.R;
import com.studyapp.models.FileModel;
import com.studyapp.utils.FileUtils;

import java.io.File;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    private Context context;
    private List<FileModel> fileList;
    private AppRepository repository;

    public FileAdapter(Context context, List<FileModel> fileList) {
        this.context = context;
        this.fileList = fileList;
        this.repository = new AppRepository(context);
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_file_card, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        FileModel file = fileList.get(position);
        holder.fileName.setText(file.getFileName());

        // Handle click to open file
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtils.openFile(context, file.getFilePath());
            }
        });

        // Handle delete click
        holder.fileDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile(file, position);
            }
        });
    }

    private void deleteFile(FileModel file, int position) {
        // 1. Delete the physical file
        File physicalFile = new File(file.getFilePath());
        if (physicalFile.exists()) {
            physicalFile.delete();
        }

        // 2. Delete the record from the database
        int rowsAffected = repository.deleteFile(file.getId());

        if (rowsAffected > 0) {
            // 3. Remove from the list and notify adapter
            fileList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "تم حذف الملف بنجاح", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "فشل في حذف الملف من قاعدة البيانات", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        ImageView fileDelete;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.file_name);
            fileDelete = itemView.findViewById(R.id.file_delete);
        }
    }
}
