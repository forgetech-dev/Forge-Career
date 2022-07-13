package com.example.forgecareer.db;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {
    private DatabaseReference databaseReference;

    public ApplicationDAO() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Application.class.getSimpleName());
    }
    public ApplicationDAO(String userID) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Application.class.getSimpleName()+ "/" +userID);
    }
    public Task<Void> add(Application application) {
        return databaseReference.push().setValue(application);
    }
}
