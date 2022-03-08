package com.example.birdsofafeather;

import android.util.Pair;

import com.example.birdsofafeather.models.db.StudentWithCourses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sorter {

    /*
    Helper function to constructs and returns the needed weighting scheme depending on the
    option chosen by user
    */
    Map<String, Integer> getWeights(String option){
        // Manually define the last 5 quarters. (Starting from current one. Summer counts as 1 quarter)
        Map<String, Integer> quarterScores = new HashMap<>();
        quarterScores.put("WI 2022", 5);
        quarterScores.put("FA 2021", 4);
        quarterScores.put("SS1 2021", 3);
        quarterScores.put("SS2 2021", 3);
        quarterScores.put("SSS 2021", 3);
        quarterScores.put("SP 2021", 2);

        // Manually define the weights for each of the class sizes
        Map<String, Integer> sizeScores = new HashMap<>();
        sizeScores.put("Tiny", 100);
        sizeScores.put("Small", 33);
        sizeScores.put("Medium", 18);
        sizeScores.put("Large", 10);
        sizeScores.put("Huge", 6);
        sizeScores.put("Gigantic", 3);

        // Return the correct weight scores depending on the option
        if (option.equals("By Class Age")){
            return quarterScores;
        }
        return sizeScores;
    }

    /*
    Parse the needed information from a Course string in order depending on the option chosen
    Parameters:
    - courseString: the full String representing a course, ex. "CSE 12 FA 2021 Large"
                    Assumed to be in this order: [course name] [quarter] [year] [size]
    - option: sorting option chosen. By class size or age.

    Returns:
    - The relevant part of the course string. Ex. Will return "FA 2021" if the option is "By Class Age",
      and "Large" if the option is "By Class Size".
     */
    String getCourseInfo(String courseString, String option){
        String[] courseSplit = courseString.split(" ");

        if (option.equals("By Class Size")){
            // Parse class size string
            return courseSplit[4];
        }
        // Parse quarter and year from course string
        return courseSplit[2] + " " +  courseSplit[3];
    }


    /*
    This function sorts a list of students with common classes depending on the option given to it.
    Parameters:
    - students : a list of a students to be sorted
    - me : the user of the app. Who we are comparing common courses against.
    - option : the sorting option given by the user. Currently only supports two options- sorting
                    by age of classes shared or size of classes shared.
    Returns:
    - a list of students, sorted accordingly.
     */
    List<StudentWithCourses> sortByOption(List<StudentWithCourses> students, StudentWithCourses me, String option) {
        // Get correct weighting scheme according to option
        Map<String, Integer> weights = getWeights(option);

        // For each student, calculate the sum of the weight for each of the common classes
        List<Pair<StudentWithCourses, Integer>> studentAndWeight = new ArrayList<>();
        for (StudentWithCourses student : students){
            List<String> commonCourses = me.getCommonCourses(student);
            int weight = 0;
            for (String course : commonCourses){
                // For each class, calculate the single class weight and add to total weight for that student
                String key = getCourseInfo(course, option);
                weight += weights.getOrDefault(key, 1);
            }
            studentAndWeight.add(new Pair(student, weight));
        }
        // Sort the students by their weights, return a list of the sorted students
        studentAndWeight.sort((s1, s2) -> s2.second - s1.second);
        List<StudentWithCourses> sortedStudents = new ArrayList<>();

        for (Pair<StudentWithCourses, Integer> student : studentAndWeight){
            sortedStudents.add(student.first);
        }
        return sortedStudents;
    }

}
