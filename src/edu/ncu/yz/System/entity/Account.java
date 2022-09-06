/*
@author 南昌大学 杨喆
 */
package edu.ncu.yz.System.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Account {
    private String name;//学生姓名
    private String stuID;//学生学号
    private int gender;
    private int totalCredits;//学生所修课程总学分
    private int courseAmount=0;//学生选课数量
    public ArrayList<Course> coursesList=new ArrayList<>();//学生选择的课程
    ArrayList<Course> failedList=new ArrayList<>();//学生未通过的课程
    public HashMap<Course, Integer> map=new HashMap<>();//学生的课程成绩

     Account(){}

    public Account(String name, String stuID){
        this.stuID=stuID;
        this.name=name;
    }

    public String getExamSituation(){
        String str=new String("");
        for(int i=0;i<coursesList.size();i++){
            Course course=coursesList.get(i);
            if(getGrade(course)>=0&&getGrade(course)<60&&!failedList.contains(course)){

                failedList.add(course);
            }

        }
        for(int i=0;i<failedList.size();i++){
            str+=" "+failedList.get(i).getName();
        }
        return str;
    }//返回值为所有未通过的课程

    public void setGrade(Course course,int score){
        if(coursesList.contains(course)){
            map.put(course,score);
        }
    }//修改学生某门课程的成绩

    public boolean addCourse(Course course){
        if(courseAmount>=5) return false;

        coursesList.add(course);
        totalCredits+=course.getCredit();
        map.put(course, -1);
        courseAmount++;
        return true;
    }//学生选课

    public boolean dropCourse(Course course) {
        if(courseAmount<=0) return false;
        if(coursesList.contains(course)){
            coursesList.remove(course);
            totalCredits-=course.getCredit();
            courseAmount--;
            if(map.containsKey(course)) map.remove(course);
        }
        return true;
    }//学生退课

    public int getGrade(Course course){
        return map.get(course);
    }//获取学生某门课程的成绩

    public String getName() {
        return name;
    }

    public String getStuID() {
        return stuID;
    }

    public int getCourseAmount() {
        return courseAmount;
    }

    public int getGender() {
        return gender;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return stuID.equals(account.stuID);
    }//通过学号唯一确定学生

    @Override
    public int hashCode() {
        return Objects.hash(stuID);
    }
}
