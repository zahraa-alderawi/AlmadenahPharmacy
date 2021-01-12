package com.apps.almadenahpharmacy.Adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.almadenahpharmacy.Models.Employee;
import com.apps.almadenahpharmacy.Models.Shift;
import com.apps.almadenahpharmacy.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShiftAdapter extends BaseAdapter {
    ArrayList<Shift> data;
    Activity activity;

    public ShiftAdapter(ArrayList<Shift> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View v = LayoutInflater.from(activity).inflate(R.layout.row_shift, null, false);
        TextView txtShiftDay = v.findViewById(R.id.txtShiftDay);
        ImageView imgShiftAttendance =  v.findViewById(R.id.imgShiftAttendance);
        ImageView imgShiftLeave =  v.findViewById(R.id.imgShiftLeave);
        txtShiftDay.setText(data.get(i).getDay()+"/" + data.get(i).getMonth());
        Uri imgUriAttendance = Uri.parse(data.get(i).getImageAttendance());
        Picasso.get().load(imgUriAttendance).into(imgShiftAttendance);
        if (data.get(i).getImageLeave().equals("image2")){
        }
        else {
            Uri imgUriLeave = Uri.parse(data.get(i).getImageLeave());
            Picasso.get().load(imgUriLeave).into(imgShiftLeave);
        }
        return v;
    }
}
