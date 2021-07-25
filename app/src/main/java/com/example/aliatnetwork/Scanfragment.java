package com.example.aliatnetwork;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.media2.MediaLibraryService2;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.aliatnetwork.R.layout.fragment_infofragment;
import static com.example.aliatnetwork.R.layout.fragment_scanfragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Scanfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Scanfragment extends Fragment {
    RecyclerView scanrecview;
    ScanRecViewAdapter adapter;
    private Button btnmain, btnSave, btnAdd;
    private String globalwareid;
    private Connection conn;
    List<ScanList> scanList = new ArrayList<>();
    List<ScanList> scanListdb = new ArrayList<>();
    List<ScanList> scanListsEnter = new ArrayList<>();
    private EditText edittxtbarcode;
    private String test;
    private String result;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Scanfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Scanfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Scanfragment newInstance(String param1, String param2) {
        Scanfragment fragment = new Scanfragment();
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

        View V = inflater.inflate(fragment_scanfragment, container, false);
        btnmain = V.findViewById(R.id.BtnMain);
        btnSave = V.findViewById(R.id.BtnSave);
        //btnAdd=V.findViewById(R.id.BtnAdd);
        scanrecview = V.findViewById(R.id.scanrecview);
        edittxtbarcode = V.findViewById(R.id.editscan);


        //read passes value of ware_id from recylserview
        Intent intent = getActivity().getIntent();
        String str = intent.getStringExtra("message_key");
        globalwareid = str.toString();


        //call function getscanned data
        getScannedData();

/*        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test = edittxtbarcode.getText().toString();

                if (test.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter new value", Toast.LENGTH_SHORT).show();
                } else
                    try {

                        scanListsEnter.add(new ScanList(test,test,test,"1"));


                        ScanRecViewAdapter adapter = new ScanRecViewAdapter(getContext(), scanListsEnter);
                        scanrecview.setLayoutManager(new LinearLayoutManager(getActivity()));
                        scanrecview.setAdapter(adapter);
                        Toast.makeText(getActivity(),"Add Complete",Toast.LENGTH_SHORT).show();
                        Log.d("size ", String.valueOf(scanList.size()));
                        Log.d("size DB", String.valueOf(scanListdb.size()));
                        Log.d("size ENTER", String.valueOf(scanListsEnter.size()));


                    } catch (NumberFormatException e) {

                    }
            }
        });*/

        //get text from edittext after pressing enter
        edittxtbarcode.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                test = edittxtbarcode.getText().toString();

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {


                    try {


                            scanListsEnter.add(new ScanList(test, test, test, "1"));

                            ScanRecViewAdapter adapter = new ScanRecViewAdapter(getContext(), scanListsEnter);
                            scanrecview.setLayoutManager(new LinearLayoutManager(getActivity()));
                            scanrecview.setAdapter(adapter);
                            Toast.makeText(getActivity(), "Add Complete", Toast.LENGTH_SHORT).show();




                    } catch (NumberFormatException e) {

                    }

                    return true;

                }
                return false;
            }
        });

        /// get text from edittext while typing
        //and select from database according to the scanned barcode
        edittxtbarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
                test = mEdit.toString();
                String value = edittxtbarcode.getText().toString();
                result = value;


                scanList = new ArrayList<>();
                List<ScanList> scanList = new ArrayList<>(scanListsEnter);
                scanListdb = new ArrayList<>();


                connecttoDB();
                Statement stmt1 = null;
                int i = 0;
                try {
                    stmt1 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                String sqlStmt = "select * from TEMP where BARCODE_NUMBER='" + result + "'";

                ResultSet rs1 = null;
                try {
                    rs1 = stmt1.executeQuery(sqlStmt);

                    System.out.println(rs1.toString());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                while (true) {
                    try {


                        if (!rs1.next()) {
                            scanList.add(new ScanList(result, result, result, result));


                            btnSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    SaveNewBarcode();

                                    //SaveFromTemp();

                                }
                            });


                            break;
                        }


                        scanListdb.add(new ScanList(rs1.getString("ITEM_CODE"), rs1.getString("BARCODE_NUMBER"), rs1.getString("SERIAL_NUMBER"), "1"));

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
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                for (i = 0; i < scanListdb.size(); i++) {
                    scanList.add(new ScanList(scanListdb.get(i).getItem(), scanListdb.get(i).getBarcode(), scanListdb.get(i).getSerialNb(), "1"));
                    String item = scanListdb.get(i).getItem();
                    String barcode = scanListdb.get(i).getBarcode();
                    String serial_nb = scanListdb.get(i).getSerialNb();
                    String quantity = "1";

                    //System.out.println("test-test: "+item+"\t"+barcode+"\t"+serial_nb+"\t"+quantity);
                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            SaveFromTemp();

                        }
                    });

                }
                ///fill the recyclerview with imagespath
                ScanRecViewAdapter scanRecViewAdapter = new ScanRecViewAdapter(getContext(), scanList);
                scanrecview.setLayoutManager(new LinearLayoutManager(getActivity()));
                scanrecview.setAdapter(scanRecViewAdapter);


            }


            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        });


        //// return to main page
        btnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return V;


    }

    public void connecttoDB() {
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


    public void getScannedData() {
        connecttoDB();
        Statement stmt1 = null;
        int i = 0;
        try {
            stmt1 = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String sqlStmt = "select distinct ITEM_ID,BARCODE,SERIAL_NUMBER,QUANTITY from WAREHOUSE_SCAN_ONSITE where WARE_ID='" + globalwareid + "'";

        ResultSet rs1 = null;
        try {
            rs1 = stmt1.executeQuery(sqlStmt);

            System.out.println(rs1.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        while (true) {
            try {

                if (!rs1.next()) break;

                scanListdb.add(new ScanList(rs1.getString("ITEM_ID"), rs1.getString("BARCODE"), rs1.getString("SERIAL_NUMBER"), rs1.getString("QUANTITY")));

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
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for (i = 0; i < scanListdb.size(); i++) {
            scanList.add(new ScanList(scanListdb.get(i).getItem(), scanListdb.get(i).getBarcode(), scanListdb.get(i).getSerialNb(), scanListdb.get(i).getQuantity()));
        }


        ///fill the recyclerview with imagespath
        ScanRecViewAdapter scanRecViewAdapter = new ScanRecViewAdapter(getContext(), scanList);
        scanrecview.setLayoutManager(new LinearLayoutManager(getActivity()));
        scanrecview.setAdapter(scanRecViewAdapter);


    }


    public void SaveNewBarcode() {
        connecttoDB();
        PreparedStatement stmtinsert1 = null;

        String value = edittxtbarcode.getText().toString();
        //System.out.println("'''''''''''''''''''''''''''''''"+value);
        //scanListEnter = new ArrayList<>(scanListdb);
        try {

            for (int i = 0; i < scanListsEnter.size(); i++) {

                stmtinsert1 = conn.prepareStatement("insert into WAREHOUSE_SCAN_ONSITE (WARE_ID,ITEM_ID,BARCODE,SERIAL_NUMBER,QUANTITY) values" +
                        "('" + globalwareid + "','" + scanListsEnter.get(i).getItem() + "','" + scanListsEnter.get(i).getBarcode() + "','" + scanListsEnter.get(i).getSerialNb() + "','" + 1 + "')");
                stmtinsert1.executeUpdate();

            }
            Toast.makeText(getActivity(), "Saving Completed", Toast.LENGTH_SHORT).show();
            stmtinsert1.close();
            conn.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void SaveFromTemp() {
        connecttoDB();
        PreparedStatement stmtinsert1 = null;


        try {

            stmtinsert1 = conn.prepareStatement("insert into WAREHOUSE_SCAN_ONSITE (WARE_ID,ITEM_ID,BARCODE,SERIAL_NUMBER,QUANTITY)" +
                    "SELECT '" + globalwareid + "',ITEM_CODE,BARCODE_NUMBER,SERIAL_NUMBER,'" + "1" + "'FROM TEMP WHERE BARCODE_NUMBER='" + result + "'");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            stmtinsert1.executeUpdate();
            Toast.makeText(getActivity(), "Saving Completed", Toast.LENGTH_SHORT).show();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        try {
            stmtinsert1.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

   /* public void BarcodeExe() {
        connecttoDB();
        Statement stmt1 = null;
        try {
            stmt1 = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String sqlStmt = "select distinct ITEM_ID,BARCODE,SERIAL_NUMBER,QUANTITY from WAREHOUSE_SCAN_ONSITE where WARE_ID='" + globalwareid + "'";

        ResultSet rs1 = null;
        try {
            rs1 = stmt1.executeQuery(sqlStmt);
            while (rs1.next()) {
                scanListdb.add(new ScanList(rs1.getString("ITEM_ID"), rs1.getString("BARCODE"), rs1.getString("SERIAL_NUMBER"), rs1.getString("QUANTITY")));
                System.out.println("''''''''''''''''''''''''" + rs1.getString("ITEM_ID"));
                if (rs1.getString("BARCODE").contains(edittxtbarcode.getText().toString())) {
                    System.out.println("mawjouuuuuuuuuuuuuuuuuuuuud");
                    Toast.makeText(getActivity(), "Mawjoud", Toast.LENGTH_SHORT).show();

                }

            }
            scanListsEnter.add(new ScanList(test, test, test, "1"));
            ScanRecViewAdapter adapter = new ScanRecViewAdapter(getContext(), scanListsEnter);
            scanrecview.setLayoutManager(new LinearLayoutManager(getActivity()));
            scanrecview.setAdapter(adapter);
            Toast.makeText(getActivity(), "Add Complete", Toast.LENGTH_SHORT).show();

            rs1.close();
            stmt1.close();
            conn.close();
            System.out.println(rs1.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }*/


    @Override
    public void onResume() {
        super.onResume();

    }
}