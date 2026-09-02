package com.example.todolistplanner;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;


public class ModificareNote extends Fragment {
    SQLiteDatabase ProiectLista;
    EditText titlu, continut, data;
    FloatingActionButton btnSalvare;
    ListaCuNote nota;

    public ModificareNote(ListaCuNote nota) {
        this.nota = nota;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_modificare_note, container, false);
        titlu = v.findViewById(R.id.etTitlu1);
        continut = v.findViewById(R.id.etContinut1);
        data = v.findViewById(R.id.etData1);
        btnSalvare = v.findViewById(R.id.floatingActionButtonSalvare1);
        ProiectLista = Conectare();

        titlu.setText(nota.getTitlu());
        continut.setText(nota.getContinut());
        data.setText(nota.getData());

        btnSalvare.setOnClickListener(view -> {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Titlul", titlu.getText().toString());
            contentValues.put("DataC", data.getText().toString());
            contentValues.put("Continut", continut.getText().toString());

            ProiectLista.update("Note", contentValues, "id=?", new String[]{String.valueOf(nota.getId())});

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, new AfisareNote())
                    .addToBackStack(null)
                    .commit();

            Toast.makeText(getContext(), "Nota actualizată!", Toast.LENGTH_SHORT).show();
        });
        return v;
    }
    SQLiteDatabase Conectare(){
        return SQLiteDatabase.openDatabase("/data/data/com.example.todolistplanner/databases/ProiectLista.db",
                null, SQLiteDatabase.OPEN_READWRITE);

    }
}