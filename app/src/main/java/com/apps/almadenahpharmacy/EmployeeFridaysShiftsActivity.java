package com.apps.almadenahpharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.apps.almadenahpharmacy.Adapters.FridayShiftAdapter;
import com.apps.almadenahpharmacy.Adapters.ShiftAdapters;
import com.apps.almadenahpharmacy.Models.Shift;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeFridaysShiftsActivity extends AppCompatActivity {
int id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_fridays_shifts);
        RecyclerView recEmployeeFridaysShifts = findViewById(R.id.recEmployeeFridaysShifts);
        final ArrayList<Shift> data = new ArrayList<>();
        id = getIntent().getIntExtra("id",1);
        final FridayShiftAdapter adapter = new FridayShiftAdapter(data,this);
        recEmployeeFridaysShifts.setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("FridaysShifts").child(id+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Shift shift= new Shift();
                        shift.setDay(Integer.parseInt(snapshot.child("day").getValue().toString()));
                        shift.setMonth(Integer.parseInt(snapshot.child("month").getValue().toString()));
                        shift.setTimeStampComing(Long.parseLong(snapshot.child("timeStampComing").getValue().toString()));
                        shift.setTimeStampLeave(Long.parseLong(snapshot.child("timeStampLeave").getValue().toString()));
                        shift.setImageLeave(snapshot.child("imageLeave").getValue().toString());
                        shift.setImageComing(snapshot.child("imageComing").getValue().toString());
                        shift.setShiftPrice(Double.parseDouble(snapshot.child("shiftPrice").getValue().toString()));
                        data.add(shift);

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;
        recEmployeeFridaysShifts.setLayoutManager(new LinearLayoutManager(EmployeeFridaysShiftsActivity.this));
        recEmployeeFridaysShifts.setHasFixedSize(true);

    }
}