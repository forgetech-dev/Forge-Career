package com.example.forgecareer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.forgecareer.db.Application;
import com.example.forgecareer.db.ApplicationDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddApplicationActivity extends AppCompatActivity {

    EditText companyNameEditText, jobTypeEditText, statusEditText, positionTypeEditText, refererEditText, priorityEditText;
    EditText applicationDateEditText, interviewDateEditText, startDateEditText, notesEditText;

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
        jobTypeEditText = findViewById(R.id.jobTypeEditText);
        statusEditText = findViewById(R.id.statusEditText);
        positionTypeEditText = findViewById(R.id.positionTypeEditText);
        refererEditText = findViewById(R.id.refererEditText);
        priorityEditText = findViewById(R.id.priorityEditText);

        applicationDateEditText = findViewById(R.id.applicationDateEditText);
        interviewDateEditText = findViewById(R.id.interviewDateEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
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

    }

    public boolean checkInput() {
        String companyName = companyNameEditText.getText().toString();
        String jobType = jobTypeEditText.getText().toString();
        String positionType = positionTypeEditText.getText().toString();
        String referrer = refererEditText.getText().toString();
        String status = statusEditText.getText().toString();
        String applicationDate = applicationDateEditText.getText().toString();
        String priority = priorityEditText.getText().toString();
        String interviewDate = interviewDateEditText.getText().toString();
        String notes = notesEditText.getText().toString();
        String startDate = startDateEditText.getText().toString();

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
        String jobType = jobTypeEditText.getText().toString();

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
        String status = statusEditText.getText().toString();

        // applicationDate (optional) -> default: N/A
        String applicationDate = applicationDateEditText.getText().toString();
        if (applicationDate.equals("")) {
            applicationDate = "N/A";
        }

        // priority (mandatory) -> default: Medium
        String priority = priorityEditText.getText().toString();
        if (priority.equals("")) {
            priority = "Medium";
        }

        // interviewDate (optional) -> default: N/A
        String interviewDate = interviewDateEditText.getText().toString();
        if (interviewDate.equals("")) {
            interviewDate = "N/A";
        }

        // startDate (optional) -> default: Summer23
        String startDate = startDateEditText.getText().toString();
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