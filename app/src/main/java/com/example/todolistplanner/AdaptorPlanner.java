package com.example.todolistplanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AdaptorPlanner extends RecyclerView.Adapter<AdaptorPlanner.ViewHolder>{
    List<ListaCuSarcini> listaCuSarcini;
    FragmentManager fragmentManager;
    private int itemSelectat = -1;
    public AdaptorPlanner(List<ListaCuSarcini> listaCuSarcini, FragmentManager fragmentManager1) {
        this.listaCuSarcini = listaCuSarcini;
        this.fragmentManager=fragmentManager1;
    }

    @NonNull
    @Override
    public AdaptorPlanner.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemplanner, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptorPlanner.ViewHolder holder, int position) {
        ListaCuSarcini listaSarcini=listaCuSarcini.get(position);
        holder.sarcina.setText(listaSarcini.getSarcina());
        holder.data.setText(listaSarcini.getData());

        holder.btnStergereSarcina.setVisibility(View.GONE);

        if (itemSelectat == position) {
            holder.btnStergereSarcina.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnLongClickListener(v -> {
            itemSelectat = holder.getAdapterPosition();
            notifyDataSetChanged();
            return true;
        });

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(listaSarcini.getBifat() == 1);
        if (listaSarcini.getBifat() == 1) {
            holder.sarcina.setPaintFlags(holder.sarcina.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.sarcina.setPaintFlags(holder.sarcina.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(buttonView.getContext(), "Sarcina a fost finalizată!", Toast.LENGTH_SHORT).show();
                holder.sarcina.setPaintFlags(holder.sarcina.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                listaSarcini.setBifat(1);
            }else {
                holder.sarcina.setPaintFlags(holder.sarcina.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                listaSarcini.setBifat(0);
            }

            SQLiteDatabase proiectLista = SQLiteDatabase.openDatabase("/data/data/com.example.todolistplanner/databases/ProiectLista.db", null, SQLiteDatabase.OPEN_READWRITE);
            proiectLista.execSQL("UPDATE Planner SET Bifat = ? WHERE id = ?", new Object[]{listaSarcini.getBifat(), listaSarcini.getId()});
            proiectLista.close();
        });

        holder.btnStergereSarcina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION)
                {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Confirmare ștergere")
                            .setMessage("Sigur vrei să ștergi această sarcina?")
                            .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    SQLiteDatabase proiectLista = SQLiteDatabase.openDatabase("/data/data/com.example.todolistplanner/databases/ProiectLista.db", null, SQLiteDatabase.OPEN_READWRITE);

                                    proiectLista.delete("Planner", "id=?", new String[]{String.valueOf(listaSarcini.getId())});
                                    proiectLista.close();

                                    listaCuSarcini.remove(currentPosition);
                                    notifyItemRemoved(currentPosition);
                                }
                            })
                            .setNegativeButton("Nu", null)
                            .show();
                }

            }
        });

        holder.itemView.setOnClickListener(v -> {
            ModificarePlanner modificarePlanner = new ModificarePlanner(listaSarcini);
            fragmentManager.beginTransaction()
                    .replace(R.id.frame, modificarePlanner)
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
        return listaCuSarcini.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sarcina,data;
        FloatingActionButton btnStergereSarcina;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sarcina=itemView.findViewById(R.id.tvSarcina);
            data=itemView.findViewById(R.id.tvDataSarcina);
            checkBox=itemView.findViewById(R.id.checkBox);
            btnStergereSarcina=itemView.findViewById(R.id.floatingActionButtonStergereSarcina);
        }
    }


}
