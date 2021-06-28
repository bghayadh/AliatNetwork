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
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.example.aliatnetwork.R.layout.fragment_ticket_activity;
import static com.example.aliatnetwork.R.layout.fragment_ticket_ass_to;


public class TicketAssToFrag extends Fragment {
    public TextView txtemployee;
    Connection conn;
    private String globalTicketId,globalactionID;
    public  EditText editTextEmployee;
    public ArrayList<TicketActionView> ticketAction,ticketActionDb;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(fragment_ticket_ass_to,container,false);

        editTextEmployee=(EditText) V.findViewById(R.id.editTxtemployee);
        txtemployee = (TextView) V.findViewById(R.id.txtemployee);
        Button btnSave = (Button) V.findViewById(R.id.BtnSave);
        Button btnMain = (Button) V.findViewById(R.id.BtnMain);

        Intent intent = getActivity().getIntent();
        String str = intent.getStringExtra("message_key");
        globalTicketId = str.toString();

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connecttoDB();
                String ActionID;

                PreparedStatement stmtinsert1 = null;

                try {

                    Statement stmt1 = null;
                    stmt1 = conn.createStatement();
                    String sqlStmt = "Select ACTION_ID from TROUBLE_TICKETS_ACTIONS where TICKET_ID='"+globalTicketId+"'";
                    ResultSet rs1 = null;
                    try {
                        rs1 = stmt1.executeQuery(sqlStmt);

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    while (true) {
                        try {
                            if (!rs1.next()) break;
                            globalactionID= rs1.getString("ACTION_ID");
                            System.out.println("ACTIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIION"+           globalactionID);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                    rs1.close();
                    stmt1.close();

                    stmtinsert1 = conn.prepareStatement("update  TROUBLE_TICKETS_ACTIONS  set LAST_MODIFIED_DATE=sysdate,EMPLOYEE='"
                            + editTextEmployee.getText()  +"' where ACTION_ID ='" + globalactionID +"' ");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                try {
                    stmtinsert1.executeUpdate();
                    Toast.makeText(getActivity(), "Saving Completed", Toast.LENGTH_SHORT).show();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("sqlllllllllll" + stmtinsert1);

                try {
                    stmtinsert1.close();
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