package com.example.e_carorder;

public class ConnectionHelperClass {

    private String powerKW;

    public ConnectionHelperClass(){
    }

    public ConnectionHelperClass(String powerKW) {
        this.powerKW = powerKW;
    }

    public String getPowerKW() {
        return powerKW;
    }

    public void setPowerKW(String powerKW) {
        this.powerKW = powerKW;
    }
}
