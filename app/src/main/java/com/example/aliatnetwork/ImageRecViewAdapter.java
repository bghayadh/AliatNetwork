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

        holder.txt1.setText(images.get(position).getImagePath());
        holder.img1.setImageResource(images.get(position).getImageIcon());

        holder.img1.setOnClickListener(new View.OnClickListener() {
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
        private TextView txt1;
        private ImageView img1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt1= itemView.findViewById(R.id.imgpath);
            img1=itemView.findViewById(R.id.imgid);
        }
    }
}
