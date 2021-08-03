package com.example.aliatnetwork;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
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
import java.util.Calendar;
import java.util.function.BinaryOperator;

import oracle.jdbc.proxy.annotation.Post;

public class SiteListViewActivity extends AppCompatActivity {

    // SiteListViewActivity for the layout sitelistview
    final Calendar cldr = Calendar.getInstance();
    int day = cldr.get(Calendar.DAY_OF_MONTH);
    int month = cldr.get(Calendar.MONTH);
    int year = cldr.get(Calendar.YEAR);
    private RecyclerView sitesRecView;
    private int arraysize=0;
    private int varraysize=0;
    private int pagination=0;
    public Connection connsite;
    public ImageButton imgBtnDate;
    public TextView txtDate;
    public ArrayList<Sitelistview> sites,sitedb;
    private Button btnprevious,btnnext,btnnew,btnmain;
    private DatePickerDialog pickerDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.sitelistview);
        btnprevious = findViewById (R.id.btnprevious);
        btnnext = findViewById (R.id.btnnext);
        btnnew= findViewById (R.id.btnnew);
        btnmain=findViewById (R.id.BtnMain);
        imgBtnDate=findViewById(R.id.btnImaDateSite);
        txtDate=findViewById(R.id.txtInfoSaveSite);


        // get sites data by default
        GetSitesData(1,10);

        //btninfo  how to send variable from one activity ot other activity
        // btninfo.setOnClickListener (new View.OnClickListener ( ) {
        //     @Override
        //     public void onClick(View v) {
        //          // to call siteinfo
        //           openSiteInfoActivity();
        //      }
        //     public void openSiteInfoActivity(){
        //         Intent intent = new Intent(SiteListViewActivity.this, SiteInfoActivity.class);
        //         intent.putExtra("message_key", "hi ALl there");
        //         startActivity(intent);
        //     }
        //  });


        btnnew.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                // call bla(Vnk new page of siteinfo
                openSiteInfoActivity();

            }
            public void openSiteInfoActivity(){
                Intent intent = new Intent(SiteListViewActivity.this, SiteInfoActivity.class);
                intent.putExtra("message_key", "0");
                startActivity(intent);
            }

        });

        imgBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDialog = new DatePickerDialog (SiteListViewActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String varmonth;

                                //reset recycler
                                sites.clear ();
                                varraysize=0;
                                pagination=0;
                                sitesRecView.setAdapter(null);
                                sitesRecView.refreshDrawableState ();
                                ///////

                                //fil date in texdate as yyy-mm0-dd
                                String varyear=year + "-" + (monthOfYear + 1) + "-"+dayOfMonth;
                                txtDate.setText(varyear);
                                //editTextDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                // get data by defaukt based on editTextDate
                                GetSitesData(1,10);
                            }
                        }, year, month, day);
                pickerDialog.show();
            }
        });

        //button previuos
        btnprevious.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                pagination=pagination-2;
                if (pagination <=0 ) {pagination=0;}
                GetSitesData((pagination *10)+1,(pagination*10)+10);
            }

        });


        //button Next
        btnnext.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                GetSitesData((pagination*10)+1,(pagination*10)+10);
            }
        });

        //// return to main page
        btnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void GetSitesData(int vfrom, int vto) {
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
            Toast.makeText (SiteListViewActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (SiteListViewActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (InstantiationException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (SiteListViewActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }



        // define recyclerview of sitelistview
        sitesRecView=findViewById(R.id.sitesRecView);
        sites =new ArrayList<>();
        sitedb=new ArrayList<>();

        //Add data for sitelistview recyclerview
        Statement stmt1 = null;
        int i=0;
        try {
            stmt1 = connsite.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String vardate = txtDate.getText().toString();
        String sqlStmt;

        if (vardate.isEmpty()){
            sqlStmt = "SELECT * FROM (select ROW_NUMBER() OVER (ORDER BY WARE_ID) row_num,WARE_ID,SITE_ID,WARE_NAME,ADDRESS, LATITUDE,LONGITUDE from WAREHOUSE where SITE='1' order by SITE_ID) T WHERE row_num >= '" + vfrom +"' AND row_num <='" + vto +"'";
        }
        else if (vardate.length()>0)
        {
            sqlStmt = "select * from(select ROW_NUMBER() OVER (ORDER BY WARE_ID) row_num,WARE_ID,SITE_ID,WARE_NAME,ADDRESS, LATITUDE,LONGITUDE from WAREHOUSE where SITE='1' and TO_DATE(TO_CHAR(CREATION_DATE, 'YYYY-MM-DD'),'YYYY-MM-DD') =TO_DATE('" + vardate + "', 'YYYY-MM-DD') ) T WHERE row_num >= '" + vfrom + "' AND row_num <='" + vto + "'";

        }
        else{
            sqlStmt = "SELECT * FROM (select ROW_NUMBER() OVER (ORDER BY WARE_ID) row_num,TICKET_ID,SITE_ID,SITE_NAME,DESCRIPTION,SUBJECT,STATUS from TROUBLE_TICKETS where TO_CHAR(CREATION_DATE, 'YYYY-MM-DD') = TO_CHAR(SYSDATE, 'YYYY-MM-DD') order by CREATION_DATE DESC) T WHERE row_num >= '" + vfrom + "' AND row_num <='" + vto + "'";
        }


        ResultSet rs1 = null;
        try {
            rs1 = stmt1.executeQuery(sqlStmt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!rs1.next()) break;
                arraysize=arraysize+1;
                sitedb.add(new Sitelistview (rs1.getString("WARE_ID"),rs1.getString("SITE_ID"),rs1.getString("WARE_NAME"),rs1.getString("ADDRESS"),rs1.getString("LATITUDE"),rs1.getString("LONGITUDE")));
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

        arraysize=sitedb.size ();

        if (arraysize >0) {
            //System.out.println("Array Size is : "+arraysize);
            sites.clear ( );
            varraysize = 0;
            for (i = varraysize; i < 10; i++) {
                if (varraysize < arraysize) {
                    sites.add (new Sitelistview (sitedb.get (i).getWAREID ( ), sitedb.get (i).getSITEID ( ), sitedb.get (i).getWARENAME ( ), sitedb.get (i).getWADDRESS ( ), sitedb.get (i).getWARELAT ( ), sitedb.get (i).getWARELNG ( )));
                    varraysize = varraysize + 1;
                }
            }
            pagination = pagination + 1;
            //connect data to coveragelistadapter
            SiteRecViewAdapter adapter = new SiteRecViewAdapter (SiteListViewActivity.this);
            adapter.setContacts (sites);
            sitesRecView.setAdapter (adapter);
            sitesRecView.setLayoutManager (new LinearLayoutManager (SiteListViewActivity.this));
        }
    }

    @Override
    protected void onRestart() {
        this.recreate();
        super.onRestart();
    }
}
