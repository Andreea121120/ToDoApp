package com.example.todolistplanner;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Note extends Fragment {
    EditText titlu, continut, data;
    FloatingActionButton btnSalvare;
    SQLiteDatabase ProiectLista;

    String dataCurenta = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            .format(Calendar.getInstance().getTime());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_note, container, false);

        titlu = v.findViewById(R.id.etTitlu);
        continut = v.findViewById(R.id.etContinut);
        data=v.findViewById(R.id.etData);
        btnSalvare = v.findViewById(R.id.floatingActionButtonSalvare);
        ProiectLista=Conectare();

        data.setText(dataCurenta);

        btnSalvare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues contentValues=new ContentValues();
                contentValues.put("Titlul", titlu.getText().toString());
                contentValues.put("DataC", dataCurenta);
                contentValues.put("Continut", continut.getText().toString());

                ProiectLista.insert("Note", null, contentValues);

                getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame, new AfisareNote())
                            .addToBackStack(null)
                            .commit();

                Toast.makeText(getContext(), "Nota salvata!!", Toast.LENGTH_SHORT).show();

            }
        });
        return v;
    }
    SQLiteDatabase Conectare(){
        return SQLiteDatabase.openDatabase("/data/data/com.example.todolistplanner/databases/ProiectLista.db",
                null, SQLiteDatabase.OPEN_READWRITE);

    }
}