package com.example.birdsofafeather;

import android.util.Pair;

import androidx.annotation.NonNull;

import com.example.birdsofafeather.models.db.Student;
import com.example.birdsofafeather.models.db.StudentWithCourses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sorter {
    private Map<String, Integer> sizeWeights;
    private Map<String, Integer> ageWeights;

    public Sorter() {
        // initialize maps for sorting
        sizeWeights = new HashMap<>();
        sizeWeights.put("Tiny", 100);
        sizeWeights.put("Small", 33);
        sizeWeights.put("Medium", 18);
        sizeWeights.put("Large", 10);
        sizeWeights.put("Huge", 6);
        sizeWeights.put("Gigantic", 3);

        ageWeights = new HashMap<>();
        ageWeights.put("FA 2021", 5);
        ageWeights.put("SSS 2021", 4);
        ageWeights.put("SS2 2021", 4);
        ageWeights.put("SS1 2021", 4);
        ageWeights.put("SP 2021", 3);
        ageWeights.put("WI 2021", 2);
    }

    public List<Pair<StudentWithCourses, Integer>> sortList(
            ALGORITHM algorithm,
            StudentWithCourses me,
            @NonNull List<StudentWithCourses> otherStudents
    )
    {
        List<MyTriplet<StudentWithCourses, Integer, Integer>> studentList = new ArrayList<>();
        // choose which map based on algorithm
        Map<String, Integer> curMap;
        switch(algorithm) {
            case CLASS_SIZE:
                curMap = sizeWeights;
                break;
            case RECENCY:
                curMap = ageWeights;
                break;
            default:
                curMap = new HashMap<>();
        }

        // create a list of triplets: (Student Obj, Common Courses count, weight)
        for (StudentWithCourses student : otherStudents) {
            int weight = 0;
            List<String> commonCourses = me.getCommonCourses(student);
            int count = commonCourses.size();
            if (count == 0) {
                continue;
            }

            // only calculate weight for class size and recency sorts
            if (algorithm != ALGORITHM.DEFAULT) {
                for (String course : commonCourses) {
                    String[] split = course.split(" ");
                    String query = algorithm == ALGORITHM.CLASS_SIZE ? split[4] : split[2] + " " + split[3];
                    weight += curMap.getOrDefault(query, 1);
                }
            }
            // add student to list
            studentList.add(new MyTriplet<>(student, count, weight));
        }

        // sort the list by count or calculated weight in descending order
        if (algorithm == ALGORITHM.DEFAULT) {
            studentList.sort((s1, s2) -> (s2.getCount() - s1.getCount()));
        }
        else {
            studentList.sort((s1, s2) -> s2.getWeight() - s1.getWeight());
        }

        // create new list to adhere to waving properties
        List<Pair<StudentWithCourses, Integer>> wavedStudentAndCountPairs = new ArrayList<>();
        int position = 0;
        //if classmate waved, place at top of list
        for (MyTriplet<StudentWithCourses, Integer, Integer> student : studentList) {
            //store student pair
            if(student.getStudent().getWavedToUser()){
                //add to top of waved list
                wavedStudentAndCountPairs.add(position++, student.getPair());
            } else{
                wavedStudentAndCountPairs.add(student.getPair());
            }
        }

        return wavedStudentAndCountPairs;
    }

    class MyTriplet<StudentWithCourses,K,V> {
        StudentWithCourses student;
        K count;
        V weight;

        public MyTriplet(StudentWithCourses student, K count, V weight) {
            this.student = student;
            this.count = count;
            this.weight = weight;
        }

        public StudentWithCourses getStudent() { return student; }
        public K getCount() { return count; }
        public V getWeight() { return weight; }
        public Pair<StudentWithCourses, K> getPair() { return new Pair<>(student, count); }
    }

    // ENUM to select which algorithm to use
    public enum ALGORITHM {
        DEFAULT,
        CLASS_SIZE,
        RECENCY
    }
}
