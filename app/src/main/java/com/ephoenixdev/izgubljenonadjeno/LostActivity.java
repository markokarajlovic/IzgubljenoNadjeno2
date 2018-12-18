package com.ephoenixdev.izgubljenonadjeno;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.ephoenixdev.izgubljenonadjeno.adapters.ListOfLostAdapter;
import com.ephoenixdev.izgubljenonadjeno.models.LostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private FirebaseAuth mAuth;

    private ImageButton btnHome, btnShare, btnAboutUs, btnContact;
    private Spinner spinner;
    private RecyclerView recyclerView;
    private ListOfLostAdapter listOfLostAdapter;
    private List<LostModel> lostModelList;

    private String drzava = "Srbija";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);

        mAuth = FirebaseAuth.getInstance();
        btnHome = findViewById(R.id.imageButtonLostHome);
        btnShare = findViewById(R.id.imageButtonLostShare);
        btnAboutUs = findViewById(R.id.imageButtonLostAboutUs);
        btnContact = findViewById(R.id.imageButtonLostContact);
        spinner = findViewById(R.id.spinnerLost);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.drzave, android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(this);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LostActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String text = getResources().getString(R.string.share_text);
                intent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                intent.putExtra(Intent.EXTRA_TEXT, text);

                startActivity(Intent.createChooser(intent, "Izaberite"));
            }
        });

        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LostActivity.this, AboutusActivity.class);
                startActivity(intent);
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LostActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerViewLost);

        createList();
    }

    @Override
    public void onResume() {
        super.onResume();
        createList();
    }

    private void createList() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(LostActivity.this));
        lostModelList = new ArrayList<>();
        listOfLostAdapter = new ListOfLostAdapter(LostActivity.this,lostModelList);
        recyclerView.setAdapter(listOfLostAdapter);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lostModelList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        LostModel lm = snapshot.getValue(LostModel.class);
                        lostModelList.add(lm);
                    }
                    listOfLostAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        Query query;

        switch (drzava){
            case "Srbija":
                query = FirebaseDatabase.getInstance().getReference("Lost").orderByChild("state").equalTo(drzava);
                break;
            case "Bosna i Hercegovina":
                query = FirebaseDatabase.getInstance().getReference("Lost").orderByChild("state").equalTo(drzava);
                break;
            case "Crna Gora":
                query = FirebaseDatabase.getInstance().getReference("Lost").orderByChild("state").equalTo(drzava);
                break;
            case "Hrvatska":
                query = FirebaseDatabase.getInstance().getReference("Lost").orderByChild("state").equalTo(drzava);
                break;
            default:
                query = FirebaseDatabase.getInstance().getReference("Lost");
        }

        query.addListenerForSingleValueEvent(valueEventListener);

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        drzava =  item;
        createList();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
