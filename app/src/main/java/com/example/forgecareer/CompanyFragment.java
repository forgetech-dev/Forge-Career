package com.example.forgecareer;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forgecareer.db.Application;
import com.example.forgecareer.recyclecViews.ApplicationAdapter;
import com.example.forgecareer.utils.ApplicationSorter;
import com.example.forgecareer.utils.Constants;
import com.example.forgecareer.utils.DropdownAdapter;
import com.example.forgecareer.utils.Filter;
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

    String sortBy = DEFAULT_SORT_OPTION;



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
        dialog.setContentView(R.layout.bottom_sheet);

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
        int sortPosition = 0;
        for (int i = 1; i < Constants.SORT_OPTIONS.length; i++) {
            if (sortOption.equals(Constants.SORT_OPTIONS[i])) {
                sortPosition = i;
            }
        }
        Log.d(TAG, "sortPosition: " + sortPosition);
        if (sortPosition > 0) {
            selections.get(sortPosition-1).setChecked(true);
        }
        sortSelection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBy = Constants.SORT_OPTIONS[1];
                dialog.dismiss();
            }
        });
        sortSelection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBy = Constants.SORT_OPTIONS[2];
                dialog.dismiss();
            }
        });
        sortSelection3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBy = Constants.SORT_OPTIONS[3];
                dialog.dismiss();
            }
        });
        sortSelection4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBy = Constants.SORT_OPTIONS[4];
                dialog.dismiss();
            }
        });
        sortSelection5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBy = Constants.SORT_OPTIONS[5];
                dialog.dismiss();
            }
        });
        sortSelection6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBy = Constants.SORT_OPTIONS[6];
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

    private void updateRecyclerView() {
        // Apply search filtering
        Map<String, Application> applicationMapFiltered = Filter.filterByLoseSearch(applicationMap, searchTextView.getText().toString());
        ApplicationSorter applicationSorter = new ApplicationSorter(applicationMapFiltered);
        sortedEntries = applicationSorter.sortByCreateDate();
        applicationAdapter = new ApplicationAdapter(sortedEntries);
        recyclerView.setAdapter(applicationAdapter);
    }

}