package com.example.aliatnetwork;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


import static com.example.aliatnetwork.R.layout.fragment_ticket_info;
import static com.example.aliatnetwork.R.layout.place_autocomplete_item_powered_by_google;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketInfoFragment extends Fragment {

    public Connection conn;
    private GpsTracker gpsTracker;
    private String globalTicketId;
    private List<SiteId> siteIDS = new ArrayList<>();
    private List<Clients> clientsList = new ArrayList<>();
    //private List<SiteId> siteName =new ArrayList<>();
    private ArrayList<SiteId>siteIdDb= new ArrayList<>();
    private ArrayList<Clients>clientDb= new ArrayList<>();

    private  String newlong;
    private String newlat;
    private double vLatitude;
    private double  vLongitude;
    private String lat;
    private String longi;
    final Calendar cldr = Calendar.getInstance();
    int day = cldr.get(Calendar.DAY_OF_MONTH);
    int month = cldr.get(Calendar.MONTH);
    int year = cldr.get(Calendar.YEAR);
    // CoverageActivity for the layout coveragelist
    private RecyclerView coveragesRecView;
    private int arraysize=0;
    private int varraysize=0;
    private int pagination=0;
    private DatePickerDialog.OnDateSetListener datePicker;
    private String ticketDate;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TicketInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketInfoFragment newInstance(String param1, String param2) {
        TicketInfoFragment fragment = new TicketInfoFragment();
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
        //return inflater.inflate(R.layout.fragment_ticket_info, container, false);

        View V = inflater.inflate(fragment_ticket_info, container, false);
        Button btnsave = (Button) V.findViewById(R.id.btnsave);
        Button btndelete = (Button) V.findViewById(R.id.btndelete);
        ImageButton btnDate = (ImageButton) V.findViewById(R.id.btnrundate);
        Button btnMain = (Button) V.findViewById(R.id.btnMain);
        AutoCompleteTextView editTxtSiteId = V.findViewById(R.id.editTextSitID);

        TextView editTxtSiteName = V.findViewById(R.id.editTxtSiteName);
        TextView editTxtRegion = V.findViewById(R.id.editTxtRegion);
        TextView editTxtDepa = V.findViewById(R.id.editTxtDepa);
        TextView editTxtSubject = V.findViewById(R.id.editTxtSubject);
        AutoCompleteTextView editTxtClient = V.findViewById(R.id.editTxtClient);
        TextView editTxtDesc = V.findViewById(R.id.editTxtDesc);
        TextView editTxtSer = V.findViewById(R.id.editTxtSer);
        TextView editTxtSerIs = V.findViewById(R.id.editTxtSerIs);
        TextView editTxtIssApp = V.findViewById(R.id.editTextDateTicket);

        /*DateFormat date = new SimpleDateFormat("MMM dd yyyy, h:mm");
        String dateformat = date.format(Calendar.getInstance().getTime());
        editTxtIssApp.setText(dateformat);*/

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int year =calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int days = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        android.R.style.Theme_DeviceDefault_Dialog,datePicker,year,month,days);
                dialog.show();
            }
        });
        datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month+1;
               // Log.d(TAG,"onDateSet: dd/mm/yyyy:"+dayOfMonth+"/"+month+"/"+year);

                String date = dayOfMonth +"/"+month+"/"+year;
                editTxtIssApp.setText(date);
                ticketDate = editTxtIssApp.getText().toString();
                System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"+ticketDate);

            }
        };
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });




        //read passes value of Site_ID from recylserview

        Intent intent = getActivity().getIntent();
        String str = intent.getStringExtra("message_key");
        globalTicketId = str.toString();


        ///begin of Site AUTO-Complete////
        connecttoDB();
        Statement stmt2 = null;
        int i = 0;
        try {
            stmt2 = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String sqlStmt = "select SITE_ID,WARE_NAME from WAREHOUSE";


        ResultSet rs1 = null;
        try {

            rs1 = stmt2.executeQuery(sqlStmt);
            System.out.println("ALiiiiiiiiiiiiiiiiiiiiii"+rs1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        while (true) {
            try {
                if (!rs1.next()) break;
                siteIdDb.add(new SiteId(rs1.getString("SITE_ID"),rs1.getString("WARE_NAME")));



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
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        for (i=0;i<siteIdDb.size();i++) {
        siteIDS.add(new SiteId(siteIdDb.get(i).getSITE_ID(),siteIdDb.get(i).getWARE_NAME()));
        
        }

        SiteIdAdapter  adapter = new SiteIdAdapter(getActivity(),siteIDS);
        editTxtSiteId.setThreshold(1);
        editTxtSiteId.setAdapter(adapter);


        // fill two text view from the auto-complete site-id////
        editTxtSiteId.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof SiteId){
                    SiteId siteId = (SiteId) item;
                    SiteId siteName = (SiteId) item;
                    System.out.println(siteId.getSITE_ID());
                    System.out.println(siteId.getWARE_NAME());

                    editTxtSiteId.setText(siteId.getSITE_ID());
                    editTxtSiteName.setText(siteId.getWARE_NAME());


                }
            }
        });




        ///End of Site AUTO-Complete////

        ///begin of Client AUTO-Complete////


        connecttoDB();
        Statement stmtClient = null;
        int j = 0;
        try {
            stmtClient = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String sqlStmtClient = "select CLIENT_ID,FIRST_NAME,LAST_NAME from CLIENTS";


        ResultSet rs3 = null;
        try {

            rs3 = stmtClient.executeQuery(sqlStmtClient);
            System.out.println("ALiiiiiiiiiiiiiiiiiiiiii"+rs3);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        while (true) {
            try {
                if (!rs3.next()) break;
                clientDb.add(new Clients(rs3.getString("CLIENT_ID"),rs3.getString("FIRST_NAME"),rs3.getString("LAST_NAME")));



            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        try {
            rs3.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            stmtClient.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        for (j=0;j<clientDb.size();j++) {
            clientsList.add(new Clients(clientDb.get(j).getCLIENT_ID(),clientDb.get(j).getFIRST_NAME(),clientDb.get(j).getLAST_NAME()));

        }

        ClientsAdapter  adapterCl = new ClientsAdapter(getActivity(),clientsList);
        editTxtClient.setThreshold(1);
        editTxtClient.setAdapter(adapterCl);




        ///End of Client AUTO-complete/////



        //Display Ticket selected

        // connect to Oracle DB
        connecttoDB();
        System.out.println("++++++++++++++++++"+globalTicketId);
        Statement stmt1 =null;
        try {
            stmt1 = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



        String sqlStmt1 = "select * from TROUBLE_TICKETS where TICKET_ID='"+globalTicketId+"'";

        ResultSet rs2 = null;
        try {

            rs2 = stmt1.executeQuery(sqlStmt1);
            System.out.println("+++++++++++++++++++++++++"+rs2);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!rs2.next()) break;

                //get long Lat///




                connecttoDB();

                Statement stmtLatLong =null;
                try {
                    stmtLatLong = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }



                String sqlLatLong = "select WAREHOUSE.LONGITUDE as longi,WAREHOUSE.LATITUDE as lat from TROUBLE_TICKETS INNER JOIN warehouse ON trouble_tickets.site_id=warehouse.site_id where TICKET_ID='"+globalTicketId+"'";

                ResultSet rsLatLong = null;
                try {

                    rsLatLong = stmtLatLong.executeQuery(sqlLatLong);
                    System.out.println("latlonglatlonglatlong"+rsLatLong.toString());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                while (true) {
                    try {
                        if (!rsLatLong.next ( )) break;
                        longi=rsLatLong.getString ("longi");
                        lat=rsLatLong.getString("lat");


                        System.out.println("longiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii"+longi);
                        System.out.println("latttttttttttttttttttttttttttttttttttttt"+lat);

                    } catch (SQLException throwables) {
                        throwables.printStackTrace ( );
                    }
                }

                try {
                    rsLatLong.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmtLatLong.close();
                    conn.close ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                getSiteMap(lat,longi);

                //end LongLAT//////

                editTxtSiteId.setText(rs2.getString("SITE_ID"));
                editTxtSiteName.setText(rs2.getString("SITE_NAME"));
                editTxtRegion.setText(rs2.getString("REGIONNAME"));
                editTxtClient.setText(rs2.getString("CLIENT_ID"));
                editTxtDepa.setText(rs2.getString("DEPARTMENT"));
                editTxtDesc.setText(rs2.getString("DESCRIPTION"));
                editTxtSubject.setText(rs2.getString("SUBJECT"));
                editTxtSer.setText(rs2.getString("SERVICE"));
                editTxtSerIs.setText(rs2.getString("SERVICE_ISSUE"));
                editTxtIssApp.setText(rs2.getString("ISSUE_APPEARED"));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        try {
            rs2.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            stmt1.close();
            conn.close ();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

     //save Ticket

        // @RequiresApi(api = Build.VERSION_CODES.P)
        if (globalTicketId.equalsIgnoreCase ("0")) {
            // call class Gps to get our location
            gpsTracker = new GpsTracker (getActivity ( ));
            if (gpsTracker.canGetLocation ( )) {
                double  lat = gpsTracker.getLatitude ( );
                double lng = gpsTracker.getLongitude ( );
                newlat=String.valueOf(lat);
                newlong=String.valueOf(lng);
                System.out.println("++++++"+newlat);
                System.out.println("++++++"+newlong);

                getSiteMap(newlat,newlong);

            } else {
                gpsTracker.showSettingsAlert ( );

            }
        }



        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                Date date = new Date();
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                String TicketID;
                TicketID = "TICKETS" + year + "_";
                System.out.println("Jannnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnoun"+TicketID);

                connecttoDB();

                PreparedStatement stmtInserTicket = null;

                try {
                    // if it is a new ticket we will use insert

                    if (globalTicketId.equalsIgnoreCase("0")) {
                        System.out.println("babababa"+globalTicketId);
                        Statement stmt1 = null;
                        stmt1 = conn.createStatement ( );
                        String sqlStmt = "Select TROUBLE_TICKETS_SEQ.nextval as nbr from dual";
                        ResultSet rs1 = null;
                        try {
                            rs1 = stmt1.executeQuery (sqlStmt);
                            System.out.println("==================="+rs1);
                        }

                        catch (SQLException throwables) {
                            throwables.printStackTrace ( );
                        }

                        while (true) {
                            try {
                                if (!rs1.next ( )) break;
                                globalTicketId=TicketID+rs1.getString ("nbr");


                                System.out.println("IDDDDDDDDDDDDDDDDDDDDDDD"+globalTicketId);

                            } catch (SQLException throwables) {
                                throwables.printStackTrace ( );
                            }
                        }
                        rs1.close();
                        stmt1.close();

                        // send data from fragment to super activity
                        ((TicketInfoActivity)getActivity()).getTicketFragment(globalTicketId);


                        /*connecttoDB();

                        Statement stmtLatLong =null;
                        try {
                            stmtLatLong = conn.createStatement();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }



                        String sqlLatLong = "select WAREHOUSE.LONGITUDE as longi,WAREHOUSE.LATITUDE as lat from TROUBLE_TICKETS INNER JOIN warehouse ON trouble_tickets.site_id=warehouse.site_id where TICKET_ID='"+globalTicketId+"'";

                        ResultSet rsLatLong = null;
                        try {

                            rsLatLong = stmtLatLong.executeQuery(sqlLatLong);
                            System.out.println("latlonglatlonglatlong"+rsLatLong.toString());
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                        while (true) {
                            try {
                                if (!rsLatLong.next ( )) break;
                                longi=rsLatLong.getString ("longi");
                                lat=rsLatLong.getString("lat");


                                System.out.println("longiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii"+longi);
                                System.out.println("latttttttttttttttttttttttttttttttttttttt"+lat);

                            } catch (SQLException throwables) {
                                throwables.printStackTrace ( );
                            }
                        }

                        try {
                            rsLatLong.close();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        try {
                            stmtLatLong.close();
                            conn.close ();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }




                        getSiteMap(lat,longi);*/

                        stmtInserTicket = conn.prepareStatement("insert into TROUBLE_TICKETS (TICKET_ID,CREATION_DATE,LAST_MODIFIED_DATE,SITE_ID,SITE_NAME,REGIONNAME,SUBJECT,DEPARTMENT,CLIENT_ID,DESCRIPTION,SERVICE,SERVICE_ISSUE,ISSUE_APPEARED) values " +
                                "('"+globalTicketId +"',sysdate,sysdate,'"+ editTxtSiteId.getText()+"','"+ editTxtSiteName.getText()  +"','"+ editTxtRegion.getText ()  +"', '"+ editTxtSubject.getText ()  +"','"+editTxtDepa.getText() +"','"+ editTxtClient.getText ()  +"','"+editTxtDesc.getText() +"','"+editTxtSer.getText() +"','"+editTxtSerIs.getText() +"',TO_DATE('"+ticketDate+"','DD-MM-YYYY') )");

                    }

                    else { // we wil use update where Ticket_ID= the one we selected
                        stmtInserTicket = conn.prepareStatement("update  TROUBLE_TICKETS set LAST_MODIFIED_DATE=sysdate,SITE_ID='"+ editTxtSiteId.getText ()  +"',SITE_NAME='"+ editTxtSiteName.getText ()  +"',REGIONNAME='"+ editTxtRegion.getText ()  +"',SUBJECT='"+editTxtSubject.getText() +"',DEPARTMENT='"+editTxtDepa.getText() +"',DESCRIPTION='"+editTxtDesc.getText() +"',SERVICE='"+editTxtSer.getText() +"',SERVICE_ISSUE='"+editTxtSerIs.getText() +"',ISSUE_APPEARED='"+ ticketDate+"' where TICKET_ID='"+globalTicketId+"'");
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }

                try {
                    stmtInserTicket.executeUpdate();
                    Toast.makeText (getActivity (),"Saving Completed",Toast.LENGTH_SHORT).show ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }
                System.out.println("sqlllllllllll"+stmtInserTicket);

                try {
                    stmtInserTicket.close();
                    conn.close ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }




            }

        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // connect to Oracle DB
                connecttoDB();

                PreparedStatement stmtdelTicket = null;
                try {
                    // Delete Ticket_id

                    stmtdelTicket = conn.prepareStatement("delete  TROUBLE_TICKETS   where TICKET_ID  ='" + globalTicketId +"' ");
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }

                try {
                    stmtdelTicket.executeUpdate();
                    Toast.makeText (getActivity (),"Delete Completed",Toast.LENGTH_SHORT).show ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }


                try {
                    stmtdelTicket.close();
                    conn.close ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }



            }


        });




        return V;
    }



    private void connecttoDB() {

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

    public void getSiteMap(String lat,String  lng)
    {
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map_ticket);
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



}
