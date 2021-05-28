package com.datakaryawan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class InputDataKaryawan extends AppCompatActivity {

    TextView name, tempat, telpon, gajipokok;
    EditText nama, nick, tanggal, gaji;
    Button simpan;
    ImageView foto;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormat;
    private Uri imageUri;
    private static final int PICK_IMAGE = 1;
    UploadTask uploadTask;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_karyawan);

        // Edit Text //
        name = findViewById(R.id.edtname);
        nick = findViewById(R.id.edtnick);
        tanggal = findViewById(R.id.edttanggal);
        gaji = findViewById(R.id.edtgaji);

        // Button //
        simpan = findViewById(R.id.btn_simpan);

        // Foto users//
        foto = findViewById(R.id.img);
        // akhir dari foto //

        // kalender otomatis //
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        // akhir dari kalender otomatis //

        // Untuk meminta akses ke galery //
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGallery();
            }
        });

        // akhir dari akses //

        // Upload data ke firebase firestore //
        documentReference = db.collection("user").document("profile");
        storageReference = firebaseStorage.getInstance().getReference("profile images");
        // end firestore //

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData();
            }
        });

    }

    private void UploadData() {

        String nama          = name.getText().toString().trim();
        String nickkaryawan  = nick.getText().toString().trim();
        String tanggallahir  = tanggal.getText().toString().trim();
        String gajikaryawan  = gaji.getText().toString().trim();

        if (!TextUtils.isEmpty(nama) || !TextUtils.isEmpty(nickkaryawan)
                || !TextUtils.isEmpty(tanggallahir) || !TextUtils.isEmpty(gajikaryawan) || foto != null) {
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(imageUri));

            uploadTask = reference.putFile(imageUri);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                Uri downloadUri = task.getResult();
                                Map<String, String > profile = new HashMap<>();
                                profile.put("Nama Karyawan", nama);
                                profile.put("Nick", nickkaryawan);
                                profile.put("Tanggal lahir", tanggallahir);
                                profile.put("Gaji Karyawan", gajikaryawan);
                                profile.put("Url", downloadUri.toString());

                                documentReference.set(profile)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(InputDataKaryawan.this, "Profile Karyawan Create", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent();
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(InputDataKaryawan.this, "Failed", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }else {
            Toast.makeText(InputDataKaryawan.this, "All Field required", Toast.LENGTH_SHORT).show();
        }
    }

    private void showGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);
    }


    private void showDateDialog(){
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                tanggal.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE || resultCode == RESULT_OK ||
        data != null || data.getData() != null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(foto);
        }
    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void kembali(View view) {
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
        finish();
    }
}