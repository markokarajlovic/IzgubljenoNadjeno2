package com.ephoenixdev.izgubljenonadjeno.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ephoenixdev.izgubljenonadjeno.R;
import com.ephoenixdev.izgubljenonadjeno.ViewItemActivity;
import com.ephoenixdev.izgubljenonadjeno.models.LostModel;

import java.util.ArrayList;
import java.util.List;

public class ListOfLostAdapter extends RecyclerView.Adapter<ListOfLostAdapter.LostViewHolder> {

    private Context mCtx;
    private List<LostModel> lostModelList;

    public ListOfLostAdapter(Context mCtx, List<LostModel> lostModelList){
        this.mCtx= mCtx;
        this.lostModelList = lostModelList;
    }

    @NonNull
    @Override
    public LostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_view, viewGroup, false);
        return new LostViewHolder(view,mCtx,lostModelList);
    }

    @Override
    public void onBindViewHolder(@NonNull LostViewHolder adViewHolder, int i) {


    }

    @Override
    public int getItemCount() {
        return lostModelList.size();
    }

    public class LostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context ctx;
        List<LostModel> lostModelList = new ArrayList<LostModel>();
        ImageView imageView;
        TextView textViewTitle, textViewPlace, textViewPhone;

        public LostViewHolder(@NonNull View itemView, Context ctx, List<LostModel> lostModelList) {

            super(itemView);
            this.ctx=ctx;
            this.lostModelList = lostModelList;
            itemView.setOnClickListener(this);

            imageView = itemView.findViewById(R.id.imageViewLost);
            textViewTitle = itemView.findViewById(R.id.textViewLostTitle);
            textViewPlace = itemView.findViewById(R.id.textViewLostPlace);
            textViewPhone = itemView.findViewById(R.id.textViewLostPhone);


        }
        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            LostModel lostModel = this.lostModelList.get(position);

            Intent intent = new Intent(this.ctx, ViewItemActivity.class);
            intent.putExtra("lostId",lostModel.getLostId());
            intent.putExtra("place",lostModel.getPlace());
            intent.putExtra("phone",lostModel.getPhone());
            intent.putExtra("discription",lostModel.getDescription());
            intent.putExtra("image",lostModel.getImage());
            intent.putExtra("date",lostModel.getDate());

            this.ctx.startActivity(intent);
        }
    }

}
