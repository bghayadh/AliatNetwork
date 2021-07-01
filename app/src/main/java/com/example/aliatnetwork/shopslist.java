package com.example.aliatnetwork;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class shopslist extends AppCompatActivity {

    private RecyclerView ShopsRecView;
    private int arraysize = 0;
    private int varraysize = 0;
    private int pagination = 0;
    public Connection conn;
    public ArrayList<ShopsListView> shopsListViews, shopsListViewsDb;
    private Button btnprevious, btnnext, btnnew,btnmain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopslist);
        btnprevious=findViewById(R.id.btnprevious);
        btnnext=findViewById(R.id.btnnext);
        btnmain=findViewById(R.id.BtnMain);
        btnnew=findViewById(R.id.btnnew);

        GetShopssData(1,10); // get shops data default
        btnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        btnnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShopInfoActivity();

            }
            public void openShopInfoActivity(){
                Intent intent = new Intent(shopslist.this,ShopInfoActivity.class);
                intent.putExtra("message_key","0");
                startActivity(intent);
            }
        });
        //Start Btn previews
        btnprevious.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                pagination=pagination-2;
                if (pagination <=0 ) {pagination=0;}
                GetShopssData((pagination *10)+1,(pagination*10)+10);
            }

        });
        // end btn previews

        btnnext.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                GetShopssData((pagination*10)+1,(pagination*10)+10);
            }
        });

        ///end btn next


    }



    public void GetShopssData(int vfrom, int vto) {


        OraDB oradb= new OraDB();
        String url = oradb.getoraurl ();
        String userName = oradb.getorausername ();
        String password = oradb.getorapwd ();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(url,userName,password);
            //Toast.makeText (MainActivity.this,"Connected to the database",Toast.LENGTH_SHORT).show ();
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println("error is: " +e.toString());
            Toast.makeText (shopslist.this ,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (shopslist.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (java.lang.InstantiationException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (shopslist.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }

        ShopsRecView=findViewById(R.id.ShopsRecView);

        shopsListViews =new ArrayList<>();
        shopsListViewsDb=new ArrayList<>();
        Statement stmt1 = null;
        int i=0;
        try {
            stmt1 = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String  sqlStmt = "SELECT * FROM (select ROW_NUMBER() OVER (ORDER BY SHOPS_ID) row_num,SHOPS_ID,SHOP_NAME,OWNER,ADDRESS,LATITUDE,LONGTITUDE from SHOPS order by SHOPS_ID) T WHERE row_num >= '" + vfrom +"' AND row_num <='" + vto +"'";
        //String  sqlStmt ="SELECT * FROM (select ROW_NUMBER() OVER (ORDER BY WARE_ID) row_num,WARE_ID,SITE_ID, WARE_NAME,ADDRESS, LATITUDE,LONGITUDE from WAREHOUSE where SITE='1' order by SITE_ID) T WHERE row_num >= '" + vfrom +"' AND row_num <='" + vto +"'";


        ResultSet rs1 = null;
        try {
            rs1 = stmt1.executeQuery(sqlStmt);
            System.out.println("======================================"+rs1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!rs1.next()) break;
                arraysize=arraysize+1;
                shopsListViewsDb.add(new ShopsListView (rs1.getString("SHOPS_ID"),rs1.getString("SHOP_NAME"),rs1.getString("OWNER"),rs1.getString("ADDRESS"),rs1.getString("LONGTITUDE"),rs1.getString("LATITUDE")));
                System.out.println(rs1.getString("SHOPS_ID"));
                System.out.println(rs1.getString("SHOP_NAME"));
                System.out.println(rs1.getString("OWNER"));
                System.out.println(rs1.getString("ADDRESS"));
                System.out.println(rs1.getString("LATITUDE"));
                System.out.println(rs1.getString("LONGTITUDE"));
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
            conn.close ();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        arraysize=shopsListViewsDb.size ();
        if(arraysize>0){
            shopsListViews.clear ();
            varraysize=0;

            for ( i=varraysize;i<10;i++){
                if(varraysize <arraysize) {
                    shopsListViews.add(new ShopsListView (shopsListViewsDb.get (i).getSHOPS_ID (),shopsListViewsDb.get (i).getSHOP_NAME (),shopsListViewsDb.get (i).getADDRESS (),shopsListViewsDb.get(i).getOWNER(),shopsListViewsDb.get(i).getLATITUDE(),shopsListViewsDb.get(i).getLONGTITUDE()));
                    varraysize=varraysize+1;

                    System.out.println("Page Array Size is : "+varraysize);
                }
            }
            pagination=pagination+1;
            //connect data to coveragelistadapter
            ShopsAdapter adapter =new ShopsAdapter(shopslist.this);
            adapter.setContacts(shopsListViews);
            ShopsRecView.setAdapter(adapter);
            ShopsRecView.setLayoutManager(new LinearLayoutManager(shopslist.this));
        }



    }
    @Override
    protected void onRestart() {
        this.recreate();
        super.onRestart();
    }
}
