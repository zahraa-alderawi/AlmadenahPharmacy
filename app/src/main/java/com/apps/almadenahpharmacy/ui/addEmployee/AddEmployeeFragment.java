package com.apps.almadenahpharmacy.ui.addEmployee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.apps.almadenahpharmacy.Day;
import com.apps.almadenahpharmacy.Employee;
import com.apps.almadenahpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddEmployeeFragment extends Fragment {

    private AddEmployeeViewModel galleryViewModel;
    FirebaseDatabase database;
    private TextInputEditText editNameAdd;
    private TextInputEditText editHoursCountAdd;
    private TextInputEditText editDaysCountAdd;
    private CheckBox checkSaturday;
    private CheckBox checkSunday;
    private CheckBox checkMonday;
    private CheckBox checkTuesday;
    private CheckBox checkWednesday;
    private CheckBox checkThursday;
    private MaterialButton addNewBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(AddEmployeeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_employee, container, false);
        editNameAdd = root.findViewById(R.id.editNameAdd);
        editHoursCountAdd = root.findViewById(R.id.editHoursCountAdd);
        editDaysCountAdd = root.findViewById(R.id.editDaysCountAdd);
        checkSaturday = root.findViewById(R.id.checkSaturday);
        checkSunday = root.findViewById(R.id.checkSunday);
        checkMonday = root.findViewById(R.id.checkMonday);
        checkTuesday = root.findViewById(R.id.checkTuesday);
        checkWednesday = root.findViewById(R.id.checkWednesday);
        checkThursday = root.findViewById(R.id.checkThursday);
        addNewBtn = root.findViewById(R.id.addNewBtn);
        database = FirebaseDatabase.getInstance();

        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Day> days = new ArrayList<>();
                if (checkSaturday.isChecked()){days.add(new Day("السبت")); }
                 if (checkSunday.isChecked()){days.add(new Day("الأحد")); }
                 if (checkMonday.isChecked()){days.add(new Day("الاثنين")); }
                 if (checkTuesday.isChecked()){days.add(new Day("الثلاثاء")); }
                 if (checkWednesday.isChecked()){days.add(new Day("الأربعاء")); }
                 if (checkThursday.isChecked()){days.add(new Day("الخميس")); }

                    if (days.size()>0){
                        Employee employee = new Employee(editNameAdd.getText().toString(),Integer.parseInt(editHoursCountAdd.getText().toString()),
                                Integer.parseInt(editDaysCountAdd.getText().toString()),days);
                        database.getReference().child("Employees").push().setValue(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "تم إضافة الموظف بنجاح", Toast.LENGTH_SHORT).show();
                            }
                        }) ;
                    }
                    else Toast.makeText(getActivity(), "يجب تحديد الأيام التي يداوم فيها الموظف", Toast.LENGTH_SHORT).show();





            }
        });

        return root;
        
    }
}