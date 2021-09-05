package com.example.aliatnetwork;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScanRecViewAdapter extends RecyclerView.Adapter<ScanRecViewAdapter.ViewHolder> {
    Context context;
    private ArrayList<ScanList> scanList;
    EditText editText;
    String globalwareid;
    AlertDialog alertDialog;
    private onEditClickListener onEditClickListener;

    //public onEditListenner onEditListenner;


    Connection conn;


    public ScanRecViewAdapter(Context context, ArrayList<ScanList> scanList) {
        this.context = context;
        this.scanList = scanList;


        Intent intent = ((Activity) context).getIntent();
        String str = intent.getStringExtra("message_key");
        globalwareid = str.toString();

    }
  /*  public interface onEditListenner{
        void onEditClick(ScanList scanListEdit,int position);

    }
    public void editData(ScanList scanListObj, int currentPosition){

        scanList.get(currentPosition).setItem(scanListObj.getItem());
        scanList.get(currentPosition).setSerialNb(scanListObj.getSerialNb());
        scanList.get(currentPosition).setBarcode(scanListObj.getBarcode());
        scanList.get(currentPosition).setQuantity(scanListObj.getQuantity());
        notifyDataSetChanged();

    }*/

    @NonNull
    @Override
    public ScanRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.scan_item_list, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ScanRecViewAdapter.ViewHolder holder, int position) {

        holder.itemCode.setText(scanList.get(position).getItem());
        holder.serialNumber.setText(scanList.get(position).getSerialNb());
        holder.barcodeNumber.setText(scanList.get(position).getBarcode());
        holder.quatity.setText(scanList.get(position).getQuantity());






        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanList.remove(position);
                notifyDataSetChanged();



            }
        });


        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.edit_scan, null);

                MaterialButton btnClose = view.findViewById(R.id.btnClose);
                Button btnEdit = view.findViewById(R.id.btnUpdate);
                EditText item = view.findViewById(R.id.itemCode);
                EditText barcod = view.findViewById(R.id.barcodeNumber);
                EditText serial = view.findViewById(R.id.serialNumber);
                EditText quantity = view.findViewById(R.id.quatity);

                item.setText(scanList.get(position).getItem());
                barcod.setText(scanList.get(position).getBarcode());
                serial.setText(scanList.get(position).getSerialNb());
                quantity.setText(scanList.get(position).getQuantity());


                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        holder.itemCode.setText(item.getText());
                        holder.serialNumber.setText(serial.getText());
                        holder.barcodeNumber.setText(barcod.getText());
                        holder.quatity.setText(quantity.getText());




                        alertDialog.cancel();

                        System.out.println("getItem  " + holder.itemCode.getText());
                        System.out.println("getSerial  " + holder.serialNumber.getText());
                        System.out.println("getBarc  " + holder.barcodeNumber.getText());


                    }
                });


                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
                builder.setCancelable(false);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.show();

/*                System.out.println("getItem  "+holder.itemCode.getText());
                System.out.println("getSerial  "+holder.serialNumber.getText());
                System.out.println("getBarc  "+holder.barcodeNumber.getText());*/


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
        TextView itemCode, serialNumber, itemModel, itemPartNum, barcodeNumber, quatity;
        ImageButton btnDel, btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemCode = itemView.findViewById(R.id.itemCode);
            serialNumber = itemView.findViewById(R.id.serialNumber);
            barcodeNumber = itemView.findViewById(R.id.barcodeNumber);
            quatity = itemView.findViewById(R.id.quatity);
            btnDel = itemView.findViewById(R.id.ImgRecScanDel);
            btnEdit = itemView.findViewById(R.id.ImgRecScanEdit);


        }


    }

    public interface onEditClickListener{

    }




}



