package com.datakaryawan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Showprofile extends AppCompatActivity {

    ImageView foto;
    UploadTask uploadTask;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    TextView nameEt, nickkaryawanEt, tglEt, gajiEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showprofile);

        nameEt = findViewById(R.id.edtname);
        nickkaryawanEt = findViewById(R.id.edtnick);
        tglEt = findViewById(R.id.edttanggal);
        gajiEt = findViewById(R.id.edtgaji);
        foto           = findViewById(R.id.img);

        // Upload data ke firebase firestore //
        documentReference = db.collection("user").document("profile");
        storageReference = firebaseStorage.getInstance().getReference("profile images");
        // end firestore //
    }

    @Override
    protected void onStart() {
        super.onStart();

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){
                            String nama_result         = task.getResult().getString("Nama Karyawan");
                            String nickkaryawan_result = task.getResult().getString("Nick");
                            String tanggallahir_result = task.getResult().getString("Tanggal lahir");
                            String gajikaryawan_result = task.getResult().getString("Gaji Karyawan");
                            String Url                  = task.getResult().getString("Url");

                            Picasso.get().load(Url).into(foto);
                            nameEt.setText(nama_result);
                            nickkaryawanEt.setText(nickkaryawan_result);
                            tglEt.setText(tanggallahir_result);
                            gajiEt.setText(gajikaryawan_result);
                        }else{
                            Toast.makeText(Showprofile.this, "No Profile exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//keluar dari account
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    public void delete(View view) {
        btn_delete();
    }

    private void btn_delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Showprofile.this);
        builder.setTitle("Delete");
        builder.setMessage("Apakah Anda yakin akan menghapus akun profil anda ?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                documentReference.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Showprofile.this, "Profile Di hapus", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}