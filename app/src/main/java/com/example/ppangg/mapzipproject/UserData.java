package com.example.ppangg.mapzipproject;

/**
 * Created by ljs93kr on 2015-07-27.
 * 현재 사용자의 모든 데이터를 담는 싱글톤 클래스
 */
public class UserData {
    private static UserData ourInstance;

    private boolean LoginPermission; // 로그인허가
    private String UserID; // 사용자 아이디
    private String UserName; // 사용자 이름

    public static UserData getInstance() {
        if(ourInstance == null) {
            ourInstance = new UserData();
        }
        return ourInstance;
    }

    private UserData() {
        init();
    }

    private void init(){
        LoginPermission = false; // 처음 instance화 할때는 로그인허가 false
        UserID = null;
        UserName = null;
    }

    public void LoginOK(){
        LoginPermission = true;
    }

    public boolean getLoginPermission(){
        return LoginPermission;
    }

    public void inputID(String id){
        UserID = id;
    }

    public void inputName(String name){
        UserName = name;
    }

    public String getUserID(){
        return UserID;
    }

    public String getUserName(){
        return UserName;
    }

}
