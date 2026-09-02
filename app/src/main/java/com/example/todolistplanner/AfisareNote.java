package com.example.todolistplanner;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AfisareNote extends Fragment {
    RecyclerView recyclerView;
    Adaptor adaptor;
    SQLiteDatabase ProiectLista;
    FloatingActionButton btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_afisare_note,container,false);
        recyclerView=v.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btn=v.findViewById(R.id.floatingActionButtonAdaugare);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, new Note())
                        .addToBackStack(null)
                        .commit();
            }
        });

        ProiectLista=Conectare();
        List<ListaCuNote> listaNote= IncarcareNote();
        adaptor=new Adaptor(listaNote, getParentFragmentManager());
        recyclerView.setAdapter(adaptor);
        return v;
    }
    private List<ListaCuNote> IncarcareNote(){
        List<ListaCuNote> lista=new ArrayList<>();
        Cursor cursor = ProiectLista.rawQuery("select * from Note", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String titlu = cursor.getString(cursor.getColumnIndexOrThrow("Titlul"));
                String data= cursor.getString(cursor.getColumnIndexOrThrow("DataC"));
                String continut = cursor.getString(cursor.getColumnIndexOrThrow("Continut"));

                lista.add(new ListaCuNote(id, titlu, data,continut));
                Log.d("AfisareNote", "id: " + id + ", Titlu: " + titlu  + ", DataC: " + data + ", Continut: " + continut);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;

    }

    SQLiteDatabase Conectare() {
        return SQLiteDatabase.openDatabase("/data/data/com.example.todolistplanner/databases/ProiectLista.db",
                null, SQLiteDatabase.OPEN_READWRITE);
    }

}