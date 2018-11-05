package com.ephoenixdev.izgubljenonadjeno;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button btnOK;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressBar progressBar;
    private TextView mStatusTextView;
    private View loginLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnOK = findViewById(R.id.buttonLogInOK);
        editTextEmail = findViewById(R.id.editTextLogInEmail);
        editTextPassword = findViewById(R.id.editTextLogInPassword);
        progressBar = findViewById(R.id.progressBarLogIn);
        mStatusTextView = findViewById(R.id.textViewLogIn);
        loginLayout = findViewById(R.id.login_layout);

        mAuth = FirebaseAuth.getInstance();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim());
            }
        });

    }
        @Override
        public void onStart() {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        }

        private void hideProgressDialog() {

            progressBar.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        }

        private void showProgressDialog() {

            progressBar.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
        }

        private boolean validateForm() {
            boolean valid = true;

            String email = editTextEmail.getText().toString();
            if (TextUtils.isEmpty(email)) {
                editTextEmail.setError("Obavezno polje");
                valid = false;
            } else {
                editTextEmail.setError(null);
            }

            String password = editTextPassword.getText().toString();
            if (TextUtils.isEmpty(password)) {
                editTextPassword.setError("Obavezno polje");
                valid = false;
            } else {
                editTextPassword.setError(null);
            }

            return valid;
        }

        private void signIn(String email, String password) {
            Log.d("Logovanje", "signIn:" + email);
            if (!validateForm()) {
                return;
            }

            showProgressDialog();

            // [START sign_in_with_email]
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Logovanje", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Logovanje", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Greška pri logovanju!",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // [START_EXCLUDE]
                            if (!task.isSuccessful()) {
                                mStatusTextView.setVisibility(View.VISIBLE);
                                mStatusTextView.setText("Neispravna lozinka ili email!");
                            }
                            hideProgressDialog();
                            // [END_EXCLUDE]
                        }
                    });
            // [END sign_in_with_email]
        }


        private void updateUI(FirebaseUser currentUser) {
        }
    }
