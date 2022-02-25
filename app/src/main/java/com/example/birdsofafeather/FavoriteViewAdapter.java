package com.example.birdsofafeather;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.models.db.StudentWithCourses;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteViewAdapter extends RecyclerView.Adapter<FavoriteViewAdapter.FaveViewHolder> {
    List<StudentWithCourses> students;

    // Constructor for FavoriteViewAdapter
    FavoriteViewAdapter(List<StudentWithCourses> students) {
        super();
        this.students = students;
    }

    // Create a copy of the row layout and pass it to the ViewHolder
    @NonNull
    @Override
    public FaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.favorite_row, parent, false);
        return new FaveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FaveViewHolder holder, int position) {
        holder.setStudent(students.get(position));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class FaveViewHolder extends RecyclerView.ViewHolder {
        private final TextView studentNameView;
        private final ImageView studentImageView;
        private StudentWithCourses student;
        Picasso picasso;

        // Constructor
        FaveViewHolder(View view) {
            super(view);
            this.studentNameView = view.findViewById(R.id.fav_classmate_name_text);
            this.studentImageView = view.findViewById(R.id.fav_classmate_imageview);
        }

        // Set the student's data to name view and image view
        public void setStudent(StudentWithCourses student) {
            this.student = student;

            // Set the view for student's name
            this.studentNameView.setText(student.getName());

            picasso = new Picasso.Builder(studentNameView.getContext()).build();
            try {
                Picasso.setSingletonInstance(picasso);
            } catch (Exception e) {
            }

            String url = student.getHeadshotURL();
            Picasso.get().load(url).into(studentImageView);
            studentImageView.setTag(url); // Tag the image with its URL
        }
    }

}
