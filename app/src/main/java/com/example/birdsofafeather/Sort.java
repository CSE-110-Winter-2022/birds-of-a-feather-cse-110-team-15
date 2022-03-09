package com.example.birdsofafeather;

import android.util.Pair;

import androidx.annotation.NonNull;

import com.example.birdsofafeather.models.db.Student;
import com.example.birdsofafeather.models.db.StudentWithCourses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sort {

    public List<Pair<StudentWithCourses, Integer>> sortByClassSize
            (StudentWithCourses me, @NonNull List<StudentWithCourses> otherStudents) {
        List<MyTriplet<StudentWithCourses, Integer, Double>> studentList = new ArrayList<>();

        Map<String, Double> weight = new HashMap<String, Double>();
        weight.put("Tiny", 1.00);
        weight.put("Small", 0.33);
        weight.put("Medium", 0.18);
        weight.put("Large", 0.10);
        weight.put("Huge", 0.06);
        weight.put("Gigantic", 0.03);

        // create a list of pair of student and the number of common courses
        for (StudentWithCourses student : otherStudents) {
            double curr_w = 0.0;
            List<String> commonCourses = me.getCommonCourses(student);
            int count = commonCourses.size();
            if (count == 0) {
                continue;
            }
            for(String course : commonCourses){
                curr_w += weight.get(course.split(" ")[4]);
            }
            studentList.add(new MyTriplet<>(student, count, curr_w));
        }

        // sort the list by the number of common courses in descending order
        studentList.sort((s1, s2) -> (int) (s2.getThird() - s1.getThird()));

        //create new list to modify and return
        List<Pair<StudentWithCourses, Integer>> wavedStudentAndCountPairs = new ArrayList<>();
        int position = 0;
        //if classmate waved, place at top of list
        for (MyTriplet<StudentWithCourses, Integer, Double> student : studentList) {
            //store student pair
            if(student.getFirst().getWavedToUser()){
                //add to top of waved list
                wavedStudentAndCountPairs.add(position, new Pair<>(student.getFirst(), student.getSecond()));
                position = position + 1;
            } else{
                wavedStudentAndCountPairs.add(new Pair<>(student.getFirst(), student.getSecond()));
            }
        }

        return wavedStudentAndCountPairs;
    }

    public List<Pair<StudentWithCourses, Integer>> sortByRecency
            (StudentWithCourses me, @NonNull List<StudentWithCourses> otherStudents) {
        List<MyTriplet<StudentWithCourses, Integer, Integer>> studentList = new ArrayList<>();

        Map<String, Integer> qua = new HashMap<String, Integer>();
        qua.put("WI", 4);
        qua.put("FA", 3);
        qua.put("SSS", 2);
        qua.put("SS2", 2);
        qua.put("SS1", 2);
        qua.put("SP", 1);

        // create a list of pair of student and the number of common courses
        for (StudentWithCourses student : otherStudents) {
            int curr_r = 0;
            List<String> commonCourses = me.getCommonCourses(student);
            int count = commonCourses.size();
            if (count == 0) {
                continue;
            }
            int most_recent = 2020;
            int tocompare = 0;
            for(String course : commonCourses){
                tocompare = qua.get(course.split(" ")[2]) + Integer.parseInt(course.split(" ")[3]);
                if(tocompare > most_recent){
                    most_recent = tocompare;
                }
            }
            
            studentList.add(new MyTriplet<>(student, count, most_recent));
        }

        // sort the list by the number of common courses in descending order
        studentList.sort((s1, s2) -> (int) (s2.getThird() - s1.getThird()));

        //create new list to modify and return
        List<Pair<StudentWithCourses, Integer>> wavedStudentAndCountPairs = new ArrayList<>();
        int position = 0;
        //if classmate waved, place at top of list
        for (MyTriplet<StudentWithCourses, Integer, Integer> student : studentList) {
            //store student pair
            if(student.getFirst().getWavedToUser()){
                //add to top of waved list
                wavedStudentAndCountPairs.add(position, new Pair<>(student.getFirst(), student.getSecond()));
                position = position + 1;
            } else{
                wavedStudentAndCountPairs.add(new Pair<>(student.getFirst(), student.getSecond()));
            }
        }

        return wavedStudentAndCountPairs;
    }

    class MyTriplet<T,U,V> {
        T first;
        U second;
        V third;

        public MyTriplet(T first, U second, V third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public T getFirst() { return first; }
        public U getSecond() { return second; }
        public V getThird() { return third; }
    }
}
