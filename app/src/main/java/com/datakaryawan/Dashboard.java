package com.datakaryawan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;

public class Dashboard extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        documentReference = db.collection("user").document("profile");
    }

    public void Input(View view)
    {
        startActivity(new Intent(getApplicationContext(), InputDataKaryawan.class));
        finish();
    }

    public void Profile(View view)
    {
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()){
                            Intent intent = new Intent(Dashboard.this, Showprofile.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(Dashboard.this, InputDataKaryawan.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}