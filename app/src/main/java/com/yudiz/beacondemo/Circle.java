package com.yudiz.beacondemo;

public class Circle {

    private double x;
    private double y;
    private double r;//收到的RSSI
    private double d;
    private String nearPoint;
    private String rightPoint;
    private String leftPoint;
    public Circle(double X,double Y,double R,double D,String np,String rp,String lp){
        x=X;
        y=Y;
        r=R;
        d=D;
        nearPoint=np;
        rightPoint=rp;
        leftPoint=lp;

    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getR(){
        return r;
    }
    public double getD(){
        return d;
    }
    public String getNearPoint(){return nearPoint;}
    public String getRightPoint(){return rightPoint;}
    public String getLeftPoint(){return leftPoint;}

    public void setX(double x_x){
        x=x_x;
    }
    public void setY(double y_y){
        y=y_y;
    }
    public void setR(double r_r){
        r=r_r;
    }
    public void setD(double d_d){
        d=d_d;
    }
    public void setNearPoint(String np){nearPoint=np;}
    public void setRightPoint(String rp){rightPoint=rp;}
    public void setLeftPoint(String lp){leftPoint=lp;}
    public void setAll(){
        x=-1;
        y=-1;
        r=-999999;
        d=0;
        nearPoint="-1";
        rightPoint="-1";
        leftPoint="-1";
    }
}
