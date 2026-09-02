package com.example.todolistplanner;

import java.io.Serializable;

public class ListaCuSarcini implements Serializable {
    private int id;
    private String sarcina;
    private String data;
    private int bifat;

    public ListaCuSarcini(int id, String sarcina, String data, int bifat ){
        this.id = id;
        this.sarcina = sarcina;
        this.data=data;
        this.bifat = bifat;

    }

    public int getId() {
        return id;
    }
    public String getSarcina() {
        return sarcina;
    }
    public String getData() {
        return data;
    }
    public int getBifat() {
        return bifat;
    }
    public void setBifat(int bifat) {
        this.bifat = bifat;
    }

}
