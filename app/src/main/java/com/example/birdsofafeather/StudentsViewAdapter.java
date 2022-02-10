package com.example.birdsofafeather;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.models.db.StudentWithCourses;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StudentsViewAdapter extends RecyclerView.Adapter<StudentsViewAdapter.ViewHolder> {
    private List<StudentWithCourses> students;
    private StudentWithCourses me;  // me, the user of the app

    // constructor
    StudentsViewAdapter(List<StudentWithCourses> students, StudentWithCourses me) {
        super();
        this.students = students;
        this.me = me;
    }

    @NonNull
    @Override
    public StudentsViewAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.classmate_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsViewAdapter.ViewHolder holder, int position){
        StudentWithCourses student = students.get(position);

        // pass a student and the number of common courses with me
        holder.setStudent(student, me.getCommonCourses(student).size());
    }

    @Override
    public int getItemCount() {
        return this.students.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final TextView studentNameView;
        private final TextView commonCourseCountView;
        private final ImageView studentImageView;
        private StudentWithCourses student;

        ViewHolder (View view) {
            super(view);
            this.studentNameView = view.findViewById(R.id.classmate_name_text);
            this.commonCourseCountView = view.findViewById(R.id.common_course_count_textview);
            this.studentImageView = view.findViewById(R.id.classmate_imageview);
            view.setOnClickListener(this);
        }

        public void setStudent(StudentWithCourses student, int count) {
            this.student = student;

            // set the view for student's name
            this.studentNameView.setText(student.getName());

            // set the view the count of the common course with the user and this student
            this.commonCourseCountView.setText(String.valueOf(count));

            // set the view student's profile
            String url = student.getHeadshotURL();
            Picasso.get().load(url).into(studentImageView);
            studentImageView.setTag(url); // Tag the image with its URL
        }

        @Override
        public void onClick(View view){
            // TODO: Go to this student's profile page
        }
    }
}
