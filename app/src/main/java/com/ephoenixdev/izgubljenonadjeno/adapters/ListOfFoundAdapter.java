package com.ephoenixdev.izgubljenonadjeno.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ephoenixdev.izgubljenonadjeno.R;
import com.ephoenixdev.izgubljenonadjeno.ViewItemActivity;
import com.ephoenixdev.izgubljenonadjeno.models.FoundModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListOfFoundAdapter extends RecyclerView.Adapter<ListOfFoundAdapter.FoundViewHolder>{

    private Context mCtx;
    private List<FoundModel> foundModelList;

    public ListOfFoundAdapter(Context mCtx, List<FoundModel> foundModelList){
        this.mCtx= mCtx;
        this.foundModelList = foundModelList;
    }

    @NonNull
    @Override
    public FoundViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_view, viewGroup, false);
        return new FoundViewHolder(view,mCtx,foundModelList);
    }

    @Override
    public void onBindViewHolder(@NonNull FoundViewHolder foundViewHolder, int i) {

        FoundModel foundModel = foundModelList.get(i);

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("FoundImages/" + foundModel.getFoundId() + "/" +foundModel.getImage());

        foundViewHolder.textViewTitle.setText(foundModel.getTitle());
        foundViewHolder.textViewPlace.setText(foundModel.getPlace());
        foundViewHolder.textViewPhone.setText(foundModel.getPhone());

        final ImageView imageView = foundViewHolder.imageView;

        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

    @Override
    public int getItemCount() {
        return foundModelList.size();
    }

    public class FoundViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context ctx;
        List<FoundModel> foundModelList = new ArrayList<FoundModel>();
        ImageView imageView;
        TextView textViewTitle, textViewPlace, textViewPhone;

        public FoundViewHolder(@NonNull View itemView, Context ctx, List<FoundModel> foundModelList) {

            super(itemView);
            this.ctx=ctx;
            this.foundModelList = foundModelList;
            itemView.setOnClickListener(this);

            imageView = itemView.findViewById(R.id.imageViewLost);
            textViewTitle = itemView.findViewById(R.id.textViewLostTitle);
            textViewPlace = itemView.findViewById(R.id.textViewLostPlace);
            textViewPhone = itemView.findViewById(R.id.textViewLostPhone);


        }
        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            FoundModel foundModel = this.foundModelList.get(position);

            Intent intent = new Intent(this.ctx, ViewItemActivity.class);
            intent.putExtra("foundOrLost",2);
            intent.putExtra("foundId",foundModel.getFoundId());
            intent.putExtra("userId",foundModel.getUserId());
            intent.putExtra("title",foundModel.getTitle());
            intent.putExtra("place",foundModel.getPlace());
            intent.putExtra("phone",foundModel.getPhone());
            intent.putExtra("discription",foundModel.getDescription());
            intent.putExtra("image",foundModel.getImage());
            intent.putExtra("date",foundModel.getDate());

            this.ctx.startActivity(intent);
        }
    }
}
