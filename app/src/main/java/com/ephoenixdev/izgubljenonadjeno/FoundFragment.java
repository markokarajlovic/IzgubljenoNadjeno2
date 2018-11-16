package com.ephoenixdev.izgubljenonadjeno;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ephoenixdev.izgubljenonadjeno.adapters.ListOfFoundAdapter;
import com.ephoenixdev.izgubljenonadjeno.models.FoundModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FoundFragment extends Fragment {

    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    private ListOfFoundAdapter listOfFoundAdapter;
    private List<FoundModel> foundModelList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_found,container,false);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recyclerViewFound);

        createList();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        createList();
    }

    private void createList() {


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        foundModelList = new ArrayList<>();
        listOfFoundAdapter = new ListOfFoundAdapter(getActivity(),foundModelList);
        recyclerView.setAdapter(listOfFoundAdapter);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foundModelList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        FoundModel fm = snapshot.getValue(FoundModel.class);
                        foundModelList.add(fm);
                    }
                    listOfFoundAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        Query query = FirebaseDatabase.getInstance().getReference("Found");
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
}
