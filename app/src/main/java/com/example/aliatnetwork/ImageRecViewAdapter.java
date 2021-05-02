package com.example.aliatnetwork;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImageRecViewAdapter extends RecyclerView.Adapter<ImageRecViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ImageListView> images;

    public ImageRecViewAdapter(Context context, ArrayList<ImageListView> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ImageRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(context).inflate(R.layout.image_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageRecViewAdapter.ViewHolder holder, int position) {

        holder.wareid.setText(images.get(position).getWareID());
        holder.imgpath.setText(images.get(position).getImagePath());

        holder.wareid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,images.get(position).getImagePath () ,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView wareid;
        private TextView imgpath;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            wareid= itemView.findViewById(R.id.wareid);
            imgpath=itemView.findViewById(R.id.imgpath);
        }
    }
}
