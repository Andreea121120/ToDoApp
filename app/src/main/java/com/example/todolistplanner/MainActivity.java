package com.example.todolistplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;

import androidx.appcompat.widget.Toolbar;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar;
        toolbar=findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.meniu);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if (item.getItemId() == R.id.lista) {
                    Log.d("FragmentTransaction", "Incarc fragmentul AfisareNote");
                    transaction.replace(R.id.frame, new AfisareNote());
                } else if (item.getItemId() == R.id.planner) {
                    Log.d("FragmentTransaction", "Incarc fragmentul AfisarePlanner");
                    transaction.replace(R.id.frame, new AfisarePlanner());
                }

                transaction.addToBackStack(null);
                transaction.commit();

                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mmenuInflater=getMenuInflater();
        mmenuInflater.inflate(R.menu.meniu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}