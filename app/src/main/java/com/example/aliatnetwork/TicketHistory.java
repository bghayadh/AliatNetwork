package com.example.aliatnetwork;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.aliatnetwork.R.layout.fragment_ticket_history;


public class TicketHistory extends Fragment {

    public TextView txtemployee;
    Connection conn;
    public String globalTicketId;
    public RecyclerView recHis;
    private ArrayList<TicketActionView> ticketsHis, ticketsHisDb;


    //// Refrech fragment when change in other fragment/////
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(fragment_ticket_history, container, false);
        recHis = V.findViewById(R.id.RecAct);
        Button btnMain = (Button) V.findViewById(R.id.BtnMain);
        Intent intent = getActivity().getIntent();
        String str = intent.getStringExtra("message_key");
        globalTicketId = str.toString();




        GetHisTicket();

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });


        return V;
    }

    private void connecttoDB() {

        // connect to DB
        OraDB oradb = new OraDB();
        String url = oradb.getoraurl();
        String userName = oradb.getorausername();
        String password = oradb.getorapwd();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(url, userName, password);
            //Toast.makeText (MainActivity.this,"Connected to the database",Toast.LENGTH_SHORT).show ();
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println("error is: " + e.toString());
            Toast.makeText(getActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " + e.toString());
            Toast.makeText(getActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show();
        } catch (java.lang.InstantiationException e) {
            System.out.println("error is: " + e.toString());
            Toast.makeText(getActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void GetHisTicket() {

        ticketsHis = new ArrayList<>();
        ticketsHisDb = new ArrayList<>();
        connecttoDB();
        Statement statement = null;
        int i = 0;
        try {
            statement = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String sqlHis = "select ACTION_ID,ACTION,STATUS,EMPLOYEE from TROUBLE_TICKETS_ACTIONS where TICKET_ID='" + globalTicketId + "'order by LAST_MODIFIED_DATE DESC ";

        ResultSet rs2 = null;
        try {

            rs2 = statement.executeQuery(sqlHis);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!rs2.next()) break;
                ticketsHisDb.add(new TicketActionView(rs2.getString("ACTION_ID"), rs2.getString("ACTION"), rs2.getString("STATUS"), rs2.getString("EMPLOYEE")));


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
            statement.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for (i = 0; i < ticketsHisDb.size(); i++) {
            ticketsHis.add(new TicketActionView(ticketsHisDb.get(i).getACTION_ID(), ticketsHisDb.get(i).getACTION(), ticketsHisDb.get(i).getSTATUS(), ticketsHisDb.get(i).getEMPLOYEE()));


        }
        ActionAdapter actionAdapter = new ActionAdapter(getContext(), ticketsHisDb);
        actionAdapter.setContacts(ticketsHis);
        recHis.setLayoutManager(new LinearLayoutManager(getActivity()));
        recHis.setAdapter(actionAdapter);

    }

}