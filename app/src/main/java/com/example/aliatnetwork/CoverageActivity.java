package com.example.aliatnetwork;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
public class CoverageActivity extends AppCompatActivity {

    final Calendar cldr = Calendar.getInstance();
    int day = cldr.get(Calendar.DAY_OF_MONTH);
    int month = cldr.get(Calendar.MONTH);
    int year = cldr.get(Calendar.YEAR);
    // CoverageActivity for the layout coveragelist
    private RecyclerView coveragesRecView;
    private int arraysize=0;
    private int varraysize=0;
    private int pagination=0;
    public Connection conncov;
    public ArrayList<Coverage> coverages,coveragedb;
    private Button btnprevious,btnnext,btnrundate;
    private DatePickerDialog picker;
    private TextView editTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.coveragelist);
        btnrundate=findViewById (R.id.btnrundate);
        editTextDate=findViewById (R.id.editTextDate);
        btnprevious = findViewById (R.id.btnprevious);
        btnnext = findViewById (R.id.btnnext);

        // set date in textdate by default
        String varyear=year + "-" + (month+1 ) + "-"+day;
        editTextDate.setText(varyear);

        // get data by defaukt based on sysdate
        GetCoverageData();

        //button previous in speedlist
        btnprevious.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                int j=pagination;
                if (arraysize > 0) {
                    //System.out.println ("Previous now ");
                    int vprev=(varraysize - pagination) ;
                    if((vprev) >=10) {
                        pagination=0;
                        coverages.clear ();
                        //System.out.println("start previous is : "+vprev);
                        for (int i=(vprev-10);i<vprev;i++) {
                            coverages.add(new Coverage (coveragedb.get (i).getCovsignal (),coveragedb.get (i).getCovlat (),coveragedb.get (i).getCovlng (),coveragedb.get (i).getCovdate ()));
                            //varraysize=varraysize-1;
                            varraysize=vprev;
                            pagination=pagination+1;
                        }
                        CoverageRecViewAdapter adapter =new CoverageRecViewAdapter(CoverageActivity.this);
                        adapter.setContacts(coverages);
                        coveragesRecView.setAdapter(adapter);
                        coveragesRecView.setLayoutManager(new LinearLayoutManager (CoverageActivity.this));
                    } else {
                        varraysize=0;
                        pagination=0;

                        coverages.clear ();
                        for (int i=0;i<j;i++) {
                            varraysize=varraysize+1;
                            pagination=pagination+1;
                            coverages.add(new Coverage (coveragedb.get (i).getCovsignal (),coveragedb.get (i).getCovlat (),coveragedb.get (i).getCovlng (),coveragedb.get (i).getCovdate ()));
                        }
                        CoverageRecViewAdapter adapter =new CoverageRecViewAdapter(CoverageActivity.this);
                        adapter.setContacts(coverages);
                        coveragesRecView.setAdapter(adapter);
                        coveragesRecView.setLayoutManager(new LinearLayoutManager (CoverageActivity.this));
                    }

                }
            }
        });






        //button next in speedlist
        btnnext.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                //System.out.println ("Next now ");

                if(varraysize <arraysize) {
                    int vnext=0;
                    pagination=0;
                    if ((varraysize +10) >arraysize ) {
                        vnext=arraysize;
                    } else {
                        vnext=varraysize +10;
                    }
                    coverages.clear ();

                    for (int i=varraysize;i<vnext;i++) {
                        if(varraysize <arraysize) {
                            coverages.add(new Coverage (coveragedb.get (i).getCovsignal (),coveragedb.get (i).getCovlat (),coveragedb.get (i).getCovlng (),coveragedb.get (i).getCovdate ()));
                            varraysize=varraysize+1;
                            pagination=pagination+1;
                        }
                    }
                    CoverageRecViewAdapter adapter =new CoverageRecViewAdapter (CoverageActivity.this);
                    adapter.setContacts(coverages);
                    coveragesRecView.setAdapter(adapter);
                    coveragesRecView.setLayoutManager(new LinearLayoutManager (CoverageActivity.this));
                } else {
                    varraysize =arraysize;
                }

            }
        });

        // Button to select date and getdata based on date selection
        btnrundate.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {




                // date picker dialog
                picker = new DatePickerDialog (CoverageActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String varmonth;

                                //reset recycler
                                coverages.clear ();
                                varraysize=0;
                                pagination=0;
                                coveragesRecView.setAdapter(null);
                                coveragesRecView.refreshDrawableState ();
                                ///////

                                //fil date in texdate as yyy-mm0-dd
                                String varyear=year + "-" + (monthOfYear + 1) + "-"+dayOfMonth;
                                editTextDate.setText(varyear);
                                //editTextDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                // get data by defaukt based on editTextDate
                                GetCoverageData();
                            }
                        }, year, month, day);
                picker.show();

            }
        });

    }

    public void GetCoverageData() {
        // connect to DB
        OraDB oradb= new OraDB();
        String url = oradb.getoraurl ();
        String userName = oradb.getorausername ();
        String password = oradb.getorapwd ();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conncov = DriverManager.getConnection(url,userName,password);
            // Toast.makeText (SpeedActivity.this,"Connected to the database",Toast.LENGTH_SHORT).show ();
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println("error is: " +e.toString());
            Toast.makeText (CoverageActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (CoverageActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (InstantiationException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (CoverageActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }



        // define recyclerview of coveragelist
        coveragesRecView=findViewById(R.id.coveragesRecView);
        coverages =new ArrayList<>();
        coveragedb=new ArrayList<>();
        //Add data for sppedlist recyclerview
        // Call Speed test DATA for display
        Statement stmt1 = null;
        int i=0;
        try {
            stmt1 = conncov.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String vardate=editTextDate.getText ().toString ();
        String sqlStmt;
        //System.out.println ("started with "+ vardate.length () );
        if (vardate.length () >0) {
           // System.out.println ("on click "+ vardate );
            sqlStmt = "select COVERAGE_SIGNAL,COVERAGE_LAT,COVERAGE_LNG,TO_DATE(TO_CHAR(COVERAGE_DATE, 'YYYY-MM-DD'),'YYYY-MM-DD') as COVERAGE_DATE from COVERAGETEST  where TO_DATE(TO_CHAR(COVERAGE_DATE, 'YYYY-MM-DD'),'YYYY-MM-DD') =TO_DATE('"+ vardate +"', 'YYYY-MM-DD') ";
           // System.out.println (sqlStmt);
        } else {
           // System.out.println ("on open "+ vardate );
            sqlStmt = "select COVERAGE_SIGNAL,COVERAGE_LAT,COVERAGE_LNG,COVERAGE_DATE from COVERAGETEST where TO_CHAR(COVERAGE_DATE, 'YYYY-MM-DD') = TO_CHAR(SYSDATE, 'YYYY-MM-DD') order by COVERAGE_DATE DESC";
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
                //System.out.println(rs1.getString("SPEED_DOWNLOAD") +" "+ rs1.getString("SPEED_UPLOAD") +" "+ rs1.getString("SPEED_DATE"));
                coveragedb.add(new Coverage (rs1.getString("COVERAGE_SIGNAL"),rs1.getString("COVERAGE_LAT"),rs1.getString("COVERAGE_LNG"),rs1.getString("COVERAGE_DATE")));
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
            conncov.close ();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        arraysize=coveragedb.size ();
        //System.out.println("Array Size is : "+arraysize);
        coverages.clear ();
        varraysize=0;
        for ( i=varraysize;i<10;i++) {
            if(varraysize <arraysize) {
                coverages.add(new Coverage (coveragedb.get (i).getCovsignal (),coveragedb.get (i).getCovlat (),coveragedb.get (i).getCovlng (),coveragedb.get (i).getCovdate ()));
                varraysize=varraysize+1;
                pagination=pagination+1;
                // System.out.println("Page Array Size is : "+varraysize);
            }
        }


        //connect data to coveragelistadapter
        CoverageRecViewAdapter adapter =new CoverageRecViewAdapter(CoverageActivity.this);
        adapter.setContacts(coverages);
        coveragesRecView.setAdapter(adapter);
        coveragesRecView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        coveragesRecView.setLayoutManager(new LinearLayoutManager (CoverageActivity.this));
    }
}
