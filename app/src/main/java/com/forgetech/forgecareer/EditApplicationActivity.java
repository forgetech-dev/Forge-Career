package com.forgetech.forgecareer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import com.forgetech.forgecareer.R;
import com.forgetech.forgecareer.db.Application;
import com.forgetech.forgecareer.utils.Constants;
import com.forgetech.forgecareer.utils.DateParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditApplicationActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;

    EditText companyNameEditText, positionTypeEditText, refererEditText;
    EditText applicationDateEditText, interviewDateEditText, notesEditText;
    AutoCompleteTextView jobTypeAutoComplete, statusAutoComplete, priorityAutoComplete, startDateAutoComplete;
    ArrayAdapter<String> jobTypeAdapterItems, statusAdapterItems, priorityAdapterItems, startDateAdapterItems;

    FloatingActionButton addApplicationCheckFAB;

    Map<String, Application> applicationMap;
    Application currentApplication;


    public static String TAG = "EditApplicationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_application);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String applicationKey = intent.getStringExtra("applicationKey");

        // Init database reference
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Application.class.getSimpleName()+ "/" + LoginActivity.userID + "/" + applicationKey);

        bindView();

        applicationMap = CompanyFragment.applicationMap;
        currentApplication = applicationMap.get(applicationKey);

        companyNameEditText.setText(currentApplication.getCompanyName());
        positionTypeEditText.setText(currentApplication.getPositionType());
        refererEditText.setText(currentApplication.getReferer());
        applicationDateEditText.setText(currentApplication.getApplicationDate());
        interviewDateEditText.setText(currentApplication.getInterviewDate());
        notesEditText.setText(currentApplication.getNotes());



        updateAutocompleteTextView();


        addApplicationCheckFAB = findViewById(R.id.addApplicationCheckFABEditApplication);
        addApplicationCheckFAB.setOnClickListener(v -> {
            if (!checkInput()) {
                Toast.makeText(this, "fields incomplete", Toast.LENGTH_SHORT).show();
            } else {
                Map<String, Object> applicationMap = createApplicationMap();
                databaseReference.updateChildren(applicationMap);
                finish();
            }
        });
    }

    public void bindView() {
        companyNameEditText = findViewById(R.id.companyNameEditTextEditApplication);
        positionTypeEditText = findViewById(R.id.positionTypeEditTextEditApplication);
        refererEditText = findViewById(R.id.refererEditTextEditApplication);
        applicationDateEditText = findViewById(R.id.applicationDateEditTextEditApplication);
        interviewDateEditText = findViewById(R.id.interviewDateEditTextEditApplication);
        notesEditText = findViewById(R.id.notesEditTextEditApplication);
        jobTypeAutoComplete = findViewById(R.id.jobTypeAutoComplete);
        priorityAutoComplete = findViewById(R.id.priorityAutoComplete);
        statusAutoComplete = findViewById(R.id.statusAutoComplete);
        startDateAutoComplete = findViewById(R.id.startDateAutoComplete);
    }

    public void updateAutocompleteTextView() {
        jobTypeAutoComplete.setText(currentApplication.getJobType());
        priorityAutoComplete.setText(currentApplication.getPriority());
        statusAutoComplete.setText(currentApplication.getStatus());
        startDateAutoComplete.setText(currentApplication.getStartDate());
        String[] items = {"One", "Two", "Three", "Four", "Five"};
        jobTypeAdapterItems = new ArrayAdapter<String>(this, R.layout.list_item, Constants.JOB_TYPE);
        priorityAdapterItems = new ArrayAdapter<String>(this, R.layout.list_item, Constants.PRIORITY);
        statusAdapterItems = new ArrayAdapter<String>(this, R.layout.list_item, Constants.STATUS);
        startDateAdapterItems = new ArrayAdapter<String>(this, R.layout.list_item, Constants.START_DATE);
        jobTypeAutoComplete.setAdapter(jobTypeAdapterItems);
        priorityAutoComplete.setAdapter(priorityAdapterItems);
        statusAutoComplete.setAdapter(statusAdapterItems);
        startDateAutoComplete.setAdapter(startDateAdapterItems);



    }

    public boolean checkInput() {
        String companyName = companyNameEditText.getText().toString();
        String jobType = jobTypeAutoComplete.getText().toString();
        String priority = priorityAutoComplete.getText().toString();
        String status = statusAutoComplete.getText().toString();
        String startDate = startDateAutoComplete.getText().toString();
        String positionType = positionTypeEditText.getText().toString();
        String referrer = refererEditText.getText().toString();
        String applicationDate = applicationDateEditText.getText().toString();
        String interviewDate = interviewDateEditText.getText().toString();
        String notes = notesEditText.getText().toString();

        if (companyName.equals("")) {
            Toast.makeText(this, "please fill out companyName", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (jobType.equals("")) {
            Toast.makeText(this, "please fill out jobType", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (status.equals("")) {
            Toast.makeText(this, "please fill out status", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public Map<String, Object> createApplicationMap() {
        // companyName (mandatory)
        String companyName = companyNameEditText.getText().toString();

        // jobType (mandatory)
        String jobType = jobTypeAutoComplete.getText().toString();

        // positionType (optional) -> default: N/A
        String positionType = positionTypeEditText.getText().toString();
        if (positionType.equals("")) {
            positionType = "N/A";
        }

        // referrer (optional) -> default: N/A
        String referrer = refererEditText.getText().toString();
        if (referrer.equals("")) {
            referrer = "N/A";
        }

        // status (mandatory)
        String status = statusAutoComplete.getText().toString();

        // applicationDate (optional) -> default: N/A
        String applicationDate = applicationDateEditText.getText().toString();
        if (applicationDate.equals("")) {
            applicationDate = "N/A";
        }

        // priority (mandatory) -> default: Medium
        String priority = priorityAutoComplete.getText().toString();
        if (priority.equals("")) {
            priority = "Medium";
        }

        // interviewDate (optional) -> default: N/A
        String interviewDate = interviewDateEditText.getText().toString();
        if (interviewDate.equals("")) {
            interviewDate = "N/A";
        }

        // startDate (optional) -> default: Summer23
        String startDate = startDateAutoComplete.getText().toString();
        if (startDate.equals("")) {
            startDate = "Summer23";
        }

        // notes (optional) -> default:
        String notes = notesEditText.getText().toString();

        // updateDate (automatic)
        String updateDate = DateParser.getCurrentDateTimeString();


        // Create new object
        Map<String, Object> applicationMap = new HashMap<>();
        applicationMap.put("applicationDate", applicationDate);
        applicationMap.put("companyName", companyName);
        applicationMap.put("expanded", false);
        applicationMap.put("interviewDate", interviewDate);
        applicationMap.put("jobType", jobType);
        applicationMap.put("notes", notes);
        applicationMap.put("positionType", positionType);
        applicationMap.put("priority", priority);
        applicationMap.put("referer", referrer);
        applicationMap.put("startDate", startDate);
        applicationMap.put("status", status);
        applicationMap.put("updateDate", updateDate);


        return applicationMap;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}