package com.forgetech.forgecareer;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.forgetech.forgecareer.R;
import com.forgetech.forgecareer.db.Application;
import com.forgetech.forgecareer.recyclecViews.ApplicationAdapter;
import com.forgetech.forgecareer.utils.ApplicationSorter;
import com.forgetech.forgecareer.utils.Constants;
import com.forgetech.forgecareer.utils.Filter;
import com.forgetech.forgecareer.utils.Utilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
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


    public static List<Application> applicationList;
    public static List<String> keyList;
    public static Map<String, Application> applicationMap;




    RecyclerView recyclerView;
    FloatingActionButton fab;
    TextView searchTextView;

    String TAG = "CompanyFragment";
    String userID;
    private DatabaseReference databaseReference;
    private ArrayList<Map.Entry<String, Application>> sortedEntries;

    Button sortButton;
    Button filterButton;
    ApplicationAdapter applicationAdapter;

    final String DEFAULT_SORT_OPTION = "Create Date";
    final String DEFAULT_FILTER_OPTION = "All";

    String sortBy = DEFAULT_SORT_OPTION;
    String filterBy = DEFAULT_FILTER_OPTION;



    public CompanyFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CompanyFragment newInstance(String param1, String param2) {
        CompanyFragment fragment = new CompanyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        searchTextView = view.findViewById(R.id.searchEditTextView);
        searchTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateRecyclerView();
                Log.d(TAG, "onTextChanged");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged");
            }
        });

        sortButton = view.findViewById(R.id.sortButton);
        filterButton = view.findViewById(R.id.filterButton);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortDialog(sortBy);
            }
        });
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog(filterBy);
            }
        });




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
                Map<String, Application> applicationMapFiltered = Filter.filterByLoseSearch(applicationMap, searchTextView.getText().toString());
                MainActivity.applicationMap = applicationMap;
//                for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
//                    Log.d(TAG, entry.getValue().getCompanyName() + " : " +entry.getValue().getCreateDate());
//                }

                ApplicationSorter applicationSorter = new ApplicationSorter(applicationMapFiltered);
                sortedEntries = applicationSorter.sortByCreateDate();
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


    private void showSortDialog(String sortOption) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_sort);

        RadioButton sortSelection1 = dialog.findViewById(R.id.sortSelection1);
        RadioButton sortSelection2 = dialog.findViewById(R.id.sortSelection2);
        RadioButton sortSelection3 = dialog.findViewById(R.id.sortSelection3);
        RadioButton sortSelection4 = dialog.findViewById(R.id.sortSelection4);
        RadioButton sortSelection5 = dialog.findViewById(R.id.sortSelection5);
        RadioButton sortSelection6 = dialog.findViewById(R.id.sortSelection6);
        ArrayList<RadioButton> selections = new ArrayList<>();
        selections.add(sortSelection1);
        selections.add(sortSelection2);
        selections.add(sortSelection3);
        selections.add(sortSelection4);
        selections.add(sortSelection5);
        selections.add(sortSelection6);
        Log.d(TAG, "sortOption: " + sortOption);
        int sortPosition = -1;
        for (int i = 0; i < Constants.SORT_OPTIONS.length; i++) {
            if (sortOption.equals(Constants.SORT_OPTIONS[i])) {
                sortPosition = i;
            }
        }
        Log.d(TAG, "sortPosition: " + sortPosition);
        if (sortPosition >= 0) {
            selections.get(sortPosition).setChecked(true);
        }

        sortSelection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBy = Constants.SORT_OPTIONS[0];
                updateRecyclerView();
                dialog.dismiss();
            }
        });
        sortSelection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBy = Constants.SORT_OPTIONS[1];
                updateRecyclerView();
                dialog.dismiss();
            }
        });
        sortSelection3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBy = Constants.SORT_OPTIONS[2];
                updateRecyclerView();
                dialog.dismiss();
            }
        });
        sortSelection4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBy = Constants.SORT_OPTIONS[3];
                updateRecyclerView();
                dialog.dismiss();
            }
        });
        sortSelection5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBy = Constants.SORT_OPTIONS[4];
                updateRecyclerView();
                dialog.dismiss();
            }
        });
        sortSelection6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBy = Constants.SORT_OPTIONS[5];
                updateRecyclerView();
                dialog.dismiss();
            }
        });
