package com.example.todolistplanner;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Locale;


public class ModificarePlanner extends Fragment {
    SQLiteDatabase ProiectLista;
    EditText sarcina, data;
    FloatingActionButton btnSalvareSarcina;
    ListaCuSarcini osarcina;

    public ModificarePlanner(ListaCuSarcini osarcina) {
        this.osarcina = osarcina;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_modificare_planner, container, false);
        sarcina=v.findViewById(R.id.etSarcina1);
        data=v.findViewById(R.id.etDataSarcina1);
        btnSalvareSarcina=v.findViewById(R.id.floatingActionButtonSalvareSarcina1);
        ProiectLista=Conectare();

        sarcina.setText(osarcina.getSarcina());
        data.setText(osarcina.getData());

        data.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int an = calendar.get(Calendar.YEAR);
                int luna = calendar.get(Calendar.MONTH);
                int zi = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        (view, anulSelectat, lunaSelectata, ziuaSelectata) -> {
                            Calendar selectedDate = Calendar.getInstance();
                            selectedDate.set(anulSelectat, lunaSelectata, ziuaSelectata);

                            Calendar currentDate = Calendar.getInstance();

                            if (selectedDate.before(currentDate)) {
                                Toast.makeText(getContext(), "Te rog să selectezi o dată viitoare!", Toast.LENGTH_SHORT).show();
                            } else {
                                String formattedDate = String.format(Locale.getDefault(), "%02d.%02d.%04d", ziuaSelectata, lunaSelectata + 1, anulSelectat);
                                data.setText(formattedDate);
                            }
                        }, an, luna, zi);
                datePickerDialog.show();
                return false;
            }
        });

        btnSalvareSarcina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sarcina.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Te rog să introduci o sarcină!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (data.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Te rog să selectezi o dată!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put("Sarcina", sarcina.getText().toString());
                contentValues.put("Data", data.getText().toString());

                ProiectLista.update("Planner", contentValues, "id=?", new String[]{String.valueOf(osarcina.getId())});

                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, new AfisarePlanner())
                        .addToBackStack(null)
                        .commit();

                Toast.makeText(getContext(), "Sarcina actualizată!!", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    SQLiteDatabase Conectare(){
        return SQLiteDatabase.openDatabase("/data/data/com.example.todolistplanner/databases/ProiectLista.db",
                null, SQLiteDatabase.OPEN_READWRITE);

    }
}