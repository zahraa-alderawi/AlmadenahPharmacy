package com.apps.almadenahpharmacy.Adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.almadenahpharmacy.Models.Shift;
import com.apps.almadenahpharmacy.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShiftAdapters extends RecyclerView.Adapter<ShiftAdapters.ShiftViewHolder> {
    ArrayList<Shift> data;
    Activity activity ;


    public ShiftAdapters(ArrayList<Shift> data , Activity activity) {
        this.data = data;
        this.activity = activity;
    }


    @NonNull
    @Override
    public ShiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_shift,parent,false);
        ShiftViewHolder shiftHolder = new ShiftViewHolder(v);
        return shiftHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftViewHolder holder, int position) {
         Shift shift = data.get(position);
        holder.date.setText(shift.getDay()+"/" +shift.getMonth());
        Uri imgUriAttendance = Uri.parse(shift.getImageAttendance());
        Picasso.get().load(imgUriAttendance).into(holder.imageAttendance);
        if (shift.getImageLeave().equals("image2")){
        }
        else {
            Uri imgUriLeave = Uri.parse(shift.getImageLeave());
            Picasso.get().load(imgUriLeave).into(holder.imageLeave);




        }
        holder.shift = shift ;
        holder.po = position ;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

     static class  ShiftViewHolder extends  RecyclerView.ViewHolder{
        TextView date;
        ImageView imageAttendance ;
        ImageView imageLeave ;
        Shift shift ;
        int po;

        public ShiftViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txtShiftDay);
            imageAttendance = itemView.findViewById(R.id.imgShiftAttendance);
            imageLeave = itemView.findViewById(R.id.imgShiftLeave);

        }
    }

}
