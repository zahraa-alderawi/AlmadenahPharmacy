package com.apps.almadenahpharmacy.Adapters;

import android.app.Activity;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.almadenahpharmacy.Models.Shift;
import com.apps.almadenahpharmacy.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public  class FridayShiftAdapter extends RecyclerView.Adapter<FridayShiftAdapter.FridayShiftViewHolder> {
    ArrayList<Shift> data;
    Activity activity ;


    public FridayShiftAdapter(ArrayList<Shift> data , Activity activity) {
        this.data = data;
        this.activity = activity;
    }


    @NonNull
    @Override
    public FridayShiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_friday_shift_details,parent,false);
        FridayShiftViewHolder shiftHolder = new FridayShiftViewHolder(v);
        return shiftHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FridayShiftViewHolder holder, int position) {
        Shift shift = data.get(position);
        holder.date.setText(shift.getDay()+"/" +shift.getMonth());
        if (shift.getImageComing().equals("")){
            holder.imageAttendance.setImageResource(R.drawable.ph);
        }
        else {
            Uri imgUriAttendance = Uri.parse(shift.getImageComing());
            Picasso.get().load(imgUriAttendance).into(holder.imageAttendance);

        }
        if (shift.getImageLeave().equals("")){
            holder.imageLeave.setImageResource(R.drawable.ph);
        }
        else {
            Uri imgUriLeave = Uri.parse(shift.getImageLeave());
            Picasso.get().load(imgUriLeave).into(holder.imageLeave);

        }
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date currentTimeZone=new Date(shift.getTimeStampComing());
        Date currentTimeZone2=new Date(shift.getTimeStampLeave());
        String t  =sdf.format(currentTimeZone) ;
        String t2  =sdf.format(currentTimeZone2) ;


        holder.timeComing.setText(t);
        if (shift.getTimeStampLeave()!=0){
            holder.timeLeave.setText(t2);

        }
        holder.txtShiftPriceFriday.setText("الراتب المخصص لهذا اليوم: "+shift.getShiftPrice() +" شيكل");
        holder.shift = shift ;
        holder.po = position ;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

     static class  FridayShiftViewHolder extends  RecyclerView.ViewHolder{
        TextView date;
        ImageView imageAttendance ;
        ImageView imageLeave ;
        TextView timeComing;
        TextView timeLeave;
        TextView txtShiftPriceFriday;
        Shift shift ;
        int po;

        public FridayShiftViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txtShiftDayFriday);
            imageAttendance = itemView.findViewById(R.id.imgShiftAttendanceFriday);
            imageLeave = itemView.findViewById(R.id.imgShiftLeaveFriday);
            timeComing=itemView.findViewById(R.id.timeShiftAttendanceFriday);
            timeLeave=itemView.findViewById(R.id.timeShiftLeaveFriday);
            txtShiftPriceFriday=itemView.findViewById(R.id.txtShiftPriceFriday);

        }
    }

}
