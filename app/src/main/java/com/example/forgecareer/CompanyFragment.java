package com.example.forgecareer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.forgecareer.db.Application;
import com.example.forgecareer.recyclecViews.ApplicationAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static List<Application> applicationList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    FloatingActionButton fab;

    String TAG = "CompanyFragment";
    String userID;
    private DatabaseReference databaseReference;



    public CompanyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompanyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompanyFragment newInstance(String param1, String param2) {
        CompanyFragment fragment = new CompanyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        if (recyclerView == null) {
            Log.d("list", "null!!!");
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        

        fab = view.findViewById(R.id.addApplicationFAB);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddApplicationActivity.class);
            startActivity(intent);
        });

        userID = LoginActivity.userID;
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Application.class.getSimpleName()+ "/" +userID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                applicationList = updateFromSnapshot(snapshot);
                ApplicationAdapter applicationAdapter = new ApplicationAdapter(applicationList);
                recyclerView.setAdapter(applicationAdapter);
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Log.d(TAG, "onDataChange -> companyName = " + snap.child("companyName").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled");
            }
        });


        return view;
    }

    private List<Application> updateFromSnapshot(DataSnapshot snapshot) {
        List<Application> applicationList = new ArrayList<>();
        for (DataSnapshot snap : snapshot.getChildren()) {
            String companyName = snap.child("companyName").getValue(String.class);
            String jobType = snap.child("jobType").getValue(String.class);
            String positionType = snap.child("positionType").getValue(String.class);
            String referrer = snap.child("referer").getValue(String.class);
            String status = snap.child("status").getValue(String.class);
            String applicationDate = snap.child("applicationDate").getValue(String.class);
            String priority = snap.child("priority").getValue(String.class);
            String interviewDate = snap.child("interviewDate").getValue(String.class);
            String notes = snap.child("notes").getValue(String.class);
            String startDate = snap.child("startDate").getValue(String.class);
            applicationList.add(new Application(companyName, jobType, positionType, startDate, referrer, status, applicationDate, priority, interviewDate, notes));
        }
        return applicationList;
    }


    private void initApplicationData() {
        applicationList = new ArrayList<>();
        applicationList.add(new Application("Amazon", "Intern", "SWE", "Summer23", "Nick", "Applied", "09/01/2022", "Ultra", "N/A", "this is the note for Amazon application"));
        applicationList.add(new Application("Google", "FullTime", "SDE", "Summer23", "Tom", "Interview", "09/02/2022", "High", "10/05/2022", "this is the note for google application"));
        applicationList.add(new Application("Waymo", "Intern", "FullStack", "Summer23", "Kshitiz", "Interested", "N/A", "High", "N/A", "This is the note for waymo application test row, trying to make it longer so that it can be a little bit different from the other two rows."));
        applicationList.add(new Application("Meta", "Intern", "SWE", "Summer23", "Ouyang", "Applied", "08/01/2022", "Ultra", "N/A", "This is a note for the meta application"));
        applicationList.add(new Application("Uber", "FullTime", "SDE", "Summer23", "N/A", "OA", "08/05/2022", "High", "N/A", "This is a note for uber page"));
        applicationList.add(new Application("Netflix", "FullTime", "PM", "Summer23", "N/A", "Rejected", "08/02/2022", "Medium", "N/A", "This is a note for netflix page, although it is rejected here, I still hope I can get a job in netflix"));
    }
}