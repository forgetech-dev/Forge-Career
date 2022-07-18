package com.forgetech.forgecareer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.forgetech.forgecareer.R;
import com.forgetech.forgecareer.db.Application;
import com.forgetech.forgecareer.db.ApplicationDAO;

public class FirebaseActivityTemp extends AppCompatActivity {

    public String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_temp);
        final EditText editName = findViewById(R.id.edit_name);
        final EditText editPosition = findViewById(R.id.edit_position);
        Button btnSubmit = findViewById(R.id.btn_submit);

        UserID = LoginActivity.userID;

        Toast.makeText(this, "UserID: " + UserID, Toast.LENGTH_SHORT).show();

        ApplicationDAO dao = new ApplicationDAO(UserID);
        btnSubmit.setOnClickListener(click->{
            Application emp = new Application(editName.getText().toString(), editPosition.getText().toString());
            dao.add(emp).addOnSuccessListener(suc -> {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(er -> {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            });
        });
    }



}