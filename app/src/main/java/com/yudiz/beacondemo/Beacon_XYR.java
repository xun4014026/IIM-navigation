package com.yudiz.beacondemo;

public class Beacon_XYR {

    private double x;
    private double y;
    private double r;//距離一公尺的RSSI
    private double n;
    private String nearPoint;
    private String rightPoint;
    private String leftPoint;
    public Beacon_XYR(double X,double Y,double R, double N,String np,String rp,String lp){
        x=X;
        y=Y;
        r=R;
        n=N;
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
    public double getN(){
        return n;
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
    public void setN(double n_n){ n=n_n;}
    public void setNearPoint(String np){nearPoint=np;}
    public void setRightPoint(String rp){rightPoint=rp;}
    public void setLeftPoint(String lp){leftPoint=lp;}

}
