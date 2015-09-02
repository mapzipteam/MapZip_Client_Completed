package com.example.ppangg.mapzipproject.map;
import android.util.Log;

public class Restaurant
{
    private String title;
    private String link;
    private String category;
    private String description;
    private String telephone;
    private String adress;
    private String roadadress;
    private int katecX;     //네이버 검색 API result 값
    private int katecY;     //네이버 검색 API result 값
    
    private double lngX;    //네이버 검색 result 값을 daum API로 바꾼 값
    private double latY;    //네이버 검색 result 값을 daum API로 바꾼 값

    public Restaurant()
    {
        title = null;
        link = null;
        category = null;
        description = null;
        telephone = null;
        adress = null;
        roadadress = null;
    }




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Log.d("vlooey", "setTitle()!!!!");this.title = title;
    }



    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }




    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }




    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }




    public String getRoadadress() {
        return roadadress;
    }

    public void setRoadadress(String roadadress) {
        this.roadadress = roadadress;
    }




    public int getKatecX() {
        return katecX;
    }

    public void setKatecX(int katecX) {
        this.katecX = katecX;
    }




    public int getKatecY() {
        return katecY;
    }

    public void setKatecY(int katecY) {
        this.katecY = katecY;
    }



    public double getLngX() {
        return lngX;
    }

    public void setLngX(double lngX) {
        this.lngX = lngX;
    }




    public double getLatY() {
        return latY;
    }

    public void setLatY(double latY) {
        this.latY = latY;
    }


}
