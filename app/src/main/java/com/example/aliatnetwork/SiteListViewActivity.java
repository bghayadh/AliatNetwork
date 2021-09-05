package com.example.aliatnetwork;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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
import java.util.List;
import java.util.function.BinaryOperator;

import oracle.jdbc.proxy.annotation.Post;

public class SiteListViewActivity extends AppCompatActivity {

    // SiteListViewActivity for the layout sitelistview
    private RecyclerView sitesRecView;
    private int arraysize = 0;
    private int varraysize = 0;
    private int pagination = 0;
    public Connection connsite;
    public ImageButton imgBtnDate;
    public TextView txtDate;
    public ArrayList<Sitelistview> sites, sitedb;
    public ArrayList<Sitelistview> getSites, getSitedb;
    private Button btnprevious, btnnext, btnnew, btnmain;
    private List<SiteId> siteIDAuto = new ArrayList<>();
    private ArrayList<SiteId> siteIdDbAuto = new ArrayList<>();
    public AutoCompleteTextView textSitID;
    public ImageButton search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sitelistview);
        btnprevious = findViewById(R.id.btnprevious);
        btnnext = findViewById(R.id.btnnext);
        btnnew = findViewById(R.id.btnnew);
        btnmain = findViewById(R.id.BtnMain);
        textSitID = findViewById(R.id.textSitID);
        sitesRecView = findViewById(R.id.sitesRecView);
        search=findViewById(R.id.searchSite);


        // get sites data by default
        GetSitesData(1, 10);
        AutoCompleteSite();


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetSitesData(1, 10);
                textSitID.setText("");
            }
        });

        btnnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call bla(Vnk new page of siteinfo
                openSiteInfoActivity();

            }

            public void openSiteInfoActivity() {
                Intent intent = new Intent(SiteListViewActivity.this, SiteInfoActivity.class);
                intent.putExtra("message_key", "0");
                startActivity(intent);
            }

        });



        //button previuos
        btnprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                pagination = pagination - 2;
                if (pagination <= 0) {
                    pagination = 0;
                }
                GetSitesData((pagination * 10) + 1, (pagination * 10) + 10);
            }

        });


        //button Next
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetSitesData((pagination * 10) + 1, (pagination * 10) + 10);
            }
        });

        //// return to main page
        btnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void GetSitesData(int vfrom, int vto) {
        // connect to DB
        ConnectDB();


        // define recyclerview of sitelistview


        sitesRecView = findViewById(R.id.sitesRecView);
        sites = new ArrayList<>();
        sitedb = new ArrayList<>();


        //Add data for sitelistview recyclerview
        Statement stmt1 = null;
        int i = 0;
        try {
            stmt1 = connsite.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String sqlStmt = "SELECT * FROM (select ROW_NUMBER() OVER (ORDER BY WARE_ID) row_num,WARE_ID,SITE_ID,WARE_NAME,ADDRESS, LATITUDE,LONGITUDE from WAREHOUSE where SITE='1' order by SITE_ID) T WHERE row_num >= '" + vfrom + "' AND row_num <='" + vto + "'";

        ResultSet rs1 = null;
        try {
            rs1 = stmt1.executeQuery(sqlStmt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!rs1.next()) break;
                arraysize = arraysize + 1;
                sitedb.add(new Sitelistview(rs1.getString("WARE_ID"), rs1.getString("SITE_ID"), rs1.getString("WARE_NAME"), rs1.getString("ADDRESS")));
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
            connsite.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        arraysize = sitedb.size();

        if (arraysize > 0) {
            //System.out.println("Array Size is : "+arraysize);
            sites.clear();
            varraysize = 0;
            for (i = varraysize; i < 10; i++) {
                if (varraysize < arraysize) {
                    sites.add(new Sitelistview(sitedb.get(i).getWAREID(), sitedb.get(i).getSITEID(), sitedb.get(i).getWARENAME(), sitedb.get(i).getWADDRESS()));
                    varraysize = varraysize + 1;
                }
            }
            pagination = pagination + 1;
            //connect data to coveragelistadapter
            SiteRecViewAdapter adapter = new SiteRecViewAdapter(SiteListViewActivity.this);
            adapter.setContacts(sites);
            sitesRecView.setAdapter(adapter);
            sitesRecView.setLayoutManager(new LinearLayoutManager(SiteListViewActivity.this));
        }
    }

    @Override
    protected void onRestart() {
        this.recreate();
        super.onRestart();
    }

    public void AutoCompleteSite() {


        // connect to DB
        ConnectDB();


        Statement stmt2 = null;
        int i = 0;
        try {
            stmt2 = connsite.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String sqlStmt = "select SITE_ID,WARE_NAME from WAREHOUSE";


        ResultSet rs1 = null;
        try {

            rs1 = stmt2.executeQuery(sqlStmt);
            System.out.println("ALiiiiiiiiiiiiiiiiiiiiii" + rs1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        while (true) {
            try {
                if (!rs1.next()) break;
                siteIDAuto.add(new SiteId(rs1.getString("SITE_ID"), rs1.getString("WARE_NAME")));


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
            stmt2.close();
            connsite.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        for (i = 0; i < siteIdDbAuto.size(); i++) {
            siteIDAuto.add(new SiteId(siteIdDbAuto.get(i).getSITE_ID(), siteIdDbAuto.get(i).getWARE_NAME()));

        }

        SiteIdAdapter adapter = new SiteIdAdapter(this, siteIDAuto);
        textSitID.setThreshold(1);
        textSitID.setAdapter(adapter);




            textSitID.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    Find();

                }

            });

            textSitID.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    GetSitesData(1,10);

                }
            });



    }


    public void ConnectDB() {
        // connect to DB
        OraDB oradb = new OraDB();
        String url = oradb.getoraurl();
        String userName = oradb.getorausername();
        String password = oradb.getorapwd();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            connsite = DriverManager.getConnection(url, userName, password);
            // Toast.makeText (SpeedActivity.this,"Connected to the database",Toast.LENGTH_SHORT).show ();
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println("error is: " + e.toString());
            Toast.makeText(SiteListViewActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " + e.toString());
            Toast.makeText(SiteListViewActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        } catch (InstantiationException e) {
            System.out.println("error is: " + e.toString());
            Toast.makeText(SiteListViewActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void Find() {

        ConnectDB();

        sites = new ArrayList<>();
        sitedb = new ArrayList<>();


        //Add data for sitelistview recyclerview
        Statement stmt1 = null;
        int i = 0;
        try {
            stmt1 = connsite.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String sqlStmt = "SELECT WARE_ID,SITE_ID,WARE_NAME,ADDRESS from WAREHOUSE where SITE='1' and SITE_ID='" + textSitID.getText().toString() + "' order by SITE_ID ";


        ResultSet rs1 = null;
        try {
            rs1 = stmt1.executeQuery(sqlStmt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!rs1.next()) break;
                sitedb.add(new Sitelistview(rs1.getString("WARE_ID"), rs1.getString("SITE_ID"), rs1.getString("WARE_NAME"), rs1.getString("ADDRESS")));
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
            connsite.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        for (i = 0; i < sitedb.size(); i++) {
            sites.add(new Sitelistview(sitedb.get(i).getWAREID(), sitedb.get(i).getSITEID(), sitedb.get(i).getWARENAME(), sitedb.get(i).getWADDRESS()));
        }

        //connect data to coveragelistadapter
        SiteRecViewAdapter adapter = new SiteRecViewAdapter(SiteListViewActivity.this);
        adapter.setContacts(sites);
        sitesRecView.setAdapter(adapter);
        sitesRecView.setLayoutManager(new LinearLayoutManager(SiteListViewActivity.this));


    }

    public void AutocompleteFree(){

        if(textSitID.length()==0)
        {
            GetSitesData(1,10);
        }
        return;

    }
}

