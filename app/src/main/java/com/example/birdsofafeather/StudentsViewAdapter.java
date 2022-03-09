package com.example.birdsofafeather;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.models.db.StudentWithCourses;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.function.BiConsumer;

public class StudentsViewAdapter extends RecyclerView.Adapter<StudentsViewAdapter.ViewHolder> {
    // List of pair of StudentWithCourses object and the number of common courses
    private List<Pair<StudentWithCourses, Integer>> studentAndCoursesCountPairs;
    private final BiConsumer<String, Boolean> onFavorite;

    // Constructor for StudentsViewAdapter
    StudentsViewAdapter(List<Pair<StudentWithCourses, Integer>> students, BiConsumer<String, Boolean> onFavorite) {
        super();
        this.studentAndCoursesCountPairs = students;
        this.onFavorite = onFavorite;
    }

    // Create a copy of the row layout and pass it to the ViewHolder
    @NonNull
    @Override
    public StudentsViewAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.classmate_row, parent, false);

        //initial: no wave-> wave is invisible
        ImageView wavedToUser = (ImageView) view.findViewById(R.id.classmate_waved);
        wavedToUser.setVisibility(View.INVISIBLE);

        return new ViewHolder(view, onFavorite);
    }

    // Bind student data at the given position to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull StudentsViewAdapter.ViewHolder holder, int position){
        // Pass a pair of student and the number of common courses with me
        holder.setStudent(studentAndCoursesCountPairs.get(position));
    }

    // Get the number of items in the list
    @Override
    public int getItemCount() {
        return this.studentAndCoursesCountPairs.size();
    }

    // Update the student list and notify the RecycleView of the update
    public void updateStudentAndCoursesCountPairs(List<Pair<StudentWithCourses, Integer>> studentAndCoursesCountPairs) {
        this.studentAndCoursesCountPairs = studentAndCoursesCountPairs;
        this.notifyDataSetChanged();
    }

    // ViewHolder for the students RecycleView that handles onClick events and set data to the row
    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final TextView studentNameView;
        private final TextView commonCourseCountView;
        private final ImageView studentImageView;
        private final ImageView waveView;
        private StudentWithCourses student;
        private final CheckBox fav;
        Picasso picasso;

        // Constructor
        ViewHolder (View view, BiConsumer<String, Boolean> onFavorite) {
            super(view);
            this.studentNameView = view.findViewById(R.id.classmate_name_text);
            this.commonCourseCountView = view.findViewById(R.id.common_course_count_textview);
            this.studentImageView = view.findViewById(R.id.classmate_imageview);
            this.waveView = view.findViewById(R.id.classmate_waved);

            view.setOnClickListener(this);

            // Handle favoriting/unfavoriting with a Toast and update to Student object
            fav = view.findViewById(R.id.favorite);
            fav.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (buttonView.isChecked()) {
                    Toast.makeText(view.getContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                    student.setFavorite(true);
                } else {
                    Toast.makeText(view.getContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
                    student.setFavorite(false);
                }
                // onFavorite updates the student in the database
                onFavorite.accept(student.getUUID(), student.isFavorite());
            });
        }

        // Set the student's data to name view, course count view, and image view
        public void setStudent(Pair<StudentWithCourses, Integer> studentAndCountPair) {
            this.student = studentAndCountPair.first;
            int commonCourseCount = studentAndCountPair.second;

            // Set the view for student's name
            this.studentNameView.setText(student.getName());

            // Set the view the count of the common course with the user and this student
            this.commonCourseCountView.setText(String.valueOf(commonCourseCount));

            // Set the view student's profile
            picasso = new Picasso.Builder(fav.getContext()).build();
            try {
                Picasso.setSingletonInstance(picasso);
            } catch (Exception e) {
            }

            String url = student.getHeadshotURL();

            // Catch Picasso singleton exceptions (only happens during testing)
            Picasso picasso = new Picasso.Builder(studentNameView.getContext()).build();
            try {
                Picasso.setSingletonInstance(picasso);
            } catch (Exception e) {
            }

            Picasso.get().load(url).into(studentImageView);
            studentImageView.setTag(url); // Tag the image with its URL

            // Set favorite status
            fav.setChecked(student.isFavorite());

            //if student waved to user, wave is visible
            if(student.getWavedToUser()){
                waveView.setVisibility(View.VISIBLE);
            }
        }

        // Define the onClick behavior
        @Override
        public void onClick(View view){
            // Go to this student's profile page
            Context context = view.getContext();
            Intent intent = new Intent(context, ViewProfileActivity.class);
            intent.putExtra("classmate_id", this.student.getUUID());
            context.startActivity(intent);
        }
    }
}
