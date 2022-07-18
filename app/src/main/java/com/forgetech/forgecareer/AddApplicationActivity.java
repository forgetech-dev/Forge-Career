package com.forgetech.forgecareer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.forgetech.forgecareer.R;
import com.forgetech.forgecareer.db.Application;
import com.forgetech.forgecareer.db.ApplicationDAO;
import com.forgetech.forgecareer.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddApplicationActivity extends AppCompatActivity {

    EditText companyNameEditText, positionTypeEditText, refererEditText;
    EditText applicationDateEditText, interviewDateEditText, notesEditText;

    AutoCompleteTextView jobTypeAutoComplete, statusAutoComplete, priorityAutoComplete, startDateAutoComplete;
    ArrayAdapter<String> jobTypeAdapterItems, statusAdapterItems, priorityAdapterItems, startDateAdapterItems;

    FloatingActionButton addApplicationCheckFAB;

    String TAG = "AddApplicationActivity";

    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_application);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find all edit text fields for further usage
        companyNameEditText = findViewById(R.id.companyNameEditText);
        positionTypeEditText = findViewById(R.id.positionTypeEditText);
        refererEditText = findViewById(R.id.refererEditText);

        applicationDateEditText = findViewById(R.id.applicationDateEditText);
        interviewDateEditText = findViewById(R.id.interviewDateEditText);
        notesEditText = findViewById(R.id.notesEditText);

        addApplicationCheckFAB = findViewById(R.id.addApplicationCheckFAB);

        addApplicationCheckFAB.setOnClickListener(v -> {
            if (!checkInput()) {
                Toast.makeText(this, "fields incomplete", Toast.LENGTH_SHORT).show();
            } else {
                String userID = LoginActivity.userID;
                ApplicationDAO dao = new ApplicationDAO(userID);
                Application application = createApplication();
                dao.add(application).addOnSuccessListener(suc -> {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er -> {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                });
                finish();
            }
        });

        bindView();
        updateAutocompleteTextView();

    }

    public void bindView() {
        jobTypeAutoComplete = findViewById(R.id.jobTypeAutoCompleteAdd);
        statusAutoComplete = findViewById(R.id.statusAutoCompleteAdd);
        priorityAutoComplete = findViewById(R.id.priorityAutoCompleteAdd);
        startDateAutoComplete = findViewById(R.id.startDateAutoCompleteAdd);
    }

    public void updateAutocompleteTextView() {
        String[] items = {"One", "Two", "Three", "Four", "Five"};
        jobTypeAutoComplete.setText("");
        priorityAutoComplete.setText("");
        statusAutoComplete.setText("");
        startDateAutoComplete.setText("");
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
        String status = statusAutoComplete.getText().toString();

        if (companyName.equals("")) {
            return false;
        }
        if (jobType.equals("")) {
            return false;
        }
        if (status.equals("")) {
            return false;
        }
        return true;
    }

    public Application createApplication() {
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


        // Create new object
        Application application = new Application(companyName, jobType, positionType, startDate, referrer, status, applicationDate, priority, interviewDate, notes);
        return application;
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