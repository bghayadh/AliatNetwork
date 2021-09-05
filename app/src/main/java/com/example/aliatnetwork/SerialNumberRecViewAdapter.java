package com.example.aliatnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class SerialNumberRecViewAdapter extends RecyclerView.Adapter<SerialNumberRecViewAdapter.ViewHolder> {
    Context context;
    private List<SerialNumber> serialNumbers;
    EditText editText;
    String globalwareid;

    Connection conn;


    public SerialNumberRecViewAdapter(Context context, List<SerialNumber> serialNumbers) {
        this.context = context;
        this.serialNumbers = serialNumbers;

    }

    @NonNull
    @Override
    public SerialNumberRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.scan_item_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SerialNumberRecViewAdapter.ViewHolder holder, int position) {
        holder.itemCode.setText(serialNumbers.get(position).getItemCode());
        holder.serialNumber.setText(serialNumbers.get(position).getSerialNumber());
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                serialNumbers.remove(position);
                notifyDataSetChanged();


            }
        });
    }
    @Override
    public int getItemCount() {
        return serialNumbers.size();
    }


    public void setContacts(ArrayList<SerialNumber> serialNumbers) {
        this.serialNumbers = serialNumbers;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemCode,serialNumber,itemModel,itemPartNum,barcodeNumber,quatity;
        ImageButton btnDel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemCode = itemView.findViewById(R.id.itemCode);
            serialNumber = itemView.findViewById(R.id.serialNumber);
            btnDel = itemView.findViewById(R.id.ImgRecScanDel);

        }


    }


}



