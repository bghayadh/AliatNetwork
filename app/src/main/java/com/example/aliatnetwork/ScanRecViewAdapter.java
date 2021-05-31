package com.example.aliatnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScanRecViewAdapter extends RecyclerView.Adapter<ScanRecViewAdapter.ViewHolder> {
    Context context;
    List<ScanList> scanList;


    public ScanRecViewAdapter(Context context, List<ScanList> scanList) {
        this.context = context;
        this.scanList = scanList;
    }

    @NonNull
    @Override
    public ScanRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.scan_item_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanRecViewAdapter.ViewHolder holder, int position) {
        holder.itemcode.setText(scanList.get(position).getItem());
        holder.barcode.setText(scanList.get(position).getBarcode());
        holder.serialnb.setText(scanList.get(position).getSerialNb());
        holder.quantity.setText(scanList.get(position).getQuantity());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                int p= holder.getLayoutPosition();
                ScanList scanlist= scanList.get(p);
                Toast.makeText(context, "Recycle Click" + scanlist +"  ", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return scanList.size();
    }


    public void setContacts(ArrayList<ScanList> scanList) {
        this.scanList = scanList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemcode,barcode,serialnb,quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemcode=itemView.findViewById(R.id.item_id);
            barcode=itemView.findViewById(R.id.barcode_id);
            serialnb=itemView.findViewById(R.id.serialnb_id);
            quantity=itemView.findViewById(R.id.quantity_id);

        }
    }


}


