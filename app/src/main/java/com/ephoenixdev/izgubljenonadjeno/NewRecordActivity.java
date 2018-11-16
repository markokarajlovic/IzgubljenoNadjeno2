package com.ephoenixdev.izgubljenonadjeno;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ephoenixdev.izgubljenonadjeno.models.FoundModel;
import com.ephoenixdev.izgubljenonadjeno.models.LostModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewRecordActivity extends AppCompatActivity {

    private ImageView EditImageView;
    private EditText EditTitle, EditDiscription, EditPlace, EditPhone;
    private Button btn;
    private RadioGroup radioGroup;
    private RadioButton rbLost, rbFound;

    private Uri mImageUri;

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseReference;
    private StorageTask mUploadTask;

    private static final int PICK_IMAGE_REQUEST = 1;
    FirebaseUser currentUser;

    private String recordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);

        EditImageView = findViewById(R.id.imageViewNewRecord);
        EditTitle = findViewById(R.id.editTextNewRecordTitle);
        EditDiscription = findViewById(R.id.editTextNewRecordDiscription);
        EditPlace = findViewById(R.id.editTextNewRecordPlace);
        EditPhone = findViewById(R.id.editTextNewRecordPhone);
        btn = findViewById(R.id.btnNewRecord);
        radioGroup = findViewById(R.id.radiogroup);
        rbFound = findViewById(R.id.radioButton2);
        rbLost = findViewById(R.id.radioButton1);

        mAuth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRecord();
            }
        });

        EditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton1) {
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference("Lost");
                    mStorageRef = FirebaseStorage.getInstance().getReference("LostImages");
                } else if (checkedId == R.id.radioButton2) {
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference("Found");
                    mStorageRef = FirebaseStorage.getInstance().getReference("FoundImages");
                }
            }
        });

    }
    private void newRecord() {

        boolean postavljen = false;
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if (currentUser == null) {
            Toast.makeText(NewRecordActivity.this, "Morate biti ulogovani!", Toast.LENGTH_SHORT).show();
        } else {

            recordId = mDatabaseReference.push().getKey();

            String idUser = currentUser.getUid().toString();
            String title = EditTitle.getText().toString().trim();
            String image;
            if (mImageUri != null) {
                image = "record_image." + getFileExtension(mImageUri);
            } else {
                image = "";
            }
            String discription = EditDiscription.getText().toString().trim();
            String phone = EditPhone.getText().toString().trim();
            String place = EditPlace.getText().toString().trim();

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date d = new Date();
            String date = dateFormat.format(d);


            if(rbFound.isChecked()) {
                FoundModel model = new FoundModel(
                        recordId,
                        idUser,
                        title,
                        place,
                        phone,
                        discription,
                        image,
                        date);
                mDatabaseReference.child(recordId).setValue(model);
            }

            if(rbLost.isChecked()) {
                LostModel model = new LostModel(
                        recordId,
                        idUser,
                        title,
                        place,
                        phone,
                        discription,
                        image,
                        date);
                mDatabaseReference.child(recordId).setValue(model);
            }

            // upload img
            if (mUploadTask != null && mUploadTask.isInProgress()) {
                Toast.makeText(NewRecordActivity.this, "Postavljanje u toku", Toast.LENGTH_SHORT).show();
            } else {
                uploadFile();
            }

            postavljen = true;
        }

        if (postavljen) {
            Toast.makeText(NewRecordActivity.this, "Postavljeno", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NewRecordActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(NewRecordActivity.this, "Neuspešno postavljanje", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(recordId+"/record_image." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 500);

                            Toast.makeText(NewRecordActivity.this, "Uspešno postavljanje slike", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewRecordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Niste izabrali sliku", Toast.LENGTH_SHORT).show();
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(EditImageView);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
    }
}
