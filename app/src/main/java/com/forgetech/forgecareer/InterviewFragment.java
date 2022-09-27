package com.forgetech.forgecareer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.forgetech.forgecareer.R;

import com.forgetech.forgecareer.db.Application;
import com.forgetech.forgecareer.decorators.OneDayDecorator;
import com.forgetech.forgecareer.recyclecViews.InterviewAdapter;
import com.forgetech.forgecareer.utils.ApplicationSorter;
import com.forgetech.forgecareer.utils.DateParser;
import com.forgetech.forgecareer.utils.Filter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.threeten.bp.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InterviewFragment#} factory method to
 * create an instance of this fragment.
 */
public class InterviewFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener, OnDateLongClickListener {

    MaterialCalendarView calendarView;
    TextView textView;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    private final String TAG = "InterviewFragment";

    private ArrayList<Map.Entry<String, Application>> applicationEntries;
    private Map<String, Application> applicationMap;
    private Map<String, Application> undatedActionRequiredMap;
    private ConstraintLayout dummyLayout;


    private LocalDate selectedDate;

    RecyclerView recyclerView;
    private DatabaseReference databaseReference;


    public InterviewFragment() {
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
        View view = inflater.inflate(R.layout.fragment_interview, container, false);
        calendarView = view.findViewById(R.id.calendarView);
//        textView = view.findViewById(R.id.textViewDateSelection);

        recyclerView = view.findViewById(R.id.interviewRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        calendarView.setOnDateChangedListener(this);
        calendarView.setOnDateLongClickListener(this);
        calendarView.setOnMonthChangedListener(this);
//        textView.setText("No Selection");
        dummyLayout = view.findViewById(R.id.dummyConstrainLayout);
        dummyLayout.setMaxHeight(0);

        selectedDate = LocalDate.now();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Application.class.getSimpleName()+ "/" + LoginActivity.userID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Log.d(TAG, "onDataChange -> companyName = " + snap.getKey().toString());
                }
                applicationMap = updateMapFromSnapshot(snapshot);
                MainActivity.applicationMap = applicationMap;
//                for (Map.Entry<String, Application> entry : applicationMap.entrySet()) {
//                    Log.d(TAG, entry.getValue().getCompanyName() + " : " +entry.getValue().getCreateDate());
//                }

                Map<String, Application> filteredMap = Filter.filterByInterviewOA(applicationMap);
                undatedActionRequiredMap = Filter.filterByUndatedActionRequired(applicationMap);
                applicationMap = filteredMap;

                ApplicationSorter applicationSorter = new ApplicationSorter(Filter.filterByDate(filteredMap, selectedDate));
                ArrayList<Map.Entry<String, Application>> sortedEntries = applicationSorter.sortByInterviewDate();
                sortedEntries.addAll(undatedActionRequiredMap.entrySet());
                for (Map.Entry<String, Application> entry : sortedEntries) {
                    Log.d(TAG, "Company: " + entry.getValue().getCompanyName() + "  " + entry.getValue().getInterviewDate());
                }

                InterviewAdapter interviewAdapter = new InterviewAdapter(sortedEntries);
                recyclerView.setAdapter(interviewAdapter);
//                ApplicationAdapter applicationAdapter = new ApplicationAdapter(applicationMap);
//                recyclerView.setAdapter(applicationAdapter);
//                Log.d(TAG, "Count: " + applicationAdapter.getItemCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled");
            }
        });


        Map<String, Application> applicationMap = MainActivity.applicationMap;
        ApplicationSorter sorter = new ApplicationSorter(applicationMap);

        applicationEntries = sorter.sortByCreateDate();
        markInterviewDate(applicationEntries);
        Log.d(TAG, applicationEntries.toString());
        calendarView.addDecorators(
                oneDayDecorator
        );
        return view;
    }

    @Override
    public void onDateSelected(
            @NonNull MaterialCalendarView calendarView,
            @NonNull CalendarDay date,
            boolean selected) {
        LocalDate localDate = date.getDate();
        selectedDate = localDate;
        oneDayDecorator.setDate(date.getDate());
        Map<String, Application> interviewAfterMap = Filter.filterByDate(applicationMap, date.getDate());


        ApplicationSorter sorter = new ApplicationSorter(interviewAfterMap);
        ArrayList<Map.Entry<String, Application>> sortedEntries = sorter.sortByInterviewDate();
        sortedEntries.addAll(undatedActionRequiredMap.entrySet());
        InterviewAdapter interviewAdapter = new InterviewAdapter(sortedEntries);
        recyclerView.setAdapter(interviewAdapter);

        Map.Entry<String, Application> entry = getNextInterview(date.getDate());


        calendarView.invalidateDecorators();

//        textView.setText(date.getDate().toString());

    }

    @Override
    public void onDateLongClick(@NonNull MaterialCalendarView calendarView, @NonNull CalendarDay date) {
//        final String text = String.format("%s is available", FORMATTER.format(date.getDate()));
//        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
//        textView.setText("No onDateLongClick");
    }

    @Override
    public void onMonthChanged(MaterialCalendarView calendarView, CalendarDay date) {
        //noinspection ConstantConditions
//        getSupportActionBar().setTitle(FORMATTER.format(date.getDate()));
//        textView.setText("onMonthChanged");
    }

    public void markInterviewDate(ArrayList<Map.Entry<String, Application>> applicationEntries) {
        for (Map.Entry<String, Application> entry : applicationEntries) {
            Application application = entry.getValue();
            Log.d(TAG, "Company: " + application.getCompanyName());
            Log.d(TAG, "Status: " + application.getStatus());
            Log.d(TAG, "InterviewDate" + application.getInterviewDate());
            if ((application.getStatus().equals("Interview") || application.getStatus().equals("OA")) && !application.getInterviewDate().equals("N/A")) {
                Log.d(TAG, application.getInterviewDate());
                Date date = DateParser.stringToDate(application.getInterviewDate());
                int year = date.getYear() + 1900;
                int month = date.getMonth() + 1;
                int day = date.getDate();
                LocalDate currentLocalDate = LocalDate.of(year, month, day);
                OneDayDecorator decorator = new OneDayDecorator();
                decorator.setDate(currentLocalDate);
                calendarView.addDecorator(decorator);
            }
        }
        LocalDate currentLocalDate = LocalDate.of(DateParser.getCurrentYear(), DateParser.getCurrentMonth(), DateParser.getCurrentDay());
        OneDayDecorator todayDecorator = new OneDayDecorator();
        todayDecorator.setDate(currentLocalDate);
        calendarView.addDecorator(todayDecorator);
    }

    private Map.Entry<String, Application> getNextInterview(LocalDate localDate) {
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day);
        Date date = calendar.getTime();
        Map.Entry<String, Application> nextEntry = null;
        for (Map.Entry<String, Application> entry : applicationEntries) {
            Application currentApplication = entry.getValue();
            if ((currentApplication.getStatus().equals("Interview") || currentApplication.getStatus().equals("OA") )&& !currentApplication.getInterviewDate().equals("N/A")) {
                Date applicationInterviewDate = DateParser.stringToDate(currentApplication.getInterviewDate());
                if (date.compareTo(applicationInterviewDate) < 0) {
                    nextEntry = entry;
                }
            }
        }
        return nextEntry;
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