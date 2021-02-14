package com.apps.almadenahpharmacy.ui.showFridays;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.apps.almadenahpharmacy.Adapters.FridayEmployeeAdapter;
import com.apps.almadenahpharmacy.Adapters.HomeFridaysRegisterAdapter;
import com.apps.almadenahpharmacy.MainActivity;
import com.apps.almadenahpharmacy.Models.Employee;
import com.apps.almadenahpharmacy.OnIntentReceived;
import com.apps.almadenahpharmacy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ShowFridaysFragment extends Fragment  {

    private ShowFridaysViewModel homeViewModel;
    Uri uri;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    String currentPhotoPath;
    StorageReference storageReference;
    int empId ;
    String shiftKey ;
    String shiftImageType;
    View dialogView ;
    AlertDialog.Builder builder;
    AlertDialog alertDialog ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(ShowFridaysViewModel.class);
        View root = inflater.inflate(R.layout.fragment_show_fridays, container, false);

        ListView listEmpFridays = root.findViewById(R.id.listEmpFridays);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<Integer> data = new ArrayList<>();
        final FridayEmployeeAdapter adapter = new FridayEmployeeAdapter(getActivity(),data);
        listEmpFridays.setAdapter(adapter);
        database.getReference().child("FridaysShifts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    int id = Integer.parseInt(snapshot.getKey());
                    data.add(id);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return root;

    }



}