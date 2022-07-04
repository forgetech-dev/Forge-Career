package com.example.forgecareer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.forgecareer.db.Application;
import com.example.forgecareer.db.ApplicationDAO;

public class FirebaseActivityTemp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_temp);
        final EditText editName = findViewById(R.id.edit_name);
        final EditText editPosition = findViewById(R.id.edit_position);
        Button btnSubmit = findViewById(R.id.btn_submit);
        ApplicationDAO dao = new ApplicationDAO();
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