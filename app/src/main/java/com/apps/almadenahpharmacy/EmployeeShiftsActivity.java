package com.apps.almadenahpharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.apps.almadenahpharmacy.Adapters.ShiftAdapters;
import com.apps.almadenahpharmacy.Models.Shift;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeShiftsActivity extends AppCompatActivity {
    int monthInt ;
    int id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_shifts);
        RecyclerView recEmpShifts = findViewById(R.id.recEmpShifts);
        final ArrayList<Shift> data = new ArrayList<>();
         id = getIntent().getIntExtra("id",1);
         monthInt = getIntent().getIntExtra("month",1);
         if (monthInt==0){
             monthInt = 12 ;
         }
            final ShiftAdapters adapter = new ShiftAdapters(data,this);
         recEmpShifts.setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
         database.getReference().child("Shifts").child(id+"").addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 data.clear();
                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     if (Integer.parseInt(snapshot.child("month").getValue().toString())==monthInt){
                         Shift shift= new Shift();
                         shift.setDay(Integer.parseInt(snapshot.child("day").getValue().toString()));
                         shift.setMonth(Integer.parseInt(snapshot.child("month").getValue().toString()));
                         shift.setTimeStampComing(Long.parseLong(snapshot.child("timeStampComing").getValue().toString()));
                         shift.setTimeStampLeave(Long.parseLong(snapshot.child("timeStampLeave").getValue().toString()));
                         shift.setImageLeave(snapshot.child("imageLeave").getValue().toString());
                         shift.setImageComing(snapshot.child("imageComing").getValue().toString());
                         data.add(shift);

                     }

                 }
                 adapter.notifyDataSetChanged();

             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         }) ;
        recEmpShifts.setLayoutManager(new LinearLayoutManager(EmployeeShiftsActivity.this));
        recEmpShifts.setHasFixedSize(true);

    }
}