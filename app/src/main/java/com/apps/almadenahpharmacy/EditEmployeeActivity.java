package com.apps.almadenahpharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.almadenahpharmacy.Models.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EditEmployeeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final EditText editNameEdit = findViewById(R.id.editNameEdit);
        final EditText editHoursCountEdit = findViewById(R.id.editHoursCountEdit);
        final EditText editDaysCountEdit = findViewById(R.id.editDaysCountEdit);

        final CheckBox checkSaturdayEdit = findViewById(R.id.checkSaturdayEdit);
        final CheckBox checkSundayEdit = findViewById(R.id.checkSundayEdit);
        final CheckBox checkMondayEdit = findViewById(R.id.checkMondayEdit);
        final CheckBox checkTuesdayEdit = findViewById(R.id.checkTuesdayEdit);
        final CheckBox checkWednesdayEdit = findViewById(R.id.checkWednesdayEdit);
        final CheckBox checkThursdayEdit = findViewById(R.id.checkThursdayEdit);

        final Button butEdit = findViewById(R.id.EditBtn);
        Employee employee = (Employee) getIntent().getSerializableExtra("Employee");
        final ArrayList<String> days = employee.getDaysNames();
        final int id = employee.getId();

        editNameEdit.setText(employee.getName());
        editHoursCountEdit.setText(employee.getHoursCount()+"");
        editDaysCountEdit.setText(employee.getDaysCount()+"");

        for (int i = 0 ; i<days.size(); i++){
            if (days.get(i).equals("السبت")) {
                checkSaturdayEdit.setChecked(true);
            } else if (days.get(i).equals("الأحد")) {
                checkSundayEdit.setChecked(true);
            }else if (days.get(i).equals("الاثنين")) {
                checkMondayEdit.setChecked(true);
            }else if (days.get(i).equals("الثلاثاء")) {
                checkTuesdayEdit.setChecked(true);
            }else if (days.get(i).equals("الأربعاء")) {
                checkWednesdayEdit.setChecked(true);
            }else if (days.get(i).equals("الخميس")) {
                checkThursdayEdit.setChecked(true);
            }
        }

        butEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final ArrayList<String> days = new ArrayList<>();
                if (checkSaturdayEdit.isChecked()){days.add("السبت"); }
                if (checkSundayEdit.isChecked()){days.add("الأحد"); }
                if (checkMondayEdit.isChecked()){days.add("الاثنين"); }
                if (checkTuesdayEdit.isChecked()){days.add("الثلاثاء"); }
                if (checkWednesdayEdit.isChecked()){days.add("الأربعاء"); }
                if (checkThursdayEdit.isChecked()){days.add("الخميس"); }

                if (days.size()>0){
                    Employee employee = new Employee(id,editNameEdit.getText().toString(),Integer.parseInt(editHoursCountEdit.getText().toString()),
                            Integer.parseInt(editDaysCountEdit.getText().toString()),days);
                    database.getReference().child("Employees").child(id+"").setValue(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(EditEmployeeActivity.this, "تم إضافة الموظف بنجاح", Toast.LENGTH_SHORT).show();
                        }
                    }) ;
                }
                else Toast.makeText(EditEmployeeActivity.this, "يجب تحديد الأيام التي يداوم فيها الموظف", Toast.LENGTH_SHORT).show();







            }
        });
    }
}