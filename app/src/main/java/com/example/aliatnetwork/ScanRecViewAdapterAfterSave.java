package com.example.aliatnetwork;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScanRecViewAdapterAfterSave extends RecyclerView.Adapter<ScanRecViewAdapterAfterSave.ViewHolder> {
    Context context;
    private List<ScanList> scanList;
    EditText editText;
    String globalwareid;
    AlertDialog alertDialog;

    Connection conn;




    public ScanRecViewAdapterAfterSave(Context context, List<ScanList> scanList) {
        this.context = context;
        this.scanList = scanList;

        Intent intent = ((Activity) context).getIntent();
        String str = intent.getStringExtra("message_key");
        globalwareid = str.toString();

    }

    @NonNull
    @Override
    public ScanRecViewAdapterAfterSave.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.scan_item_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanRecViewAdapterAfterSave.ViewHolder holder, int position) {
        holder.itemCode.setText(scanList.get(position).getItem());
        holder.serialNumber.setText(scanList.get(position).getSerialNb());
        holder.barcodeNumber.setText(scanList.get(position).getBarcode());
        holder.quatity.setText(scanList.get(position).getQuantity());

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connecttoDB();

                PreparedStatement stmtdelTicket = null;
                try {
                    // Delete Ticket_id

                    stmtdelTicket = conn.prepareStatement("delete from warehouse_scan_onsite where item_id='"+holder.itemCode.getText()+"' and barcode='"+holder.barcodeNumber.getText()+"' and SERIAL_NUMBER='"+holder.serialNumber.getText()+"'and quantity='"+holder.quatity.getText()+"' and ware_id='"+globalwareid+"'");
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }

                try {
                    stmtdelTicket.executeUpdate();
                    Toast.makeText (context ,"Delete Completed",Toast.LENGTH_SHORT).show ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }


                try {
                    stmtdelTicket.close();
                    conn.close ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }

                scanList.remove(position);
                notifyDataSetChanged();
                notifyItemChanged(position);




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
        TextView itemCode,serialNumber,itemModel,itemPartNum,barcodeNumber,quatity;
        ImageButton btnDel,btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemCode = itemView.findViewById(R.id.itemCode);
            serialNumber = itemView.findViewById(R.id.serialNumber);
            barcodeNumber=itemView.findViewById(R.id.barcodeNumber);
            quatity=itemView.findViewById(R.id.quatity);
            btnDel=itemView.findViewById(R.id.ImgRecScanDel);
            btnEdit=itemView.findViewById(R.id.ImgRecScanEdit);


        }


    }

    public void connecttoDB() {
        // connect to DB
        OraDB oradb = new OraDB();
        String url = oradb.getoraurl();
        String userName = oradb.getorausername();
        String password = oradb.getorapwd();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(url, userName, password);
            //Toast.makeText (context,"Connected to the database",Toast.LENGTH_SHORT).show ();
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println("error is: " + e.toString());
            Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " + e.toString());
            Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
        } catch (java.lang.InstantiationException e) {
            System.out.println("error is: " + e.toString());
            Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }









}



