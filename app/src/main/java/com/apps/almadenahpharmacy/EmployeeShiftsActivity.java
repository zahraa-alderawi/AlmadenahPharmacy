package com.apps.almadenahpharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.apps.almadenahpharmacy.Adapters.ShiftAdapter;
import com.apps.almadenahpharmacy.Models.Employee;
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
        ListView listEmpShifts = findViewById(R.id.listEmpShifts);
        final ArrayList<Shift> data = new ArrayList<>();
         id = getIntent().getIntExtra("id",1);
         monthInt = getIntent().getIntExtra("month",1);
         if (monthInt==0){
             monthInt = 12 ;
         }
        final ShiftAdapter adapter= new ShiftAdapter(data,this);
         listEmpShifts.setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Toast.makeText(this, monthInt+"", Toast.LENGTH_SHORT).show();
         database.getReference().child("Shifts").child(id+"").child(monthInt+"").addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 data.clear();
                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     Shift shift= new Shift();
                     shift.setDay(Integer.parseInt(snapshot.child("day").getValue().toString()));
                     shift.setMonth(Integer.parseInt(snapshot.child("month").getValue().toString()));
                     shift.setImageAttendance(snapshot.child("imageAttendance").getValue().toString());
                     shift.setImageLeave(snapshot.child("imageLeave").getValue().toString());
                     Toast.makeText(EmployeeShiftsActivity.this, data.size()+"", Toast.LENGTH_SHORT).show();

                     data.add(shift);

                 }
                 adapter.notifyDataSetChanged();

             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         }) ;

    }
}