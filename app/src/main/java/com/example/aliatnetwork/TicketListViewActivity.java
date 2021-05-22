package com.example.aliatnetwork;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.BinaryOperator;

import oracle.jdbc.proxy.annotation.Post;

import androidx.recyclerview.widget.RecyclerView;

public class TicketListViewActivity extends AppCompatActivity {

    private RecyclerView TicketsRecView;
    private int arraysize = 0;
    private int varraysize = 0;
    private int pagination = 0;
    public Connection connsite;
    public ArrayList<TicketListView> tickets, ticketDb;
    private Button btnprevious, btnnext, btnnew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticketlistview);
        btnprevious = findViewById(R.id.btnprevious);
        btnnext = findViewById(R.id.btnnext);
        btnnew = findViewById(R.id.btnnew);

        GetTicketsData(1,10); // get ticket data by default

            // start btnNew
        btnnew.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                // call blank new page of TicketInfo
                openTicketInfoActivity();

            }
            public void openTicketInfoActivity(){
                Intent intent = new Intent(TicketListViewActivity.this, TicketInfoActivity.class);
                intent.putExtra("message_key", "0");
                startActivity(intent);
            }
        });

        // End btn new

        //Start Btn previews
        btnprevious.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                pagination=pagination-2;
                if (pagination <=0 ) {pagination=0;}
                GetTicketsData((pagination *10)+1,(pagination*10)+10);
            }

        });
        // end btn previews

        btnnext.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                GetTicketsData((pagination*10)+1,(pagination*10)+10);
            }
        });
        ///end btn next

    }

    public void GetTicketsData(int vfrom, int vto) {
        // connect to DB
        OraDB oradb= new OraDB();
        String url = oradb.getoraurl ();
        String userName = oradb.getorausername ();
        String password = oradb.getorapwd ();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            connsite = DriverManager.getConnection(url,userName,password);
            // Toast.makeText (SpeedActivity.this,"Connected to the database",Toast.LENGTH_SHORT).show ();
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println("error is: " +e.toString());
            Toast.makeText (TicketListViewActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (TicketListViewActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (InstantiationException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (TicketListViewActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }



        // define recyclerview of ticketlistView
        TicketsRecView=findViewById(R.id.TicketsRecView);
        tickets =new ArrayList<>();
        ticketDb=new ArrayList<>();
        //Add data for ticketlistView recyclerview

        Statement stmt1 = null;
        int i=0;
        try {
            stmt1 = connsite.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String  sqlStmt = "select * from(select ROW_NUMBER() OVER (ORDER BY TICKET_ID) row_num, TROUBLE_TICKETS.TICKET_ID,TROUBLE_TICKETS.DEPARTMENT,TROUBLE_TICKETS.SUBJECT,TROUBLE_TICKETS.STATUS,WAREHOUSE.LONGITUDE,WAREHOUSE.LATITUDE \n" +
                "from TROUBLE_TICKETS INNER JOIN warehouse ON trouble_tickets.site_id=warehouse.site_id order by TICKET_ID) T WHERE row_num >= '" + vfrom +"' AND row_num <='" + vto +"'";


        ResultSet rs1 = null;
        try {
            rs1 = stmt1.executeQuery(sqlStmt);
            System.out.println("======"+rs1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        while (true) {
            try {
                if (!rs1.next()) break;
                arraysize=arraysize+1;
                ticketDb.add(new TicketListView (rs1.getString("TICKET_ID"),rs1.getString("DEPARTMENT"),rs1.getString("SUBJECT"),rs1.getString("STATUS"),rs1.getString("LONGITUDE"),rs1.getString("LATITUDE")));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        try {
            rs1.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            stmt1.close();
            connsite.close ();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        arraysize=ticketDb.size ();
        if(arraysize>0){
            tickets.clear ();
            varraysize=0;

            for ( i=varraysize;i<10;i++){
                if(varraysize <arraysize) {
                    tickets.add(new TicketListView (ticketDb.get (i).getTICKET_ID (),ticketDb.get (i).getDEPARTMENT (),ticketDb.get (i).getSUBJECT (),ticketDb.get(i).getLONGITUDE(),ticketDb.get(i).getLATITUDE(),ticketDb.get(i).getSTATUS()));
                    varraysize=varraysize+1;

                    // System.out.println("Page Array Size is : "+varraysize);
                }
            }
            pagination=pagination+1;
            //connect data to coveragelistadapter
            TicketRecViewAdapter adapter =new TicketRecViewAdapter(TicketListViewActivity.this);
            adapter.setContacts(tickets);
            TicketsRecView.setAdapter(adapter);
            TicketsRecView.setLayoutManager(new LinearLayoutManager (TicketListViewActivity.this));
        }


    }

    @Override
    protected void onRestart() {
        this.recreate();
        super.onRestart();
    }
}

