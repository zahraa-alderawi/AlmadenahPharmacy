package com.apps.almadenahpharmacy.ui.addEmployee;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.apps.almadenahpharmacy.Models.Employee;
import com.apps.almadenahpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddEmployeeFragment extends Fragment {

    private AddEmployeeViewModel galleryViewModel;
    FirebaseDatabase database;
    private TextInputEditText editNameAdd;
    private TextInputEditText editHoursCountAdd;
    private TextInputEditText editDaysCountAdd;
    private TextInputEditText editExtraHourPriceAdd;
    private CheckBox checkSaturday;
    private CheckBox checkSunday;
    private CheckBox checkMonday;
    private CheckBox checkTuesday;
    private CheckBox checkWednesday;
    private CheckBox checkThursday;
    private RadioGroup radioGroupAdd;
    private RadioButton genderRadioButton;
    private MaterialButton addNewBtn;
    int lastKey ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(AddEmployeeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_add_employee, container, false);
        lastKey=0;
        editNameAdd = root.findViewById(R.id.editNameAdd);
        editHoursCountAdd = root.findViewById(R.id.editHoursCountAdd);
        editDaysCountAdd = root.findViewById(R.id.editDaysCountAdd);
        editExtraHourPriceAdd = root.findViewById(R.id.editExtraHourPriceAdd);
        checkSaturday = root.findViewById(R.id.checkSaturday);
        checkSunday = root.findViewById(R.id.checkSunday);
        checkMonday = root.findViewById(R.id.checkMonday);
        checkTuesday = root.findViewById(R.id.checkTuesday);
        checkWednesday = root.findViewById(R.id.checkWednesday);
        checkThursday = root.findViewById(R.id.checkThursday);
        radioGroupAdd = (RadioGroup) root.findViewById(R.id.radioGroupAdd);

        addNewBtn = root.findViewById(R.id.addNewBtn);
        database = FirebaseDatabase.getInstance();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final Query lastQuery = reference.child("Employees").orderByKey().limitToLast(1);
        lastQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                     lastKey = Integer.parseInt(  snap.getKey()+"");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<String> days = new ArrayList<>();
                int selectedId = radioGroupAdd.getCheckedRadioButtonId();
                genderRadioButton =  root.findViewById(selectedId);

                if (checkSaturday.isChecked()) {
                    days.add("السبت");
                }
                if (checkSunday.isChecked()) {
                    days.add("الأحد");
                }
                if (checkMonday.isChecked()) {
                    days.add("الاثنين");
                }
                if (checkTuesday.isChecked()) {
                    days.add("الثلاثاء");
                }
                if (checkWednesday.isChecked()) {
                    days.add("الأربعاء");
                }
                if (checkThursday.isChecked()) {
                    days.add("الخميس");
                }

                if (days.size() > 0) {
                    Employee employee = new Employee(lastKey + 1,
                            editNameAdd.getText().toString(),
                            Integer.parseInt(editHoursCountAdd.getText().toString()),
                            Integer.parseInt(editDaysCountAdd.getText().toString()),
                            days,
                            genderRadioButton.getText().toString(),
                            Integer.parseInt(editExtraHourPriceAdd.getText().toString()));

                    database.getReference().child("Employees").child((lastKey + 1) + "").setValue(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "تم إضافة الموظف بنجاح", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    Toast.makeText(getActivity(), "يجب تحديد الأيام التي يداوم فيها الموظف", Toast.LENGTH_SHORT).show();


            }
        });
        return root;

    }


}