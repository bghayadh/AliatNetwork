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

public class SiteListViewActivity extends AppCompatActivity {

    // SiteListViewActivity for the layout sitelistview
    private RecyclerView sitesRecView;
    private int arraysize=0;
    private int varraysize=0;
    private int pagination=0;
    public Connection connsite;
    public ArrayList<Sitelistview> sites,sitedb;
    private Button btnprevious,btnnext,btnnew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.sitelistview);
        btnprevious = findViewById (R.id.btnprevious);
        btnnext = findViewById (R.id.btnnext);
        btnnew= findViewById (R.id.btnnew);

        // get sites data by default
        GetSitesData();

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
                // call blank new page of siteinfo
                openSiteInfoActivity();

            }
            public void openSiteInfoActivity(){
                Intent intent = new Intent(SiteListViewActivity.this, SiteInfoActivity.class);
                intent.putExtra("message_key", "");
                startActivity(intent);
            }
        });

        //button previuos
        btnprevious.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                      int j=pagination;
                    if (arraysize > 0) {
                        //System.out.println ("Previous now ");
                        int vprev=(varraysize - pagination) ;
                        if((vprev) >=10) {
                            pagination=0;
                            sites.clear ();
                            //System.out.println("start previous is : "+vprev);
                            for (int i=(vprev-10);i<vprev;i++) {
                                sites.add(new Sitelistview (sitedb.get (i).getWAREID (),sitedb.get (i).getSITEID (),sitedb.get (i).getWARENAME (),sitedb.get (i).getWADDRESS (),sitedb.get (i).getWARELAT (),sitedb.get (i).getWARELNG ()));
                                //varraysize=varraysize-1;
                                varraysize=vprev;
                                pagination=pagination+1;
                            }

                           SiteRecViewAdapter adapter =new SiteRecViewAdapter(SiteListViewActivity.this);
                            adapter.setContacts(sites);
                            sitesRecView.setAdapter(adapter);
                            sitesRecView.setLayoutManager(new LinearLayoutManager (SiteListViewActivity.this));
                        } else {
                            varraysize=0;
                            pagination=0;
                            sites.clear ();
                            for (int i=0;i<j;i++) {
                                varraysize=varraysize+1;
                                pagination=pagination+1;
                                sites.add(new Sitelistview (sitedb.get (i).getWAREID (),sitedb.get (i).getSITEID (),sitedb.get (i).getWARENAME (),sitedb.get (i).getWADDRESS (),sitedb.get (i).getWARELAT (),sitedb.get (i).getWARELNG ()));
                            }
                            SiteRecViewAdapter adapter =new SiteRecViewAdapter(SiteListViewActivity.this);
                            adapter.setContacts(sites);
                            sitesRecView.setAdapter(adapter);
                            sitesRecView.setLayoutManager(new LinearLayoutManager (SiteListViewActivity.this));
                        }

                    }
                }

        });


        //button Next
        btnnext.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if(varraysize <arraysize) {
                    int vnext=0;
                    pagination=0;
                    if ((varraysize +10) >arraysize ) {
                        vnext=arraysize;
                    } else {
                        vnext=varraysize +10;
                    }
                    sites.clear ();

                    for (int i=varraysize;i<vnext;i++) {
                        if(varraysize <arraysize) {
                            sites.add(new Sitelistview (sitedb.get (i).getWAREID (),sitedb.get (i).getSITEID (),sitedb.get (i).getWARENAME (),sitedb.get (i).getWADDRESS (),sitedb.get (i).getWARELAT (),sitedb.get (i).getWARELNG ()));
                            varraysize=varraysize+1;
                            pagination=pagination+1;
                        }
                    }

                    SiteRecViewAdapter adapter =new SiteRecViewAdapter (SiteListViewActivity.this);
                    adapter.setContacts(sites);
                    sitesRecView.setAdapter(adapter);
                    sitesRecView.setLayoutManager(new LinearLayoutManager (SiteListViewActivity.this));
                } else {
                    varraysize =arraysize;
                }
            }
        });
    }

    public void GetSitesData() {
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

        String  sqlStmt = "select WARE_ID,SITE_ID, WARE_NAME,ADDRESS, LATITUDE,LONGITUDE from WAREHOUSE where SITE='1' order by SITE_ID";

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
        //System.out.println("Array Size is : "+arraysize);
        sites.clear ();
        varraysize=0;
        for ( i=varraysize;i<10;i++) {
            if(varraysize <arraysize) {
                sites.add(new Sitelistview (sitedb.get (i).getWAREID (),sitedb.get (i).getSITEID (),sitedb.get (i).getWARENAME (),sitedb.get (i).getWADDRESS (),sitedb.get (i).getWARELAT (),sitedb.get (i).getWARELNG ()));
                varraysize=varraysize+1;
                pagination=pagination+1;
                // System.out.println("Page Array Size is : "+varraysize);
            }
        }

        //connect data to coveragelistadapter
        SiteRecViewAdapter adapter =new SiteRecViewAdapter(SiteListViewActivity.this);
        adapter.setContacts(sites);
        sitesRecView.setAdapter(adapter);
        sitesRecView.setLayoutManager(new LinearLayoutManager (SiteListViewActivity.this));
    }



}
