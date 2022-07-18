package com.forgetech.forgecareer;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import com.forgetech.forgecareer.R;
import com.forgetech.forgecareer.db.Application;
import com.forgetech.forgecareer.utils.Counter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
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
 * Use the {@link HomeFragment} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match


    private String TAG = "HomeFragment";

    private PieChart pieChart;
    private DatabaseReference databaseReference;
    private String userID;
    private Map<String, Application> applicationMap;

    TextView noDataTextView;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        pieChart = view.findViewById(R.id.overviewPiechart);
        pieChart.setVisibility(View.INVISIBLE);
        noDataTextView = view.findViewById(R.id.noDataTextView);

        applicationMap = new HashMap<>();
        userID = LoginActivity.userID;
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Application.class.getSimpleName()+ "/" +userID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                applicationMap = updateMapFromSnapshot(snapshot);
                MainActivity.applicationMap = applicationMap;
                setupPieChart();
                if (applicationMap.size() == 0) {
                    pieChart.setVisibility(View.INVISIBLE);
                    noDataTextView.setVisibility(View.VISIBLE);
                } else {
                    pieChart.setVisibility(View.VISIBLE);
                    noDataTextView.setVisibility(View.INVISIBLE);
                    loadPieChartData(applicationMap);
                }

                Log.d(TAG, applicationMap.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled");
            }
        });
        return view;
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(false);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Overview");
        pieChart.setCenterTextSize(24);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.getDescription().setEnabled(false);
    }

    private void loadPieChartData(Map<String, Application> applicationMap) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        int interestedCount = Counter.countTodo(applicationMap);
        int appliedCount = Counter.countApplied(applicationMap);
        int OACount = Counter.countOA(applicationMap);
        int interviewCount = Counter.countInterview(applicationMap);
        int rejectedCount = Counter.countRejected(applicationMap);
        int offerCount = Counter.countOffer(applicationMap);
        float sumCount = (float)(interestedCount + appliedCount + OACount + interviewCount + rejectedCount + offerCount);
        Log.d(TAG, "interestedCount: " + interestedCount);
        Log.d(TAG, "appliedCount: " + appliedCount);
        Log.d(TAG, "OACount: " + OACount);
        Log.d(TAG, "interviewCount: " + interviewCount);
        Log.d(TAG, "rejectedCount: " + rejectedCount);
        Log.d(TAG, "offerCount: " + offerCount);
        if (interestedCount > 0) {
            entries.add(new PieEntry(interestedCount, "Interested"));
        }
        if (appliedCount > 0) {
            entries.add(new PieEntry(appliedCount, "Applied"));
        }
        if (OACount > 0) {
            entries.add(new PieEntry(OACount, "OA"));
        }
        if (interviewCount > 0) {
            entries.add(new PieEntry(interviewCount, "Interview"));
        }
        if (rejectedCount > 0) {
            entries.add(new PieEntry(rejectedCount, "Rejected"));
        }
        if (offerCount > 0) {
            entries.add(new PieEntry(offerCount, "Offer"));
        }
//        entries.add(new PieEntry(0.5f, "A"));
//        entries.add(new PieEntry(0.5f, "B"));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }
        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Status");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int)value);
            }
        });
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1000, Easing.EaseInOutQuad);
        pieChart.setVisibility(View.VISIBLE);
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
            Application application = new Application(companyName, jobType, positionType, startDate, referrer, status, applicationDate, priority, interviewDate, notes);
            applicationList.add(application);

            String applicationKey = snap.getKey();
            applicationMap.put(applicationKey, application);
        }
        return applicationMap;
    }
}