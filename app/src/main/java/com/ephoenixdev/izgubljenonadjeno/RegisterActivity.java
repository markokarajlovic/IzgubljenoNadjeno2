package com.ephoenixdev.izgubljenonadjeno;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.os.Handler;
import android.widget.Toast;

import com.ephoenixdev.izgubljenonadjeno.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    private Button btnOK;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPassword1;
    private ProgressBar progressBar;
    private ImageView imageViewProfileImg;
    private View registerLayout;

    private Uri mImageUri;

    private static final int PICK_IMAGE_REQUEST = 1;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnOK = findViewById(R.id.buttonRegOK);
        editTextEmail = findViewById(R.id.editTextRegEmail);
        editTextPassword = findViewById(R.id.editTextRegPassword);
        editTextPassword1 = findViewById(R.id.editTextRegPassword1);
        progressBar = findViewById(R.id.progressBarReg);
        imageViewProfileImg = findViewById(R.id.imageViewRegProfileImg);
        registerLayout = findViewById(R.id.register_layout);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("ProfileImages");

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(editTextEmail.getText().toString().trim(),editTextPassword.getText().toString().trim());
            }
        });

        imageViewProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(currentUser.getUid()+"/profileImage." + getFileExtension(mImageUri));

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

                            Toast.makeText(RegisterActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                            currentUser = mAuth.getCurrentUser();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
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

            Picasso.get().load(mImageUri).into(imageViewProfileImg);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount(String email, String password) {

        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, update UI with the signed-in user's information
                            Log.d("Registracija", "Registracija uspesna!");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            sendEmailVerification();
                            createUser();

                            // upload img
                            if (mUploadTask != null && mUploadTask.isInProgress()) {
                                Toast.makeText(RegisterActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                            } else {
                                uploadFile();
                            }

                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign up fails, display a message to the user.

                            Toast.makeText(RegisterActivity.this, "Registracija neuspesna!",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void createUser() {

        DatabaseReference databaseUser;
        currentUser = mAuth.getCurrentUser();
        databaseUser = FirebaseDatabase.getInstance().getReference("User");

        String profileImage;

        if (mImageUri != null) {
            profileImage = "profileImage." + getFileExtension(mImageUri);

        }else
        {
            profileImage = "";
        }
        UserModel um = new UserModel(currentUser.getUid(),profileImage);
        databaseUser.child(currentUser.getUid()).setValue(um);

    }

    private void hideProgressDialog() {
        progressBar.setVisibility(View.GONE);
        registerLayout.setVisibility(View.VISIBLE);
    }

    private void showProgressDialog() {

        progressBar.setVisibility(View.VISIBLE);
        registerLayout.setVisibility(View.GONE);

    }

    private boolean validateForm() {

        boolean valid = true;

        String email = editTextEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Obavezno polje.");
            valid = false;
        } else {
            editTextEmail.setError(null);
        }

        String password = editTextPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Obavezno polje.");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }

        String password1 = editTextPassword1.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editTextPassword1.setError("Obavezno polje.");
            valid = false;
        } else {
            editTextPassword1.setError(null);
        }

        if(password == password1){
            valid = false;
            editTextPassword1.setError("Lozinke moraju biti iste!");
        }

        return valid;
    }

    private void updateUI(FirebaseUser currentUser) {

    }

    private void sendEmailVerification() {

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("RegisterActivity", "sendEmailVerification", task.getException());
                            Toast.makeText(RegisterActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

}
