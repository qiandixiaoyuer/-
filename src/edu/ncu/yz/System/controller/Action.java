
package edu.ncu.yz.System.controller;

import edu.ncu.yz.System.entity.Account;
import edu.ncu.yz.System.entity.Course;
import edu.ncu.yz.System.entity.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/*
@author 南昌大学 杨喆
 */
public class Action implements Initializable {
    public Button btLogin;
    public Pane pane;
    public Button btSelectCourse;
    public AnchorPane root;
    public Button btMySpace;
    public Button btAllSituation;
    public AnchorPane anchorPane;
    public ScrollPane sPane;

    Account currentStudent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Text text=new Text("请先登录");
        text.setManaged(false);
        text.layoutXProperty().bind(root.widthProperty().subtract(200));
        text.layoutYProperty().bind(root.heightProperty().subtract(root.heightProperty().subtract(32)));
        root.getChildren().addAll(text);
        sPane.setVisible(false);
    }

    //“登录”按钮点击事件
    public void loginAction(ActionEvent actionEvent) {
        showLoginPane();
    }
    //显示登录界面
    public void showLoginPane(){//显示登录界面
        pane.getChildren().clear();
        sPane.setVisible(false);

        TextField tfName=new TextField();//姓名输入框
        tfName.layoutXProperty().bind((pane.widthProperty().subtract(tfName.widthProperty())).divide(2));
        tfName.layoutYProperty().bind(pane.heightProperty().divide(2).add(30));
        Label lblName=new Label("姓名:");
        lblName.layoutXProperty().bind(tfName.layoutXProperty().subtract(32));
        lblName.layoutYProperty().bind(tfName.layoutYProperty().add(4));
        lblName.setLabelFor(tfName);

        TextField tfStuID=new TextField();//学号输入框
        tfStuID.layoutXProperty().bind((pane.widthProperty().subtract(tfStuID.widthProperty())).divide(2));
        tfStuID.layoutYProperty().bind(pane.heightProperty().divide(2).subtract(30));
        Label lblStuID=new Label("学号:");
        lblStuID.setLabelFor(tfStuID);
        lblStuID.layoutXProperty().bind(tfStuID.layoutXProperty().subtract(32));
        lblStuID.layoutYProperty().bind(tfStuID.layoutYProperty().add(4));

        Button btConfirmLogin=new Button("确定");//确认登录按钮
        btConfirmLogin.layoutXProperty().bind((pane.widthProperty()).subtract(btConfirmLogin.widthProperty()).divide(2));
        btConfirmLogin.layoutYProperty().bind(tfName.layoutYProperty().add(50));
        btConfirmLogin.setOnAction(event -> {
            String name=tfName.getText();
            String stuID=tfStuID.getText();
            if(!name.equals("")&&!stuID.equals("")){

                Account student;
                if(DataBase.hasStudent(stuID)){
                    student=DataBase.getStudent(stuID);
                }
                else{
                    student=new Account(name,stuID);
                    DataBase.addStudent(student);
                }
                currentStudent=student;
                showMessage(currentStudent);
            }
        });

    pane.getChildren().addAll(tfName,tfStuID,lblName,lblStuID,btConfirmLogin);

    }

    //“选课”按钮点击事件
    public void selectCourseAction(ActionEvent actionEvent) {
        if(currentStudent != null){
            showCoursePane();
        }
    }
    //显示选课界面
    private void showCoursePane() {
        pane.getChildren().clear();
        sPane.setVisible(false);

        int yGap=30;
        int size=DataBase.schoolCoursesList.size();

        Text[][] txtCourse=new Text[size][3];//1为名字，2为学分，3为余量
        Button[] btSelectCourse=new Button[size];//选课按钮
        for(int i=0;i<3;i++){
            Text text=new Text(150*i+100,100-yGap,"");
            if(i==0) text.setText("课程名称");
            else if(i==1) text.setText("学分");
            else text.setText("余量/总量");
            pane.getChildren().add(text);
        }
        for(int i=0;i<size;i++){
            Course course=DataBase.schoolCoursesList.get(i);
            btSelectCourse[i]=new Button("选课");
            btSelectCourse[i].setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            btSelectCourse[i].setTextFill(Color.BLUE);
            btSelectCourse[i].setUnderline(true);
            int finalI1 = i;
            btSelectCourse[i].setCursor(Cursor.HAND);

            if(currentStudent.coursesList.contains(course)) {
                btSelectCourse[i].setText("退课");
                btSelectCourse[i].setTextFill(Color.RED);
                btSelectCourse[i].setUnderline(false);
            }
            btSelectCourse[i].setLayoutX(550);
            btSelectCourse[i].setLayoutY(100+i*yGap-16);
            int finalI = i;
            btSelectCourse[i].setOnAction(event -> {
                if(btSelectCourse[finalI].getText().equals("选课")){//选课
                    if(course.amountDecrease()){
                        if(currentStudent.addCourse(course)){
                            txtCourse[finalI][2].setText(String.valueOf(course.getResAmount())+"/"+String.valueOf(course.getMaxCapacity()));
                            btSelectCourse[finalI].setText("退课");
                            btSelectCourse[finalI].setTextFill(Color.RED);
                            btSelectCourse[finalI].setUnderline(false);
                            currentStudent.coursesList.sort(Course::compareTo);
                        }
                        else{
                            course.amountIncrease();
                            showAlert("您已选择了五门课程，请不要再选了！");
                        }
                    }
                    else showAlert("课程名额不足，请尝试选择其他课程");

                }
                else if(btSelectCourse[finalI].getText().equals("退课")){//退课
                    if(course.amountIncrease()){
                        if(currentStudent.dropCourse(course)){
                            txtCourse[finalI][2].setText(String.valueOf(course.getResAmount())+"/"+String.valueOf(course.getMaxCapacity()));
                            btSelectCourse[finalI].setText("选课");
                            btSelectCourse[finalI].setTextFill(Color.BLUE);
                            btSelectCourse[finalI].setUnderline(true);
                        }
                    }
                }
            });

            for(int j=0;j<3;j++){//显示课程名称，学分，余量
                txtCourse[i][j]=new Text();
                if(i==0){
                    txtCourse[i][j].setLayoutX(150*j+100);
                    txtCourse[i][j].setLayoutY(100);
                }
                if(i>0){//调整文本位置
                    txtCourse[i][j].layoutXProperty().bind(txtCourse[i-1][j].layoutXProperty());
                    txtCourse[i][j].layoutYProperty().bind(txtCourse[i-1][j].layoutYProperty().add(yGap));
                }

                if(j==0) txtCourse[i][j].setText(course.getName());//设置文本内容
                else if(j==1) txtCourse[i][j].setText(String.valueOf(course.getCredit()));
                else  txtCourse[i][j].setText(String.valueOf(course.getResAmount())+"/"+String.valueOf(course.getMaxCapacity()));
            }
        }
        Button btComplete=new Button("结束选课");//结束选课按钮
        btComplete.setCursor(Cursor.HAND);
        btComplete.setLayoutX(300);
        btComplete.setLayoutY(100+yGap*(size+2));
        btComplete.setOnAction(event -> {
            if(currentStudent.getCourseAmount()>=3&&currentStudent.getCourseAmount()<=5){
                showAlert("选课成功！");
                showMessage(currentStudent);
            }
            else if(currentStudent.getCourseAmount()<3){
                showAlert("至少选三门课！");
            }
        });
        pane.getChildren().addAll(btSelectCourse);
        pane.getChildren().addAll(btComplete);
        for(int i=0;i<size;i++){
            pane.getChildren().addAll(txtCourse[i]);
        }
    }


    //弹出一个窗口，显示提示信息
    private void showAlert(String s) {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(s);
        alert.show();
    }

    //如果登录成功，点击“我的”后会显示当前学生的信息
    public void mySpaceAction(ActionEvent actionEvent) {
        if(currentStudent!=null) showMessage(currentStudent);
        else showAlert("请先登录！");
    }

    //显示当前学生的信息，包括名字，姓名，性别，总学分，
    //所有选修的课程及成绩以及未通过的课程（补考通知）
    public void showMessage(Account student){//在面板上显示学生的个人信息
        pane.getChildren().clear();
        sPane.setVisible(false);


        int yGap=30;
        Text txtName=new Text("姓名:"+student.getName());
        txtName.setLayoutX(0);

        Text txtStuID=new Text("学号:"+student.getStuID());
        txtStuID.setLayoutX(120);

        Text txtGender=new Text("性别:"+student.getGender());
        txtGender.layoutXProperty().bind(txtName.layoutXProperty());
        txtGender.layoutYProperty().bind(txtName.layoutYProperty().add(yGap));

        Text txtTotalCredit=new Text("总学分:"+student.getTotalCredits());
        txtTotalCredit.layoutXProperty().bind(txtStuID.layoutXProperty());
        txtTotalCredit.layoutYProperty().bind(txtStuID.layoutYProperty().add(yGap));

        Text text=new Text("课程情况:");//显示学生所选课程情况
        text.layoutXProperty().bind(txtGender.layoutXProperty().subtract(50));
        text.layoutYProperty().bind(txtGender.layoutYProperty().add(1.4*yGap));
        int size=student.coursesList.size();
        Text[][] texts=new Text[size][3];
        TextField[] tfScore=new TextField[size];
        for(int i=0;i<3;i++){
            switch (i){
                case 0: Text text1 = new Text(120*i, 90, "课程名称");pane.getChildren().add(text1);break;
                case 1: Text text2=new Text(120*i,90,"学分");pane.getChildren().add(text2);break;
                case 2: Text text3=new Text(120*i,90,"成绩");pane.getChildren().add(text3);break;
            }
        }
        for(int i=0;i<size;i++){
            Course course=student.coursesList.get(i);
            for(int j=0;j<3;j++){
                texts[i][j]=new Text();
                texts[i][j].setText(switch (j){
                    case 0->course.getName();
                    case 1->String.valueOf(course.getCredit());
                    case 2->student.getGrade(course)==-1?"未录入":String.valueOf(student.getGrade(course));
                    default -> throw new IllegalStateException("Unexpected value: " + j);
                });
                if(i==0){
                    texts[i][j].setLayoutX(j*120);
                    texts[i][j].layoutYProperty().bind(txtGender.layoutYProperty().add(3*yGap));
                }
                else{
                    texts[i][j].layoutXProperty().bind(texts[i-1][j].layoutXProperty());
                    texts[i][j].layoutYProperty().bind(texts[i-1][j].layoutYProperty().add(yGap));
                }
            }
            tfScore[i]=new TextField();//修改成绩的框与课程成绩在同一位置并隐藏起来。
            tfScore[i].setVisible(false);
            tfScore[i].layoutXProperty().bind(texts[i][2].layoutXProperty());
            tfScore[i].layoutYProperty().bind(texts[i][2].layoutYProperty().subtract(15));
            tfScore[i].setPrefWidth(60);
            pane.getChildren().addAll(texts[i]);
            pane.getChildren().addAll(tfScore[i]);
        }

        if(!student.getExamSituation().equals("")){//生成补考通知
            Text text1=new Text("补考通知:\n你有:"+student.getExamSituation()+" 科目未通过\n请于yyyy年mm月dd日hh时m分前往p教室进行补考！");
            text1.setLayoutX(texts[size-1][0].getLayoutX());
            text1.setLayoutY(texts[size-1][0].getLayoutY()+100);

            pane.getChildren().addAll(text1);
        }

        Button btChangeScore=new Button("修改成绩");//修改成绩
        btChangeScore.setCursor(Cursor.HAND);
        btChangeScore.layoutXProperty().bind((pane.widthProperty().subtract(pane.widthProperty().subtract(0))));
        btChangeScore.layoutYProperty().bind((pane.heightProperty().subtract(50)));
        btChangeScore.setOnAction(event -> {
            for(int i=0;i<size;i++){
                Course course=student.coursesList.get(i);
                if(student.getGrade(course)!=-1) tfScore[i].setText(String.valueOf(student.getGrade(course)));
                else tfScore[i].setText("未录入");
                tfScore[i].setVisible(true);
            }
            Button btConfirmChange=new Button("确定");
            btConfirmChange.setCursor(Cursor.HAND);
            btConfirmChange.layoutXProperty().bind(pane.widthProperty().divide(2));
            btConfirmChange.layoutYProperty().bind(pane.heightProperty().subtract(50));
            pane.getChildren().addAll(btConfirmChange);
            btConfirmChange.setOnAction(event1 -> {//确定修改成绩
                for(int i=0;i<size;i++){
                    Course course=student.coursesList.get(i);
                    if(isNumeric(tfScore[i].getText())){
                        student.setGrade(course, Integer.parseInt(tfScore[i].getText()));
                    }
                }
                showMessage(student);
            });
        });

        pane.getChildren().addAll(txtName,txtStuID,txtGender,txtTotalCredit,btChangeScore,text);
    }


    //“学生名单”按钮点击事件
    public void allSituationAction(ActionEvent actionEvent) {showAllSituation();}
    //显示所有学生的姓名、名字及选择的课程
    public void showAllSituation(){
        pane.getChildren().clear();
        sPane.setVisible(true);
        sPane.setContent(null);
        Group group=new Group();

        Node lastNode=new Node() {};
        int yGap=30;
        int numOfStudents=DataBase.studentsList.size();
        for(int i=0;i<numOfStudents;i++){
            Account student=DataBase.studentsList.get(i);

            Text textStuID=new Text("学号:"+student.getStuID());//显示学号
            if(i>0){
//                textStuID.layoutXProperty().bind(lastNode.layoutXProperty());
//                textStuID.layoutYProperty().bind(lastNode.layoutYProperty().add(3*yGap));
                textStuID.setLayoutX(lastNode.getLayoutX());
                textStuID.setLayoutY(lastNode.getLayoutY()+3*yGap);
            }
            else{
                textStuID.setLayoutX(120);
                textStuID.setLayoutY(20);
            }
            lastNode=textStuID;

            Text textName=new Text("姓名:"+student.getName());//显示名字
//            textName.layoutXProperty().bind(textStuID.layoutXProperty().subtract(50));
//            textName.layoutYProperty().bind(textStuID.layoutYProperty());
            textName.setLayoutX(textStuID.getLayoutX()-120);
            textName.setLayoutY(textStuID.getLayoutY());

            group.getChildren().addAll(textName,textStuID);

            int numOfCourse=student.getCourseAmount();
            for(int j=0;j<numOfCourse;j++){
                Text text=new Text(student.coursesList.get(j).getName());
                text.setManaged(false);
//                text.layoutXProperty().bind(lastNode.layoutXProperty());
//                text.layoutYProperty().bind(lastNode.layoutYProperty().add(yGap));
                text.setLayoutX(lastNode.getLayoutX());
                text.setLayoutY(lastNode.getLayoutY()+yGap);
                lastNode=text;
                group.getChildren().addAll(text);
            }
        }
        sPane.setContent(group);
    }


    //“考试情况”按钮点击事件
    public void failSituaionAction(ActionEvent actionEvent) {
        showFailSituation();
    }
    //显示所有有不及格科目的学生的姓名、学号以及未通过的课程
    private void showFailSituation() {
        pane.getChildren().clear();
        sPane.setVisible(true);
        sPane.setContent(null);
        Group group=new Group();

        Text text=new Text("不及格学生名单及其未通过课程：");
        text.setFont(Font.font(26));
        text.setLayoutY(50);
        group.getChildren().add(text);

        int numOfStudents=DataBase.studentsList.size();
        int i=0;
        for(int k=0;k<numOfStudents;k++){
            Account student=DataBase.studentsList.get(k);
            if(!student.getExamSituation().equals("")){
                group.getChildren().add(new Text(0,100+30*i,"姓名:"+student.getName()+" 学号:"+student.getStuID()+"\n        "+student.getExamSituation()));
                i++;
            }
        }
        sPane.setContent(group);
    }

    //判断一个字符串是否为纯数字
    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

}
