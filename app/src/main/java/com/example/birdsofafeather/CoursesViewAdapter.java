package com.example.birdsofafeather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.models.db.Course;

import java.util.List;
import java.util.function.Consumer;

// the adapter class for the course recyclerView for creating and managing an item in each row
public class CoursesViewAdapter extends RecyclerView.Adapter<CoursesViewAdapter.ViewHolder> {
    private List<Course> courses;
    private Consumer<Course> onCourseRemoved; // Consumer that is used for deleting a course from database

    // constructor
    CoursesViewAdapter(List<Course> courses, Consumer<Course> onCourseRemoved){
        super();
        this.courses = courses;
        this.onCourseRemoved = onCourseRemoved;
    }

    @NonNull
    @Override
    // create the copy of the row layout and pass it to the ViewHolder
    public CoursesViewAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.course_row, parent, false);

        return new ViewHolder(view, this::removeCourse, onCourseRemoved);
    }

    @Override
    // set course data at the given position to the ViewHolder
    public void onBindViewHolder(@NonNull CoursesViewAdapter.ViewHolder holder, int position){
        holder.setCourse(courses.get(position));
    }

    @Override
    // get the size of courses list
    public int getItemCount() {
        return this.courses.size();
    }

    // add a course to a list and notify the recyclerView of the list update
    public void addCourse(Course course){
        this.courses.add(course);
        this.notifyItemInserted(this.courses.size() - 1);
    }

    // remove a course at the given position and notify the recyclerView of the list update
    public void removeCourse(int position){
        this.courses.remove(position);
        this.notifyItemRemoved(position);
    }

    // the ViewHolder class for the course recyclerView what is used for each row in the list.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseView;
        private Course course;

        ViewHolder (View itemView, Consumer<Integer> removeNote, Consumer<Course> onCourseRemoved){
            super(itemView);
            this.courseView = itemView.findViewById(R.id.course_row_text);

            // define onClick event for the remove button
            Button removeButton = itemView.findViewById(R.id.remove_course_button);
            removeButton.setOnClickListener((view) -> {
                // remove course from the list and the database
                removeNote.accept(getAdapterPosition());
                onCourseRemoved.accept(course);
            });
        }

        // set the given course to this row
        public void setCourse(Course course){
            this.course = course;
            this.courseView.setText(course.getName());
        }
    }

}