//
//        editLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "editLayout::onClick", Toast.LENGTH_SHORT).show();
//            }
//        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showFilterDialog(String filterOption) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_filter);
        RadioButton filterSelection0 = dialog.findViewById(R.id.filterSelection0);
        RadioButton filterSelection1 = dialog.findViewById(R.id.filterSelection1);
        RadioButton filterSelection2 = dialog.findViewById(R.id.filterSelection2);
        RadioButton filterSelection3 = dialog.findViewById(R.id.filterSelection3);
        RadioButton filterSelection4 = dialog.findViewById(R.id.filterSelection4);
        RadioButton filterSelection5 = dialog.findViewById(R.id.filterSelection5);
        RadioButton filterSelection6 = dialog.findViewById(R.id.filterSelection6);
        RadioButton filterSelection7 = dialog.findViewById(R.id.filterSelection7);
        RadioButton filterSelection8 = dialog.findViewById(R.id.filterSelection8);
        RadioButton filterSelection9 = dialog.findViewById(R.id.filterSelection9);
        ArrayList<RadioButton> selections = new ArrayList<>();
        selections.add(filterSelection0);
        selections.add(filterSelection1);
        selections.add(filterSelection2);
        selections.add(filterSelection3);
        selections.add(filterSelection4);
        selections.add(filterSelection5);
        selections.add(filterSelection6);
        selections.add(filterSelection7);
        selections.add(filterSelection8);
        selections.add(filterSelection9);

        int filterPosition = -1;
        for (int i = 0; i < Constants.FILTER_OPTIONS.length; i++) {
            if (filterOption.equals(Constants.FILTER_OPTIONS[i])) {
                filterPosition = i;
            }
        }
        Log.d(TAG, "filterPosition: " + filterPosition);
        if (filterPosition >= 0) {
            selections.get(filterPosition).setChecked(true);
        }
        filterSelection0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBy = Constants.FILTER_OPTIONS[0];
                updateRecyclerView();
                dialog.dismiss();
            }
        });
        filterSelection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBy = Constants.FILTER_OPTIONS[1];
                updateRecyclerView();
                dialog.dismiss();
            }
        });
        filterSelection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBy = Constants.FILTER_OPTIONS[2];
                updateRecyclerView();
                dialog.dismiss();
            }
        });
        filterSelection3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBy = Constants.FILTER_OPTIONS[3];
                updateRecyclerView();
                dialog.dismiss();
            }
        });
        filterSelection4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBy = Constants.FILTER_OPTIONS[4];
                updateRecyclerView();
                dialog.dismiss();
            }
        });
        filterSelection5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBy = Constants.FILTER_OPTIONS[5];
                updateRecyclerView();
                dialog.dismiss();
            }
        });
        filterSelection6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBy = Constants.FILTER_OPTIONS[6];
                updateRecyclerView();
                dialog.dismiss();
            }
        });
        filterSelection7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBy = Constants.FILTER_OPTIONS[7];
                updateRecyclerView();
                dialog.dismiss();
            }
        });
        filterSelection8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBy = Constants.FILTER_OPTIONS[8];
                updateRecyclerView();
                dialog.dismiss();
            }
        });
        filterSelection9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBy = Constants.FILTER_OPTIONS[9];
                updateRecyclerView();
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
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
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference deleteDatabaseReference = db.getReference(Application.class.getSimpleName()+ "/" +userID + "/" + applicationKey);;
            deleteDatabaseReference.removeValue();
            applicationAdapter.applicationEntries.remove(position);
//            applicationAdapter.notifyDataSetChanged();
        }
    };

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

    private void updateRecyclerView() {
        // Apply search filtering
        Map<String, Application> applicationMapFiltered = Filter.filterByLoseSearch(applicationMap, searchTextView.getText().toString());


        Log.d(TAG, "filterBy: " + filterBy);

        // Apply filter by
        if (filterBy.equals("Intern")) {
            applicationMapFiltered = Filter.filterByIntern(applicationMapFiltered);
        }
        else if (filterBy.equals("Full Time")) {
            applicationMapFiltered = Filter.filterByFullTime(applicationMapFiltered);
        }
        else if (filterBy.equals("Interested")) {
            applicationMapFiltered = Filter.filterByInterested(applicationMapFiltered);
        }
        else if (filterBy.equals("Applied")) {
            applicationMapFiltered = Filter.filterByApplied(applicationMapFiltered);
        }
        else if (filterBy.equals("OA")) {
            applicationMapFiltered = Filter.filterByOA(applicationMapFiltered);
        }
        else if (filterBy.equals("Interview")) {
            applicationMapFiltered = Filter.filterByInterview(applicationMapFiltered);
        }
        else if (filterBy.equals("Rejected")) {
            applicationMapFiltered = Filter.filterByRejected(applicationMapFiltered);
        }
        else if (filterBy.equals("Offer")) {
            applicationMapFiltered = Filter.filterByOffer(applicationMapFiltered);
        }
        else if (filterBy.equals("Referred")) {
            applicationMapFiltered = Filter.filterByReferred(applicationMapFiltered);
        }


        ApplicationSorter applicationSorter = new ApplicationSorter(applicationMapFiltered);


        Log.d(TAG, "sortOption: " + sortBy);
        if (sortBy.equals("Priority")) {
            sortedEntries = applicationSorter.sortByPriority();
            Log.d(TAG, "sort by priority");
        }
        else if (sortBy.equals("Create Date")) {
            sortedEntries = applicationSorter.sortByCreateDate();
            Log.d(TAG, "sort by create date");
        }
        else if (sortBy.equals("Interview Date")) {
            sortedEntries = applicationSorter.sortByInterviewDate();
            Log.d(TAG, "sort by interview date");
        }
        else if (sortBy.equals("Applied Date")) {
            sortedEntries = applicationSorter.sortByAppliedDate();
            Collections.reverse(sortedEntries);
            Log.d(TAG, "sort by applied date");
        }
        else if (sortBy.equals("Status")) {
            sortedEntries = applicationSorter.sortByStatus();
            Log.d(TAG, "sort by status");
        }
        else {
            sortedEntries = applicationSorter.sortByStatus();
            Collections.reverse(sortedEntries);
            Log.d(TAG, "sort by status (reversed)");
        }
        Log.d(TAG, Utilities.printEntries(sortedEntries));
        applicationAdapter = new ApplicationAdapter(sortedEntries);
        recyclerView.setAdapter(applicationAdapter);
    }

}