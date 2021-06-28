package com.example.aliatnetwork;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.media2.MediaLibraryService2;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.aliatnetwork.R.layout.action_item;
import static com.example.aliatnetwork.R.layout.fragment_ticket_activity;
import static com.example.aliatnetwork.R.layout.fragment_ticket_info;
import static com.example.aliatnetwork.R.layout.item_spinner;


public class TicketActivityFrag extends Fragment {

    public Connection conn;
    public Spinner spinnerAction;
    public  EditText editAction;
    private int arraysize = 0;
    private int varraysize = 0;
    private int pagination = 0;
    private String globalactionID,globalTicketId;
    private RecyclerView RecViewAction;
    public ArrayList<TicketActionView> ticketAction,ticketActionDb;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_ticket_activity, container, false);
        View V = inflater.inflate(fragment_ticket_activity,container,false);
        spinnerAction = (Spinner) V.findViewById(R.id.spinnerTicket);
        Button btnSave = (Button) V.findViewById(R.id.BtnSave);
        Button btnMain = (Button) V.findViewById(R.id.BtnMain);
        editAction = (EditText) V.findViewById(R.id.txtAction);
        RecViewAction = V.findViewById(R.id.RecAct);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAction.setAdapter(adapter);
        Intent intent = getActivity().getIntent();
        String str = intent.getStringExtra("message_key");
        globalTicketId = str.toString();



        //////////////////////////////////
        ticketAction = new ArrayList<>();
        ticketActionDb = new ArrayList<>();
        connecttoDB();
        Statement statement = null;
        int i =0;
        try {
            statement = conn.createStatement();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }

        String sqlshowAction="select ACTION_ID,ACTION,STATUS from TROUBLE_TICKETS_ACTIONS where TICKET_ID='"+globalTicketId+"'";

        ResultSet rs2 = null;
        try {

            rs2=statement.executeQuery(sqlshowAction);
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        while (true){
            try {
                if (!rs2.next()) break;
                ticketActionDb.add(new TicketActionView(rs2.getString("ACTION_ID"),rs2.getString("ACTION"),rs2.getString("STATUS")));


            }catch (SQLException throwables){
                throwables.printStackTrace();
            }

        }
        try {
            rs2.close();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        try {
            statement.close();
            conn.close();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        for (i=0;i<ticketActionDb.size();i++){
            ticketAction.add(new TicketActionView(ticketActionDb.get(i).getACTION_ID(),ticketActionDb.get(i).getACTION(),ticketActionDb.get(i).getSTATUS()));
            String aID =ticketActionDb.get(i).getACTION_ID();

        }
        ActionAdapter actionAdapter = new ActionAdapter(getContext(),ticketAction);
        RecViewAction.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecViewAction.setAdapter(actionAdapter);




        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });
        spinnerAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                switch (position){
                    case 0:

                        editAction.setEnabled(true);

                        break;
                    case 1:
                        editAction.setEnabled(false);

                        break;
                    case 2:
                        editAction.setEnabled(false);

                        break;
                    case 3:
                        editAction.setEnabled(true);

                        break;
                    case 4:
                        editAction.setEnabled(true);



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date = new Date();
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                String ActionID;
                ActionID = "Action" + year + "_";

                connecttoDB();

                PreparedStatement stmtInsertAction = null;

                try {

                    Statement stmt1 = null;
                    stmt1 = conn.createStatement();
                    String sqlStmt = "Select TROUBLE_TICKETS_ACTIONS_SEQ.nextval as nbr from dual";
                    ResultSet rs1 = null;
                    try {
                        rs1 = stmt1.executeQuery(sqlStmt);
                        System.out.println("========11111111111111===========  " + rs1);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    while (true) {
                        try {
                            if (!rs1.next()) break;
                            globalactionID = ActionID + rs1.getString("nbr");


                            System.out.println("IDDDDDDDDDDDDDDDDDDDDDDD" + globalactionID);

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                    rs1.close();
                    stmt1.close();

                    stmtInsertAction = conn.prepareStatement("insert into TROUBLE_TICKETS_ACTIONS (ACTION_ID,CREATION_DATE,LAST_MODIFIED_DATE,TICKET_ID,ACTION,STATUS) values " +
                            "('" + globalactionID + "',sysdate,sysdate,'" + globalTicketId + "','" + editAction.getText().toString() + "','" + spinnerAction.getSelectedItem().toString() + "')");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                try {
                    stmtInsertAction.executeUpdate();
                    Toast.makeText(getActivity(), "Saving Completed", Toast.LENGTH_SHORT).show();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("sqlllllllllll" + stmtInsertAction);

                try {
                    stmtInsertAction.close();
                    conn.close();


                } catch (SQLException throwables) {
                    throwables.printStackTrace();
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




}