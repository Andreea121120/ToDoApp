package com.example.todolistplanner;

import java.io.Serializable;

public class ListaCuNote implements Serializable {
    private int id;
    private String titlu;
    private String continut;
    private String data;

    public String getRezumat() {
        String[] randuri = continut.split("\n");
        StringBuilder rezumat = new StringBuilder();

        for (int i = 0; i < Math.min(1, randuri.length); i++) {
            rezumat.append(randuri[i]).append("\n");
        }

        return rezumat.toString().trim();
    }

    public ListaCuNote(int id, String titlu, String data,String continut ){
        this.id = id;
        this.titlu = titlu;
        this.data=data;
        this.continut = continut;

    }

    public int getId() {
        return id;
    }
    public String getTitlu() {
        return titlu;
    }
    public String getData() {
        return data;
    }
    public String getContinut() {
        return continut;
    }
}
