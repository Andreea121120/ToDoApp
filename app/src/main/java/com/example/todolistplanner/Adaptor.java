package com.example.todolistplanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class Adaptor extends RecyclerView.Adapter<Adaptor.ViewHolder> {
    List <ListaCuNote> listaCuNote;
    private int itemSelectat = -1;
    FragmentManager fragmentManager;
    public Adaptor(List<ListaCuNote> listaCuNote,FragmentManager fragmentManager1) {
        this.listaCuNote = listaCuNote;
        this.fragmentManager = fragmentManager1;
    }
    @NonNull
    @Override
    public Adaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull Adaptor.ViewHolder holder, int position) {
        ListaCuNote listaNote=listaCuNote.get(position);
        holder.titlu.setText(listaNote.getTitlu());
        holder.data.setText(listaNote.getData());
        holder.continut.setText(listaNote.getRezumat());

        holder.btnStergere.setVisibility(View.GONE);

        if (itemSelectat == position) {
            holder.btnStergere.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnLongClickListener(v -> {
            itemSelectat = holder.getAdapterPosition();
            notifyDataSetChanged();
            return true;
        });

        holder.btnStergere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION)
                {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Confirmare ștergere")
                            .setMessage("Sigur vrei să ștergi această notă?")
                            .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    SQLiteDatabase proiectLista = SQLiteDatabase.openDatabase("/data/data/com.example.todolistplanner/databases/ProiectLista.db", null, SQLiteDatabase.OPEN_READWRITE);

                                    proiectLista.delete("Note", "id=?", new String[]{String.valueOf(listaNote.getId())});
                                    proiectLista.close();

                                    listaCuNote.remove(currentPosition);
                                    notifyItemRemoved(currentPosition);
                                }
                            })
                            .setNegativeButton("Nu", null)
                            .show();
                    }
                }
        });

        holder.itemView.setOnClickListener(v -> {
            ModificareNote modificareNote=new ModificareNote(listaNote);
            fragmentManager.beginTransaction()
                    .replace(R.id.frame, modificareNote)
                    .addToBackStack(null)
                    .commit();

            if (itemSelectat == position) {
                itemSelectat = -1;
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaCuNote.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titlu, continut, data;
        FloatingActionButton btnStergere;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titlu=itemView.findViewById(R.id.tvTitlu);
            continut=itemView.findViewById(R.id.tvContinut);
            data=itemView.findViewById(R.id.tvData);
            btnStergere=itemView.findViewById(R.id.floatingActionButtonStergere);

        }
    }
}
