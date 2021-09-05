package com.example.aliatnetwork;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.res.TypedArrayUtils;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.nio.charset.CoderMalfunctionError;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.example.aliatnetwork.R.layout.edit_scan;
import static com.example.aliatnetwork.R.layout.fragment_scanfragment;
import static com.example.aliatnetwork.R.layout.scan_item_list;


public class Scanfragment extends Fragment implements InsertDialog.onInputSelected {
    private static final String TAG = "MyDialog";
    private Button btnInsertBefor, btnInsertAfter, btnInsert, btnLast, btnNext, btnPreviews, btnSave, btnDelete,btnScan;
    private ArrayList<Item> itemList = new ArrayList<>();
    private List<Item> itemDb = new ArrayList<>();
    public EditText editItemName, editItemCode, editItemModel, editItemPartNumber, editSerialNumber, editBarcodeNumber, editQuantity, scan_id;
    private String globalwareid;
    private AutoCompleteTextView editTextScan;
    private TextView pagin;
    public int globalScanID;
    public int ScanId;
    private Connection conn;
    private List<ScanId> scanIdsListDb = new ArrayList<>();
    private List<ScanId> scanIdsList = new ArrayList<>();
    private List<Item> onCreatView = new ArrayList<>();
    private List<Item> showItem = new ArrayList<>();
    private List<Item> showItemDb = new ArrayList<>();


    private ArrayList<ItemAuto> itemAuto = new ArrayList<>();
    private ArrayList<ItemAuto> itemAutoDb = new ArrayList<>();
    private int scanCounter = 0;
    private int scanListPos = 1;
    private String scanInput;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(fragment_scanfragment, container, false);
        editTextScan = view.findViewById(R.id.editTextScan);
        editItemName = view.findViewById(R.id.itemName);
        editItemCode = view.findViewById(R.id.itemCode);
        editItemModel = view.findViewById(R.id.itemModel);
        editItemPartNumber = view.findViewById(R.id.itemPartNumber);
        editSerialNumber = view.findViewById(R.id.serialNumber);
        editBarcodeNumber = view.findViewById(R.id.barcodeNumber);
        editQuantity = view.findViewById(R.id.quatity);
        btnSave = view.findViewById(R.id.BtnSave);
        btnDelete = view.findViewById(R.id.BtnDelete);
        btnInsert = view.findViewById(R.id.btnInsert);
        btnInsertAfter = view.findViewById(R.id.btnInsertAfter);
        btnInsertBefor = view.findViewById(R.id.btnInsertBefor);
        btnLast = view.findViewById(R.id.btnLast);
        btnNext = view.findViewById(R.id.btnNext);
        btnPreviews = view.findViewById(R.id.btnPrevious);
        btnScan=view.findViewById(R.id.btnScan);
        scan_id = view.findViewById(R.id.scan_id);
        pagin=view.findViewById(R.id.pagination);


        //GetScannedData();

        //read passes value of ware_id from recylserview
        Intent intent = getActivity().getIntent();
        String str = intent.getStringExtra("message_key");
        globalwareid = str.toString();

        GetScanId();
        AutoComplete();




        if (scanIdsList.isEmpty()) {
            Toast.makeText(getActivity(), "No Item", Toast.LENGTH_SHORT).show();

        } else {

            GetItemData();






        }


        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* InsertDialog insertDialog = new InsertDialog();
                insertDialog.setTargetFragment(Scanfragment.this, 1);
                insertDialog.show(getFragmentManager(), "Dialoge");*/

