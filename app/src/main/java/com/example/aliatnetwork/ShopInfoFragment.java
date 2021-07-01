package com.example.aliatnetwork;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShopInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopInfoFragment extends Fragment {

    public Connection conn;
    private String globalShopsID;
    private GpsTracker gpsTracker;
    private double sLatitude,sLongitude;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShopInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopInfoFragment newInstance(String param1, String param2) {
        ShopInfoFragment fragment = new ShopInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_shop_info,container,false);
        Button BtnShopSave = (Button) v.findViewById(R.id.btnShopsSave);
        Button BtnShopsDelete = (Button)  v.findViewById(R.id.btnShopsdelete);
        Button BtnShopsMain =(Button) v.findViewById(R.id.btnShopsmain);
        EditText editTextShopName =(EditText) v.findViewById(R.id.editTextShopName);
        EditText editTextOwner=(EditText) v.findViewById(R.id.editTextOwner);
        EditText editTextaddress=(EditText) v.findViewById(R.id.editTextaddress);
        EditText editTextShopslati=(EditText) v.findViewById(R.id.editTextShopslati);
        EditText editTextShopslong=(EditText) v.findViewById(R.id.editTextShopslong);

        Intent intent =getActivity().getIntent();
        String str =intent.getStringExtra("message_key");
        globalShopsID=str.toString();

        if (globalShopsID.equalsIgnoreCase("0")){
            gpsTracker=new GpsTracker(getActivity());
            if(gpsTracker.canGetLocation()){
                double slatitude =gpsTracker.getLatitude();
                double slongitude=gpsTracker.getLongitude();
                editTextShopslati.setText(String.valueOf(slatitude));
                editTextShopslong.setText(String.valueOf(slongitude));
            }else {
                gpsTracker.showSettingsAlert();
                editTextShopslati.setText("Error to get latitud");
                editTextShopslong.setText("Error to get latitud");
            }
        }
        connecttoDB();
        Statement stmt1 = null;
        try {
            stmt1=conn.createStatement();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }

        String sqlShowShops = "select *from SHOPS where SHOPS_ID='"+globalShopsID+"'";

        ResultSet rs1 = null;
        try {
            rs1=stmt1.executeQuery(sqlShowShops);
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }

        while (true){
            try {
                if (!rs1.next())break;
                editTextShopName.setText(rs1.getString("SHOP_NAME"));
                editTextOwner.setText(rs1.getString("OWNER"));
                editTextaddress.setText(rs1.getString("ADDRESS"));
                editTextShopslati.setText(rs1.getString("LATITUDE"));
                editTextShopslong.setText(rs1.getString("LONGTITUDE"));

            }catch (SQLException throwables){
                throwables.printStackTrace();
            }
        }
        try {
            rs1.close();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        try {
            stmt1.close();
            conn.close();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }

        getSiteMap(editTextShopslati.getText().toString(),editTextShopslong.getText().toString());

        BtnShopSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                String shopsId;
                shopsId= "SHOP"+year+"_" ;

                connecttoDB();

                PreparedStatement stmtSaveShops = null;
                try {
                    if(globalShopsID.equalsIgnoreCase("0")){
                        Statement stmt1=null;
                        stmt1=conn.createStatement();
                        String sqlStmt ="select SHOPS_SEQ.nextval as nbr from dual";
                        ResultSet rs1 = null;
                        try {
                            rs1 = stmt1.executeQuery(sqlStmt);
                        }catch (SQLException throwables){
                            throwables.printStackTrace();
                        }
                        while (true){
                            try {
                                if (!rs1.next())break;
                                globalShopsID=shopsId+rs1.getString("nbr");
                            }catch (SQLException throwables){
                                throwables.printStackTrace();
                            }
                        }
                        rs1.close();
                        stmt1.close();

                        ((ShopInfoActivity)getActivity()).getShopsFragment(globalShopsID);
                        stmtSaveShops=conn.prepareStatement("insert into SHOPS (SHOPS_ID,CREATE_DATE,LAST_MODIFIED_DATE,SHOP_NAME,OWNER,ADDRESS,LONGTITUDE,LATITUDE) values"+
                                "('"+globalShopsID+"',sysdate,sysdate,'"+editTextShopName.getText()+"','"+editTextOwner.getText()+"','"+editTextaddress.getText()+"','"+editTextShopslong.getText().toString()+"','"+editTextShopslati.getText().toString()+"')");

                    }
                    else {
                        stmtSaveShops=conn.prepareStatement("update SHOPS set LAST_MODIFIED_DATE=sysdate,SHOP_NAME='"+editTextShopName.getText()+"',OWNER='"+editTextOwner.getText()+"',ADDRESS='"+editTextaddress.getText()+"',LONGTITUDE='"+editTextShopslong.getText()+"',LATITUDE='"+editTextShopslati.getText()+"' where SHOPS_ID='"+globalShopsID+"'");
                    }
                }catch (SQLException throwables){
                    throwables.printStackTrace();
                }
                try {
                    stmtSaveShops.executeQuery();
                    Toast.makeText(getActivity(),"Saving Completed",Toast.LENGTH_SHORT).show();

                }catch (SQLException throwables){
                    throwables.printStackTrace();
                }
                try {
                    stmtSaveShops.close();
                    conn.close();
                }catch (SQLException throwables){
                    throwables.printStackTrace();
                }
                Intent intent = new Intent(getActivity(),shopslist.class);
                startActivity(intent);


            }
        });

        BtnShopsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connecttoDB();
                PreparedStatement stmtDeleteShops= null;
                try {
                    stmtDeleteShops=conn.prepareStatement("delete SHOPS where SHOPS_ID='"+globalShopsID+"'");

                }catch (SQLException throwables){
                    throwables.printStackTrace();
                }
                try {
                    stmtDeleteShops.executeQuery();
                    Toast.makeText (getActivity (),"Delete Completed",Toast.LENGTH_SHORT).show ();

                }catch (SQLException throwables){
                    throwables.printStackTrace();
                }
                try {
                    stmtDeleteShops.close();
                    conn.close();
                }catch (SQLException throwables){
                    throwables.printStackTrace();
                }
                Intent intent = new Intent(getActivity(),shopslist.class);
                startActivity(intent);
            }

        });






        //// return to main page
        BtnShopsMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });
        return v;
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
            //Toast.makeText (MainActivity.this,"Connected to the database",Toast.LENGTH_SHORT).show ();
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println("error is: " +e.toString());
            Toast.makeText (getActivity (),"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (getActivity (),"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (java.lang.InstantiationException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (getActivity (),"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }
    }

    public void getSiteMap(String lat,String lng)
    {
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map_shops);
        //Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                // get the latitude and longitude



                // convert into Double
                sLatitude = Double.parseDouble(lat);
                sLongitude = Double.parseDouble(lng);
                LatLng location = new LatLng(sLatitude, sLongitude);


                //set position and title for marker
                googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(sLatitude+" : "+sLongitude));

                //animating to zoom the marker
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,10));


            }




        });
    }
}