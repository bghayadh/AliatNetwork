package com.example.aliatnetwork;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
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

import static com.example.aliatnetwork.R.layout.fragment_imagefragment;
import static com.example.aliatnetwork.R.layout.fragment_infofragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Infofragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Infofragment extends Fragment {
    public Connection conn;
    private GpsTracker gpsTracker;
    private String globalwareid;
    private double vLatitude,vLongitude;

    ///added for pass data in fragment
    private SendMessage sendMessage;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Infofragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Infofragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Infofragment newInstance(String param1, String param2) {
        Infofragment fragment = new Infofragment ( );
        Bundle args = new Bundle ( );
        args.putString (ARG_PARAM1, param1);
        args.putString (ARG_PARAM2, param2);
        fragment.setArguments (args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        if (getArguments ( ) != null) {
            mParam1 = getArguments ( ).getString (ARG_PARAM1);
            mParam2 = getArguments ( ).getString (ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate (fragment_imagefragment, container, false);

        View V = inflater.inflate (fragment_infofragment, container, false);
        Button btnsave = (Button) V.findViewById(R.id.btnsave);
        Button btndelete = (Button) V.findViewById(R.id.btndelete);
        Button btnmain = (Button) V.findViewById(R.id.btnmain);
        TextView editTextwareName= (TextView) V.findViewById(R.id.editTextwareName);
        TextView editTextsiteid= (TextView) V.findViewById(R.id.editTextsiteid);
        TextView editTextaddress= (TextView) V.findViewById(R.id.editTextaddress);
        TextView editTextlongitude= (TextView) V.findViewById(R.id.editTextlongitude);
        TextView editTextlatitude= (TextView) V.findViewById(R.id.editTextlatitude);
        CheckBox checktech2G= (CheckBox) V.findViewById(R.id.checktech2G);
        CheckBox checktech3G= (CheckBox) V.findViewById(R.id.checktech3G);
        CheckBox checktech4G= (CheckBox) V.findViewById(R.id.checktech4G);
        CheckBox checktech5G= (CheckBox) V.findViewById(R.id.checktech5G);
        CheckBox checksite= (CheckBox) V.findViewById(R.id.checksite);

        //read passes value of ware_id from recylserview

        Intent intent = getActivity ().getIntent();
        String str = intent.getStringExtra("message_key");
        globalwareid = str.toString();





        // @RequiresApi(api = Build.VERSION_CODES.P)
        if (globalwareid.equalsIgnoreCase ("0")) {
            // call class Gps to get our location
            gpsTracker = new GpsTracker (getActivity ( ));
            if (gpsTracker.canGetLocation ( )) {
                double latitude = gpsTracker.getLatitude ( );
                double longitude = gpsTracker.getLongitude ( );
                editTextlatitude.setText(String.valueOf(latitude));
                editTextlongitude.setText(String.valueOf(longitude));
            } else {
                gpsTracker.showSettingsAlert ( );
                editTextlatitude.setText("Error to get latitude");
                editTextlatitude.setText("Error to get longitude");
            }
        }


        //Display warehouse selected
        // connect to Oracle DB
        connecttoDB();
        Statement stmt1 = null;
        try {
            stmt1 = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String  sqlStmt = "select * from WAREHOUSE where WARE_ID ='" + globalwareid +"'  and SITE='1' ";

        ResultSet rs1 = null;
        try {
            rs1 = stmt1.executeQuery(sqlStmt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!rs1.next()) break;
                editTextwareName.setText (rs1.getString("WARE_NAME"));
                editTextsiteid.setText (rs1.getString("SITE_ID"));
                editTextlatitude.setText (rs1.getString("LATITUDE"));
                editTextlongitude.setText (rs1.getString("LONGITUDE"));
                editTextaddress.setText (rs1.getString("ADDRESS"));

                if (rs1.getString("TECH_2G").equalsIgnoreCase ("1")) {
                    checktech2G.setChecked (true);
                } else {checktech2G.setChecked (false);}

                if (rs1.getString("TECH_3G").equalsIgnoreCase ("1")) {
                    checktech3G.setChecked (true);
                } else {checktech3G.setChecked (false);}

                if (rs1.getString("TECH_4G").equalsIgnoreCase ("1")) {
                    checktech4G.setChecked (true);
                } else {checktech4G.setChecked (false);}

                if (rs1.getString("TECH_5G").equalsIgnoreCase ("1")) {
                    checktech5G.setChecked (true);
                } else {checktech5G.setChecked (false);}

                if (rs1.getString("SITE").equalsIgnoreCase ("1")) {
                    checksite.setChecked (true);
                } else {checksite.setChecked (false);}

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

        //call the get site map
        getSiteMap(editTextlatitude.getText().toString(),editTextlongitude.getText().toString());



        /// save button
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tech2G ,tech3G,tech4G,tech5G,chksite;
                Date date = new Date();
                Calendar calendar = new GregorianCalendar ();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                String wareID;
                wareID= "WARE_"+year+"_" ;

                if (checktech2G.isChecked ( ) == true) {
                    tech2G=1;
                }else
                {
                    tech2G=0;
                }

                if (checktech3G.isChecked ( ) == true) {
                    tech3G=1;
                }else
                {
                    tech3G=0;
                }

                if (checktech4G.isChecked ( ) == true) {
                    tech4G=1;
                }else
                {
                    tech4G=0;
                }

                if (checktech5G.isChecked ( ) == true) {
                    tech5G=1;
                }else
                {
                    tech5G=0;
                }

                if (checksite.isChecked ( ) == true) {
                    chksite=1;
                }else
                {
                    chksite=0;
                }

                // connect to Oracle DB
                connecttoDB();

                PreparedStatement stmtinsert1 = null;

                try {
                    // if it is a new Warehouse we will use insert
                    if (globalwareid.equalsIgnoreCase("0")) {

                        Statement stmt1 = null;
                        stmt1 = conn.createStatement ( );
                        String sqlStmt = "select WAREHOUSE_SEQ.nextval as nbr from dual";
                        ResultSet rs1 = null;
                        try {
                            rs1 = stmt1.executeQuery (sqlStmt);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace ( );
                        }

                        while (true) {
                            try {
                                if (!rs1.next ( )) break;
                                globalwareid=wareID+rs1.getString ("nbr");
                                //System.out.println(rs1.getString("compteur"));

                            } catch (SQLException throwables) {
                                throwables.printStackTrace ( );
                            }
                        }
                        rs1.close();
                        stmt1.close();

                        // send data from fragment to super activity
                        ((SiteInfoActivity)getActivity()).getfromfragment(globalwareid);

                        stmtinsert1 = conn.prepareStatement("insert into WAREHOUSE (WARE_ID,CREATION_DATE,LAST_MODIFY_DATE,WARE_NAME,CITY,LONGITUDE,LATITUDE,SITE,SITE_ID,TECH_2G,TECH_3G,TECH_4G,TECH_5G,AREA_ID,AREA_NAME,ADDRESS,CLUSTER_ID,CLUSTER_NAME) values " +
                                "('"+globalwareid +"' ,sysdate, sysdate,'"+ editTextwareName.getText()  +"', '0', '"+ editTextlongitude.getText ()  +"', '"+ editTextlatitude.getText ()  +"','"+chksite +"','"+ editTextsiteid.getText ()  +"','"+tech2G +"','"+tech3G +"','"+tech4G +"','"+tech5G +"','0','0','"+ editTextaddress.getText ()  +"','0','0')");

                     /*   Bundle bundle = new Bundle();
                        bundle.putString("key1", globalwareid);
                        Imagefragment imgfr = new Imagefragment();
                        imgfr.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.infofragment,imgfr).commit();*/

                        ///added for pass data in fragment
                        sendMessage.sendData(globalwareid);

                    }

                    else { // we wil use update where wareid= the one we selected
                        stmtinsert1 = conn.prepareStatement("update  WAREHOUSE  set LAST_MODIFY_DATE=sysdate,WARE_NAME='"+ editTextwareName.getText()  +"',SITE_ID='"+ editTextsiteid.getText ()  +"',LONGITUDE='"+ editTextlongitude.getText ()  +"',LATITUDE='"+ editTextlatitude.getText ()  +"',SITE='"+chksite +"',TECH_2G='"+tech2G +"',TECH_3G='"+tech3G +"',TECH_4G='"+tech4G +"',TECH_5G='"+tech5G +"',ADDRESS='"+ editTextaddress.getText ()  +"' where WARE_ID ='" + globalwareid +"' ");
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }

                try {
                    stmtinsert1.executeUpdate();
                    Toast.makeText (getActivity (),"Saving Completed",Toast.LENGTH_SHORT).show ();

                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }


                try {
                    stmtinsert1.close();
                    conn.close ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }

                Intent intent = new Intent(getActivity(),SiteListViewActivity.class);
                startActivity(intent);

            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // connect to Oracle DB
                connecttoDB();

                PreparedStatement stmtinsert1 = null;
                try {
                    // Delete Ware_id

                    stmtinsert1 = conn.prepareStatement("delete  WAREHOUSE   where WARE_ID ='" + globalwareid +"' ");

                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }

                try {
                    stmtinsert1.executeUpdate();
                    Toast.makeText (getActivity (),"Delete Completed",Toast.LENGTH_SHORT).show ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }


                try {
                    stmtinsert1.close();
                    conn.close ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }

                Intent intent = new Intent(getActivity(),SiteListViewActivity.class);
                startActivity(intent);

            }





        });


        //// return to main page
        btnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });



        // after you've done all your manipulation, return your layout to be shown
        return V;
    }

    public void getSiteMap(String lat,String lng)
    {
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        //Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                // get the latitude and longitude



                // convert into Double
                vLatitude = Double.parseDouble(lat);
                vLongitude = Double.parseDouble(lng);
                LatLng location = new LatLng(vLatitude, vLongitude);


                //set position and title for marker
                googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(vLatitude+" : "+vLongitude));

                //animating to zoom the marker
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,10));


            }




        });
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



    ///added for pass data in fragment
    interface SendMessage {
        void sendData(String message);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            sendMessage = (SendMessage) getActivity();
        }
        catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

}