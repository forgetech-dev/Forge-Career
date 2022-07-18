package com.forgetech.forgecareer.db;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
