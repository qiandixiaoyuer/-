/*
@author 南昌大学 杨喆
2022.9.7
 */
package edu.ncu.yz.System;

import edu.ncu.yz.System.entity.Course;
import edu.ncu.yz.System.entity.DataBase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadCourseMessage(new File("src/edu/ncu/yz/System/CourseList"));

        AnchorPane root= FXMLLoader.load(getClass().getResource("view/main.fxml"));
        Scene scene=new Scene(root,800,550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("学生选课系统");
        primaryStage.show();
    }

    private void loadCourseMessage(File file) throws FileNotFoundException {//从文件中导入学生信息
        Scanner fileScanner=new Scanner(file);
        while(fileScanner.hasNextLine()){
            Scanner lineScanner=new Scanner(fileScanner.nextLine());
            while (lineScanner.hasNext()){
                Course course=new Course(lineScanner.next(),lineScanner.nextInt(),lineScanner.nextInt());
                DataBase.addCourse(course);
            }
        }
    }


}
