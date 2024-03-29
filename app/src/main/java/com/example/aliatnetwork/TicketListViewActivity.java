package com.example.aliatnetwork;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
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
import java.util.Calendar;
import java.util.function.BinaryOperator;

import oracle.jdbc.proxy.annotation.Post;

import androidx.recyclerview.widget.RecyclerView;

public class TicketListViewActivity extends AppCompatActivity {

    final Calendar cldr = Calendar.getInstance();
    int day = cldr.get(Calendar.DAY_OF_MONTH);
    int month = cldr.get(Calendar.MONTH);
    int year = cldr.get(Calendar.YEAR);
    private RecyclerView TicketsRecView;
    private int arraysize = 0;
    private int varraysize = 0;
    private int pagination = 0;
    public Connection connsite;
    public ArrayList<TicketListView> tickets, ticketDb;
    private Button btnprevious, btnnext, btnnew,btnmain;
    private DatePickerDialog pickerDialog;
    public TextView editInfoSave;
    public Button btnImaDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticketlistview);
        btnprevious = findViewById(R.id.btnprevious);
        btnnext = findViewById(R.id.btnnext);
        btnnew = findViewById(R.id.btnnew);
        btnmain=findViewById (R.id.BtnMain);
        btnImaDate= findViewById(R.id.btnImaDate);
        editInfoSave = findViewById(R.id.txtInfoSave);

        if(getIntent() != null){
            String spinnerValue = getIntent().getStringExtra("Status");
        }




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
                intent.putExtra("Status","0");
                startActivity(intent);
            }
        });

        // Button to select date and getdata based on date selection
        btnImaDate.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {




                // date T dialog
                pickerDialog = new DatePickerDialog (TicketListViewActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String varmonth;

                                //reset recycler
                                tickets.clear ();
                                varraysize=0;
                                pagination=0;
                                TicketsRecView.setAdapter(null);
                                TicketsRecView.refreshDrawableState ();
                                ///////

                                //fil date in texdate as yyy-mm0-dd
                                String varyear=year + "-" + (monthOfYear + 1) + "-"+dayOfMonth;
                                editInfoSave.setText(varyear);
                                //editTextDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                // get data by defaukt based on editTextDate
                                GetTicketsData(1,10);
                            }
                        }, year, month, day);
                pickerDialog.show();

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

        //// return to main page
        btnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

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

        String  vardate = editInfoSave.getText().toString();
        String sqlStmt;
        if (vardate.isEmpty()){
            sqlStmt="select * from(select ROW_NUMBER() OVER (ORDER BY TICKET_ID) row_num, TICKET_ID,SITE_ID,SITE_NAME,DESCRIPTION,SUBJECT,STATUS from TROUBLE_TICKETS order by TICKET_ID) T WHERE row_num >= '" + vfrom +"' AND row_num <='" + vto +"'";

        }

        else if (vardate.length()>0) {
          sqlStmt = "select * from(select ROW_NUMBER() OVER (ORDER BY TICKET_ID) row_num, TICKET_ID,SITE_ID,SITE_NAME,DESCRIPTION,SUBJECT,STATUS from TROUBLE_TICKETS where TO_DATE(TO_CHAR(CREATION_DATE, 'YYYY-MM-DD'),'YYYY-MM-DD') =TO_DATE('" + vardate + "', 'YYYY-MM-DD') ) T WHERE row_num >= '" + vfrom + "' AND row_num <='" + vto + "'";

        }else {

            sqlStmt = "SELECT * FROM (select ROW_NUMBER() OVER (ORDER BY TICKET_ID) row_num,TICKET_ID,SITE_ID,SITE_NAME,DESCRIPTION,SUBJECT,STATUS from TROUBLE_TICKETS where TO_CHAR(CREATION_DATE, 'YYYY-MM-DD') = TO_CHAR(SYSDATE, 'YYYY-MM-DD') order by CREATION_DATE DESC) T WHERE row_num >= '" + vfrom + "' AND row_num <='" + vto + "'";


        }

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
                ticketDb.add(new TicketListView (rs1.getString("TICKET_ID"),rs1.getString("SITE_ID"),rs1.getString("SITE_NAME"),rs1.getString("STATUS"),rs1.getString("SUBJECT"), rs1.getString("DESCRIPTION")));

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
                    tickets.add(new TicketListView (ticketDb.get (i).getTICKET_ID (),ticketDb.get(i).getSITE_ID(),ticketDb.get(i).getSITE_NAME(),ticketDb.get (i).getSUBJECT (),ticketDb.get(i).getSTATUS(),ticketDb.get(i).getDESCRIPTION()));
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