                editItemName.setText("");
                editItemModel.setText("");
                editItemCode.setText("");
                editItemPartNumber.setText("");
                editSerialNumber.setText("");
                editBarcodeNumber.setText("");
                editQuantity.setText("");



            }
        });
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoComplete();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (scanCounter < scanIdsList.size() - 1) {
                    scanCounter++;
                    scanListPos++;
                    System.out.println("scanCounter  " + scanCounter);

                    connecttoDB();
                    Statement stmt2 = null;
                    PreparedStatement StatementNext = null;

                    try {
                        stmt2 = conn.createStatement();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    String sqlStmt = "select  SCAN_ID,ITEM_NAME,ITEM_CODE,ITEM_MODEL,ITEM_PART_NUMBER,BARCODE,SERIAL_NUMBER,QUANTITY from WAREHOUSE_SCAN_ONSITE where SCAN_ID='" + scanIdsList.get(scanCounter).getScanID() + "'";


                    ResultSet rs1 = null;
                    try {

                        rs1 = stmt2.executeQuery(sqlStmt);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    while (true) {
                        try {
                            if (!rs1.next()) break;
                            showItemDb.add(new Item(rs1.getString("ITEM_NAME"), rs1.getString("ITEM_CODE"), rs1.getString("ITEM_MODEL"), rs1.getString("ITEM_PART_NUMBER"), rs1.getString("SERIAL_NUMBER"), rs1.getString("BARCODE"), rs1.getString("QUANTITY"), rs1.getInt("SCAN_ID")));


                            editItemName.setText(rs1.getString("ITEM_NAME"));
                            editItemCode.setText(rs1.getString("ITEM_CODE"));
                            editItemModel.setText(rs1.getString("ITEM_MODEL"));
                            editItemPartNumber.setText(rs1.getString("ITEM_PART_NUMBER"));
                            editSerialNumber.setText(rs1.getString("SERIAL_NUMBER"));
                            editBarcodeNumber.setText(rs1.getString("BARCODE"));
                            scan_id.setText(rs1.getString("SCAN_ID"));

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

                    pagin.setText(scanListPos+"/"+scanIdsList.size());
                    scanIdsList.indexOf(scanCounter);
                    System.out.println(" ''''''''''''''''  " + scanIdsList.indexOf(scanCounter));

                } else {
                    System.out.println("condition valide");

                    Toast.makeText(getActivity(), "No Next Item", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteScan();
                GetScanId();

            }
        });

        btnPreviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (scanCounter > 0) {
                    scanCounter--;
                    scanListPos--;
                    connecttoDB();
                    Statement stmt2 = null;
                    PreparedStatement StatementNext = null;

                    try {
                        stmt2 = conn.createStatement();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    String sqlStmt = "select  SCAN_ID,ITEM_NAME,ITEM_CODE,ITEM_MODEL,ITEM_PART_NUMBER,BARCODE,SERIAL_NUMBER,QUANTITY from WAREHOUSE_SCAN_ONSITE where SCAN_ID='" + scanIdsList.get(scanCounter).getScanID() + "'";


                    ResultSet rs1 = null;
                    try {

                        rs1 = stmt2.executeQuery(sqlStmt);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    while (true) {
                        try {
                            if (!rs1.next()) break;
                            showItemDb.add(new Item(rs1.getString("ITEM_NAME"), rs1.getString("ITEM_CODE"), rs1.getString("ITEM_MODEL"), rs1.getString("ITEM_PART_NUMBER"), rs1.getString("SERIAL_NUMBER"), rs1.getString("BARCODE"), rs1.getString("QUANTITY"), rs1.getInt("SCAN_ID")));


                            editItemName.setText(rs1.getString("ITEM_NAME"));
                            editItemCode.setText(rs1.getString("ITEM_CODE"));
                            editItemModel.setText(rs1.getString("ITEM_MODEL"));
                            editItemPartNumber.setText(rs1.getString("ITEM_PART_NUMBER"));
                            editSerialNumber.setText(rs1.getString("SERIAL_NUMBER"));
                            editBarcodeNumber.setText(rs1.getString("BARCODE"));
                            scan_id.setText(rs1.getString("SCAN_ID"));

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


                    scanIdsList.indexOf(scanCounter);
                    System.out.println(" ''''''''''''''''  " + scanIdsList.indexOf(scanCounter));

                    pagin.setText(scanListPos+"/"+scanIdsList.size());


                } else {
                    System.out.println("condition valide");
                    Toast.makeText(getActivity(), "No Previews Item", Toast.LENGTH_SHORT).show();

                }


            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* if (!btnInsert.isPressed()){
                    Toast.makeText(getActivity(), "Insert not Pressed", Toast.LENGTH_SHORT).show();

                }
                else{*/
                    SaveScan();

                    GetScanId();
                //}


/*
               Intent intent = new Intent(getActivity(), SiteInfoActivity.class);
                startActivity(intent);
*/


            }


        });


        return view;
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


    @Override
    public void sendInput(String name, String code, String model, String partNumber, String serial, String barcode, String qt) {
        Log.d(TAG, "send Input: Found incoming input:  " + name);
        editItemName.setText(name);
        editItemCode.setText(code);
        editItemModel.setText(model);
        editItemPartNumber.setText(partNumber);
        editSerialNumber.setText(serial);
        editBarcodeNumber.setText(barcode);
        editQuantity.setText(qt);
    }

    public void GetItemData() {



        connecttoDB();
        Statement stmt1 = null;

        int i = 0;
        try {
            stmt1 = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String sqlStmt = "select  SCAN_ID,ITEM_NAME,ITEM_CODE,ITEM_MODEL,ITEM_PART_NUMBER,BARCODE,SERIAL_NUMBER,QUANTITY from WAREHOUSE_SCAN_ONSITE where scan_id='" + scanIdsList.get(0).getScanID() + "' ";


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

                onCreatView.add(new Item(rs1.getString("ITEM_NAME"), rs1.getString("ITEM_CODE"), rs1.getString("ITEM_MODEL"), rs1.getString("ITEM_PART_NUMBER"), rs1.getString("SERIAL_NUMBER"), rs1.getString("BARCODE"), rs1.getString("QUANTITY"), rs1.getInt("SCAN_ID")));


                editItemName.setText(rs1.getString("ITEM_NAME"));
                editItemCode.setText(rs1.getString("ITEM_CODE"));
                editItemModel.setText(rs1.getString("ITEM_MODEL"));
                editItemPartNumber.setText(rs1.getString("ITEM_PART_NUMBER"));
                editSerialNumber.setText(rs1.getString("SERIAL_NUMBER"));
                editBarcodeNumber.setText(rs1.getString("BARCODE"));
                editQuantity.setText(rs1.getString("QUANTITY"));
                scan_id.setText(rs1.getString("SCAN_ID"));
                System.out.println("");




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


        pagin.setText(scanListPos+"/"+scanIdsList.size());



    }


    public void SaveScan() {
        connecttoDB();

        PreparedStatement stmtInserTicket = null;

        try {
            // if it is a new ticket we will use insert

            Statement stmt1 = null;
            stmt1 = conn.createStatement();
            String sqlStmt = "Select WAREHOUSE_SCAN_ONSITE_SEQ.nextval as nbr from dual";
            ResultSet rs1 = null;
            try {
                rs1 = stmt1.executeQuery(sqlStmt);
                System.out.println("===================" + rs1);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            while (true) {
                try {
                    if (!rs1.next()) break;


                    globalScanID = rs1.getInt("nbr");


                    System.out.println("IDDDDDDDDDDDDDDDDDDDDDDD" + globalScanID);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            rs1.close();
            stmt1.close();

            // send data from fragment to super activity


            stmtInserTicket = conn.prepareStatement("insert into WAREHOUSE_SCAN_ONSITE (WARE_ID,SCAN_ID,SCAN_DATE,UPDATE_DATE,ITEM_NAME,ITEM_CODE,ITEM_MODEL,ITEM_PART_NUMBER,SERIAL_NUMBER,BARCODE,QUANTITY) values " +
                    "('" + globalwareid + "','" + globalScanID + "',sysdate,sysdate,'" + editItemName.getText() + "','" + editItemCode.getText() + "','" + editItemModel.getText() + "','" + editItemPartNumber.getText() + "', '" + editSerialNumber.getText() + "','" + editBarcodeNumber.getText() + "','" + 1 + "' )");
            ///added for pass data in fragment

            System.out.println("globalScanID  " + globalScanID);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            stmtInserTicket.executeUpdate();
            Toast.makeText(getActivity(), "Saving Completed", Toast.LENGTH_SHORT).show();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        try {
            stmtInserTicket.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        GetItemData();

        pagin.setText(scanListPos+"/"+scanIdsList.size());

    }


    public void deleteScan() {
        connecttoDB();

        PreparedStatement stmtdelTicket = null;


        try {

            stmtdelTicket = conn.prepareStatement("delete from warehouse_scan_onsite where scan_id='" + scan_id.getText() + "'");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            stmtdelTicket.executeUpdate();
            Toast.makeText(getActivity(), "Delete Completed", Toast.LENGTH_SHORT).show();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        try {
            stmtdelTicket.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }




/*
        Intent intent = new Intent(getActivity(), SiteInfoActivity.class);
        startActivity(intent);
*/


    }
    private void GetScanId(){
        connecttoDB();
        Statement stmt1 = null;
        scanIdsList= new ArrayList<>();
        scanIdsListDb = new ArrayList<>();
        int i = 0;
        try {
            stmt1 = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String sqlStmt = "select  SCAN_ID from WAREHOUSE_SCAN_ONSITE where WARE_ID='" + globalwareid + "'";


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

                scanIdsListDb.add(new ScanId(rs1.getInt("SCAN_ID")));

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

        for (int u = 0; u < scanIdsListDb.size(); u++) {

            scanIdsList.add(new ScanId(scanIdsListDb.get(u).getScanID()));
            System.out.println("sizeeeeeeeeeee " + scanIdsList.size());


        }
    }

    private void AutoComplete(){

        scanInput = editTextScan.getText().toString();
        connecttoDB();
        Statement stmtAuto = null;
        try {
            stmtAuto = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



        String sqlAuto = "select serial_number, null  AS BARCODE_NUMBER,ITEM_CODE,item_model,item_partno,item_name FROM serial_number where serial_number='"+scanInput+"' OR item_code='"+scanInput+"' or item_model='"+scanInput+"' or item_partno='"+scanInput+"'\n"+
        "UNION ALL SELECT  null AS SERIAL_NUMBER,barcode_number,item_code,null as item_model,null as item_part_number,null as item_name FROM item_barcode where barcode_number ='"+scanInput+"' or item_code='"+scanInput+"'\n"+
        "UNION ALL SELECT null as serial_number,null as barcode_number,item_code,item_model,item_part_number,null as item_name FROM item_model_partnumber where item_code='"+scanInput+"' or item_model='"+scanInput+"' or item_part_number='"+scanInput+"'";


        ResultSet rs2 = null;
        try {

            rs2 = stmtAuto.executeQuery(sqlAuto);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        while (true) {
            try {
                if (!rs2.next()) break;
                itemAutoDb.add(new ItemAuto(rs2.getString("ITEM_NAME"), rs2.getString("ITEM_CODE"), rs2.getString("ITEM_MODEL"), rs2.getString("ITEM_PARTNO"), rs2.getString("SERIAL_NUMBER"), rs2.getString("BARCODE_NUMBER"), "1"));


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
            stmtAuto.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        for (int j = 0; j < itemAutoDb.size(); j++) {
            itemAuto.add(new ItemAuto(itemAutoDb.get(j).getItemName(), itemAutoDb.get(j).getItemCode(), itemAutoDb.get(j).getItemModel(), itemAutoDb.get(j).getItemPartNumber(), itemAutoDb.get(j).getSerialNumber(), itemAutoDb.get(j).getBarcodeNumber(), itemAutoDb.get(j).getQuantity()));

            editItemName.setText(itemAuto.get(j).getItemName());
            editItemCode.setText(itemAutoDb.get(j).getItemCode());
            editItemModel.setText(itemAutoDb.get(j).getItemModel());
            editItemPartNumber.setText(itemAutoDb.get(j).getItemPartNumber());
            editSerialNumber.setText(itemAuto.get(j).getSerialNumber());
            editBarcodeNumber.setText(itemAuto.get(j).getBarcodeNumber());
            editQuantity.setText("1");
        }




/*        ItemAdapter Autoadapter = new ItemAdapter(getActivity(), itemAuto);
        editTextScan.setThreshold(1);
        editTextScan.setAdapter(Autoadapter);

        editTextScan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object item = parent.getItemAtPosition(position);
                if (item instanceof ItemAuto) {
                    ItemAuto itemAuto = (ItemAuto) item;

                    editItemName.setText(itemAuto.getItemName());
                    editItemCode.setText(itemAuto.getItemCode());
                    editItemModel.setText(itemAuto.getItemModel());
                    editItemPartNumber.setText(itemAuto.getItemPartNumber());
                    editSerialNumber.setText(itemAuto.getSerialNumber());
                    editBarcodeNumber.setText(itemAuto.getBarcodeNumber());

                }

            }
        });*/


        editTextScan.setText("");
    }


}