package com.apps.almadenahpharmacy.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.apps.almadenahpharmacy.EmployeeFridaysShiftsActivity;
import com.apps.almadenahpharmacy.Models.Shift;
import com.apps.almadenahpharmacy.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FridayEmployeeAdapter extends BaseAdapter {
    Activity activity ;
    ArrayList<Integer> data ;

    public FridayEmployeeAdapter(Activity activity, ArrayList<Integer> data) {
        this.activity = activity;
        this.data = data;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final View v = LayoutInflater.from(activity).inflate(R.layout.row_friday_shift_employee, null, false);
        final TextView empName = v.findViewById(R.id.employeeNameFriday);
        MaterialButton btnShow = v.findViewById(R.id.btnShowEmployeeShiftsFriday);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("Employees").child(data.get(i)+"").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                empName.setText(name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       btnShow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(activity, EmployeeFridaysShiftsActivity.class);
               intent.putExtra("id",data.get(i));
               activity.startActivity(intent);
           }
       });
        return v;
    }
}
