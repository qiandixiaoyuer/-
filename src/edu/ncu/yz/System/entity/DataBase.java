/*
@author 南昌大学 杨喆
 */
package edu.ncu.yz.System.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class DataBase {
    public static ArrayList<Course> schoolCoursesList=new ArrayList<>();
    public static ArrayList<Account> studentsList=new ArrayList<>();
    public static HashMap<String,Account> map=new HashMap<>();

    public static void addStudent(Account student){
        studentsList.add(student);
        map.put(student.getStuID(),student);
    }//向数据库里添加学生

    public static boolean hasStudent(String stuID){
        if(map.containsKey(stuID)) return true;
        return false;
    }//判断数据库里是否有该名学生

    public static Account getStudent(String stuID){
        if(map.containsKey(stuID)) return map.get(stuID);
        return null;
    }//获取学生的学号

    public static void addCourse(Course course) {
        schoolCoursesList.add(course);
    }//向数据库里添加课程


}
