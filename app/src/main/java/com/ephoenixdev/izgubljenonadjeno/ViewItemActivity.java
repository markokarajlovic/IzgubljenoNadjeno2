package com.ephoenixdev.izgubljenonadjeno;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ViewItemActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView viewTitle, viewPlace, viewDiscription, viewPhone;
    private ImageView imageView;
    private ImageButton btnShare;
    private String title, place, discription, image, phone, foundId, lostId, userId;
    private int foundOrLost;

    private StorageReference storageRef;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        mAuth = FirebaseAuth.getInstance();

        viewTitle = findViewById(R.id.textViewViewTitle);
        viewPlace = findViewById(R.id.textViewViewPlace);
        viewDiscription = findViewById(R.id.textViewViewDiscription);
        viewPhone = findViewById(R.id.textViewViewPhone);
        imageView = findViewById(R.id.imageViewViewImage);
        btnShare = findViewById(R.id.imageButtonViewShare);


        userId = getIntent().getStringExtra("userId");
        title = getIntent().getStringExtra("title");
        discription = getIntent().getStringExtra("discription");
        phone = getIntent().getStringExtra("phone");
        place = getIntent().getStringExtra("place");
        image = getIntent().getStringExtra("image");

        viewTitle.setText(title);
        viewDiscription.setText(discription);
        viewPhone.setText(phone);
        viewPlace.setText(place);

        foundOrLost = getIntent().getIntExtra("foundOrLost" , 0);

        if(foundOrLost == 1){
            lostId = getIntent().getStringExtra("lostId");
           setImageOfLost(lostId,image);
        }
        if(foundOrLost == 2){
            foundId = getIntent().getStringExtra("found");
            setImageOfFound(foundId,image);
        }

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                intent.putExtra(Intent.EXTRA_TEXT, discription);

                startActivity(Intent.createChooser(intent, "Izaberite"));
            }
        });



    }

    private void setImageOfFound(String foundId, String image) {

        storageRef = FirebaseStorage.getInstance().getReference("FoundImages/" + foundId + "/" +image);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void setImageOfLost(String lostId, String image) {

        storageRef = FirebaseStorage.getInstance().getReference("LostImages/" + lostId + "/" +image);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
