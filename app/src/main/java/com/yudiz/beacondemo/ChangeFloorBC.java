package com.yudiz.beacondemo;

public class ChangeFloorBC {

    private int rssi;//RSSI
    private int minor;//minor

    ChangeFloorBC(int r, int m){
        rssi=r;
        minor=m;
    }

    private ChangeFloorBC(){
        rssi=0;
        minor=-1;
    }


    void setRssi(int r){
        rssi=r;
    }

    void setMinor(int m){
        minor=m;
    }

    int getRssi(){
        return rssi;
    }

    int getMinor(){
        return minor;
    }
}
