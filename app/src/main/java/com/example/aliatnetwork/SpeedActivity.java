package com.example.aliatnetwork;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
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

public class SpeedActivity extends AppCompatActivity {

    final Calendar cldr = Calendar.getInstance();
    int day = cldr.get(Calendar.DAY_OF_MONTH);
    int month = cldr.get(Calendar.MONTH);
    int year = cldr.get(Calendar.YEAR);
    // SpeedActivity for the layout Speedlist
    private RecyclerView speedsRecView;
    private int arraysize=0;
    private int varraysize=0;
    private int pagination=0;
    public Connection connspd;
    public ArrayList<Speed> speeds,speeddb;
    private Button btnprevious,btnnext,btnrundate;
    private DatePickerDialog picker;
    private TextView editTextDate;
    private Button btnmain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.speedlist);
        btnrundate=findViewById (R.id.btnrundate);
        editTextDate=findViewById (R.id.editTextDate);
         btnprevious = findViewById (R.id.btnprevious);
         btnnext = findViewById (R.id.btnnext);
         btnmain=findViewById(R.id.BtnMain);

         // set date in textdate by default
        String varyear=year + "-" + (month+1 ) + "-"+day;
        editTextDate.setText(varyear);

         // get data by defaukt based on sysdate
         GetSpeedData(1,10);

        //button previous in speedlist
        btnprevious.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                pagination=pagination-2;
                if (pagination <=0 ) {pagination=0;}
                GetSpeedData((pagination *10)+1,(pagination*10)+10);
            }
        });

        //button next in speedlist
        btnnext.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                GetSpeedData((pagination*10)+1,(pagination*10)+10);
            }
        });

        // Button to select date and getdata based on date selection
        btnrundate.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {




                // date picker dialog
                picker = new DatePickerDialog (SpeedActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String varmonth;

                                //reset recycler
                                speeds.clear ();
                                varraysize=0;
                                pagination=0;
                                speedsRecView.setAdapter(null);
                                speedsRecView.refreshDrawableState ();
                                ///////

                                //fil date in texdate as yyy-mm0-dd
                                String varyear=year + "-" + (monthOfYear + 1) + "-"+dayOfMonth;
                                editTextDate.setText(varyear);
                                //editTextDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                // get data by defaukt based on editTextDate
                                GetSpeedData(1,10);
                            }
                        }, year, month, day);
                picker.show();

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

   public void GetSpeedData(int vfrom, int vto) {
       // connect to DB
       OraDB oradb= new OraDB();
       String url = oradb.getoraurl ();
       String userName = oradb.getorausername ();
       String password = oradb.getorapwd ();

       try {
           StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
           StrictMode.setThreadPolicy(policy);
           Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
           connspd = DriverManager.getConnection(url,userName,password);
           // Toast.makeText (SpeedActivity.this,"Connected to the database",Toast.LENGTH_SHORT).show ();
       } catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
           System.out.println("error is: " +e.toString());
           Toast.makeText (SpeedActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
       } catch (IllegalAccessException e) {
           System.out.println("error is: " +e.toString());
           Toast.makeText (SpeedActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
       } catch (InstantiationException e) {
           System.out.println("error is: " +e.toString());
           Toast.makeText (SpeedActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
       }



       // define recyclerview of speedlist
       speedsRecView=findViewById(R.id.sppedsRecView);
       speeds =new ArrayList<>();
       speeddb=new ArrayList<>();
       //Add data for sppedlist recyclerview
       // Call Speed test DATA for display
       Statement stmt1 = null;
       int i=0;
       try {
           stmt1 = connspd.createStatement();
       } catch (SQLException throwables) {
           throwables.printStackTrace();
       }
       String vardate=editTextDate.getText ().toString ();
       String sqlStmt;
       //System.out.println ("started with "+ vardate.length () );
       if (vardate.length () >0) {
            sqlStmt = "SELECT * FROM (select ROW_NUMBER() OVER (ORDER BY speedtstid) row_num,SPEED_DOWNLOAD,SPEED_UPLOAD,SPEED_LAT,SPEED_LNG,TO_DATE(TO_CHAR(SPEED_DATE, 'YYYY-MM-DD'),'YYYY-MM-DD') as SPEED_DATE from SPEEDTEST  where TO_DATE(TO_CHAR(SPEED_DATE, 'YYYY-MM-DD'),'YYYY-MM-DD') =TO_DATE('"+ vardate +"', 'YYYY-MM-DD') ) T WHERE row_num >= '" + vfrom +"' AND row_num <='" + vto +"' ";
       } else {
            sqlStmt = "SELECT * FROM ( select ROW_NUMBER() OVER (ORDER BY speedtstid) row_num,SPEED_DOWNLOAD,SPEED_UPLOAD,SPEED_LAT,SPEED_LNG,SPEED_DATE from SPEEDTEST where TO_CHAR(SPEED_DATE, 'YYYY-MM-DD') = TO_CHAR(SYSDATE, 'YYYY-MM-DD') order by SPEED_DATE DESC ) T WHERE row_num >= '" + vfrom +"' AND row_num <='" + vto +"' ";
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
               speeddb.add(new Speed(rs1.getString("SPEED_DOWNLOAD"),rs1.getString("SPEED_UPLOAD"),rs1.getString("SPEED_LAT"),rs1.getString("SPEED_LNG"),rs1.getString("SPEED_DATE")));
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
           connspd.close ();
       } catch (SQLException throwables) {
           throwables.printStackTrace();
       }

       arraysize=speeddb.size ();

       if (arraysize >0)  {
               speeds.clear ();
               varraysize=0;
               for ( i=varraysize;i<10;i++) {
                   if(varraysize <arraysize) {
                       speeds.add(new Speed(speeddb.get (i).getSpddownload (),speeddb.get (i).getSpdupload (),speeddb.get (i).getSpdlat (),speeddb.get (i).getSpdlng (),speeddb.get (i).getSpddate ()));
                       varraysize=varraysize+1;

                   }
               }
                pagination=pagination+1;

               //connect data to speedlistadapter
               SpeedRecViewAdapter adapter =new SpeedRecViewAdapter(SpeedActivity.this);
               adapter.setContacts(speeds);
               speedsRecView.setAdapter(adapter);
               speedsRecView.setLayoutManager(new LinearLayoutManager (SpeedActivity.this));
       }


   }
}
