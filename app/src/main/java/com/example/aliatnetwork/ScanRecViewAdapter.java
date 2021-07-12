package com.example.aliatnetwork;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScanRecViewAdapter extends RecyclerView.Adapter<ScanRecViewAdapter.ViewHolder> {
    Context context;
    List<ScanList> scanList;
    EditText editText;




    Connection conn;


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

                Integer pos = (Integer) holder.itemView.getTag();
                String barcode = scanList.get(position).getBarcode().toString();
                String serialnb = scanList.get(position).getSerialNb().toString();
                new AlertDialog.Builder(context)
                        .setTitle("Are You Sure You Want To Delete This ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                connecttoDB();

                                PreparedStatement stmtinsert1 = null;
                                try {
                                    // Delete Ware_id

                                    stmtinsert1 = conn.prepareStatement("delete  WAREHOUSE_SCAN_ONSITE  where BARCODE ='" + barcode + "' and SERIAL_NUMBER='"+serialnb+"'");

                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }

                                try {
                                    stmtinsert1.executeUpdate();
                                    // Toast.makeText(context, "Delete Completed", Toast.LENGTH_SHORT).show();
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }


                                try {
                                    stmtinsert1.close();
                                    conn.close();
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }


                                Intent intent = new Intent(context, SiteListViewActivity.class);
                                context.startActivity(intent);

                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
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


    public void connecttoDB() {
        // connect to DB
        OraDB oradb= new OraDB();
        String url = oradb.getoraurl ();
        String userName = oradb.getorausername ();
        String password = oradb.getorapwd ();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(url,userName,password);
            //Toast.makeText (context,"Connected to the database",Toast.LENGTH_SHORT).show ();
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println("error is: " +e.toString());
            Toast.makeText (context,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (context,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (java.lang.InstantiationException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (context,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }
    }
}


