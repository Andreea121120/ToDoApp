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


public class AfisarePlanner extends Fragment {
    RecyclerView recyclerView;
    AdaptorPlanner adaptorPlanner;
    SQLiteDatabase ProiectLista;
    FloatingActionButton btnAdaugareSarcina;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_afisare_planner,container,false);
        recyclerView=v.findViewById(R.id.rvPlanner);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnAdaugareSarcina=v.findViewById(R.id.floatingActionButtonAdaugareSarcina);

        btnAdaugareSarcina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, new Planner())
                        .addToBackStack(null)
                        .commit();
            }
        });

        ProiectLista=Conectare();
        List<ListaCuSarcini> listaSarcini= IncarcareSarcini();
        adaptorPlanner=new AdaptorPlanner(listaSarcini, getParentFragmentManager());
        recyclerView.setAdapter(adaptorPlanner);

        return v;
    }

    private List<ListaCuSarcini> IncarcareSarcini() {
        List<ListaCuSarcini> lista = new ArrayList<>(); // Change to ListaCuSarcini
        Cursor cursor = ProiectLista.rawQuery("select * from Planner", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String sarcina = cursor.getString(cursor.getColumnIndexOrThrow("Sarcina"));
                String data = cursor.getString(cursor.getColumnIndexOrThrow("Data"));
                int bifat = cursor.getInt(cursor.getColumnIndexOrThrow("Bifat"));

                lista.add(new ListaCuSarcini(id, sarcina, data, bifat));
                Log.d("AfisareSarcini", "id: " + id + ", Sarcina: " + sarcina + ", Data: " + data + ", Bifat: " + bifat);
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