package com.example.forgecareer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.forgecareer.db.Application;
import com.example.forgecareer.recyclecViews.ApplicationAdapter;
import com.example.forgecareer.utils.ApplicationSorter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static List<String> keyList;
    public static Map<String, Application> applicationMap;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    FloatingActionButton fab;

    String TAG = "CompanyFragment";
    String userID;
    private DatabaseReference databaseReference;
    private ArrayList<Map.Entry<String, Application>> sortedEntries;
    ApplicationAdapter applicationAdapter;



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
                keyList = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    keyList.add(snap.getKey());
                    Log.d(TAG, "onDataChange -> companyName = " + snap.getKey().toString());
                }
                applicationMap = updateMapFromSnapshot(snapshot);
                MainActivity.applicationMap = applicationMap;
//                for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
//                    Log.d(TAG, entry.getValue().getCompanyName() + " : " +entry.getValue().getCreateDate());
//                }
                ApplicationSorter applicationSorter = new ApplicationSorter(applicationMap);
                sortedEntries = applicationSorter.sortByCreateDate();
                for (Map.Entry<String, Application> entry : sortedEntries) {
                    Log.d(TAG, entry.getValue().getCompanyName() + " : " +entry.getValue().getCreateDate());
                }

                applicationAdapter = new ApplicationAdapter(sortedEntries);
                recyclerView.setAdapter(applicationAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled");
            }
        });
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        return view;
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();


            String applicationKey = applicationAdapter.applicationEntries.get(position).getKey();
            Toast.makeText(getContext(), "removing item at position: " + position, Toast.LENGTH_SHORT).show();

            FirebaseDatabase db = FirebaseDatabase.getInstance();

            DatabaseReference deleteDatabaseReference = db.getReference(Application.class.getSimpleName()+ "/" +userID + "/" + applicationKey);;
            deleteDatabaseReference.removeValue();
            applicationAdapter.notifyDataSetChanged();
        }
    };

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
            String createDate = snap.child("createDate").getValue(String.class);
            String updateDate = snap.child("updateDate").getValue(String.class);
            Log.d(TAG, createDate);
            applicationList.add(new Application(companyName, jobType, positionType, startDate, referrer, status, applicationDate, priority, interviewDate, notes, createDate, updateDate));
        }
        return applicationList;
    }
    private Map<String, Application> updateMapFromSnapshot(DataSnapshot snapshot) {
        List<Application> applicationList = new ArrayList<>();
        Map<String, Application> applicationMap = new HashMap<>();
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
            String createDate = snap.child("createDate").getValue(String.class);
            String updateDate = snap.child("updateDate").getValue(String.class);
            Application application = new Application(companyName, jobType, positionType, startDate, referrer, status, applicationDate, priority, interviewDate, notes, createDate, updateDate);
            applicationList.add(application);

            String applicationKey = snap.getKey();
            applicationMap.put(applicationKey, application);
        }
        return applicationMap;

    }

}