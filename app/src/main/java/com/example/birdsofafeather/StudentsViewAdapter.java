package com.example.birdsofafeather;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;
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

public class StudentsViewAdapter extends RecyclerView.Adapter<StudentsViewAdapter.ViewHolder> {
    // list of pair of StudentWithCourses object and the number of common courses
    private List<Pair<StudentWithCourses, Integer>> studentAndCoursesCountPairs;

    // constructor
    StudentsViewAdapter(List<Pair<StudentWithCourses, Integer>> students) {
        super();
        this.studentAndCoursesCountPairs = students;
    }

    // create a copy of the row layout and pass it to the ViewHolder
    @NonNull
    @Override
    public StudentsViewAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.classmate_row, parent, false);

        return new ViewHolder(view);
    }

    // bind student data at the given position to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull StudentsViewAdapter.ViewHolder holder, int position){
        // pass a pair of student and the number of common courses with me
        holder.setStudent(studentAndCoursesCountPairs.get(position));
    }

    // get the number of items in the list
    @Override
    public int getItemCount() {
        return this.studentAndCoursesCountPairs.size();
    }

    // update the student list and notify the RecycleView of the update
    public void updateStudentAndCoursesCountPairs(List<Pair<StudentWithCourses, Integer>> studentAndCoursesCountPairs) {
        this.studentAndCoursesCountPairs = studentAndCoursesCountPairs;
        this.notifyDataSetChanged();;
    }

    // ViewHolder for the students RecycleView that handles onClick events and set data to the row
    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final TextView studentNameView;
        private final TextView commonCourseCountView;
        private final ImageView studentImageView;
        private StudentWithCourses student;
        private int commonCourseCount;

        // constructor
        ViewHolder (View view) {
            super(view);
            this.studentNameView = view.findViewById(R.id.classmate_name_text);
            this.commonCourseCountView = view.findViewById(R.id.common_course_count_textview);
            this.studentImageView = view.findViewById(R.id.classmate_imageview);
            view.setOnClickListener(this);
        }

        // set the student's data to name view, course count view, and image view
        public void setStudent(Pair<StudentWithCourses, Integer> studentAndCountPair) {
            this.student = studentAndCountPair.first;
            this.commonCourseCount = studentAndCountPair.second;

            // set the view for student's name
            this.studentNameView.setText(student.getName());

            // set the view the count of the common course with the user and this student
            this.commonCourseCountView.setText(String.valueOf(commonCourseCount));

            // set the view student's profile
            String url = student.getHeadshotURL();
            Picasso.get().load(url).into(studentImageView);
            studentImageView.setTag(url); // Tag the image with its URL
        }

        // define the on click view
        @Override
        public void onClick(View view){
            // Go to this student's profile page
            Context context = view.getContext();
            Intent intent = new Intent(context, ViewProfileActivity.class);
            intent.putExtra("classmate_id", this.student.getStudentId());
            context.startActivity(intent);
        }
    }
}
