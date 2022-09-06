/*
@author 南昌大学 杨喆
 */
package edu.ncu.yz.System.entity;

import java.util.ArrayList;

public class Course implements Comparable{
    private String teacher;
    private int maxCapacity;//课程容纳的最大人数
    private int resAmount;//课程名额余量
    private int credit;//课程的学分
    private String name;//课程名称
    ArrayList<Account> courseStudentsList=new ArrayList<>();

    public Course(String name, int credit,int maxCapacity ) {
        this.maxCapacity = maxCapacity;
        this.credit = credit;
        this.name = name;
        this.resAmount=maxCapacity;
    }

    public boolean amountDecrease(){
        if(resAmount>0){
            resAmount--;
            return true;
        }
        return false;
    }//课程余量减1

    public boolean amountIncrease(){
        if(resAmount<maxCapacity){
            resAmount++;
            return true;
        }
        return false;
    }//课程余量+1

    public String getTeacher() {
        return teacher;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getCredit() {
        return credit;
    }

    public String getName() {
        return name;
    }

    public int getResAmount() {
        return resAmount;
    }

    @Override
    public int compareTo(Object o) {
        Course course=(Course) o;
        return this.credit-course.credit;
    }
}
