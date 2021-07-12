package com.example.aliatnetwork;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class ShopImageFragment extends Fragment {

    private String globalShopsId;
    private ImageSwitcher imageShopsSw;
    private ArrayList<Uri> imageShopsUri;
    private static final int PICK_IMAGES_CODE=0;
    private String[] imageShopsSrc;
    private TextView txtShopImagePath;
    private ImageButton imageShopsBtn;
    private RecyclerView imageRecViewShop;
    private TextView txtShopsId;
    private ArrayList<ShopsImageListView> imageShopsList,imageShopsListDb;
    int pos=0;
    String server = "ftp.ipage.com";
    int port = 21;
    String user ="beid";
    String pass="10th@Loop";
    FTPClient ftpClient = new FTPClient();
    public Connection conn;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_shop_image,container,false);
        Button btnShopsPrev = (Button) v.findViewById(R.id.btnPreviousShops);
        Button btnShopsMain = (Button) v.findViewById(R.id.btnShopsMain);
        Button btnShopsNext = (Button) v.findViewById(R.id.btnNextShops);
        Button btnShopsPickImage = (Button) v.findViewById(R.id.btnSelectImagesShops);
        Button btnUploadpimagesShops= (Button) v.findViewById(R.id.btnUploadpimagesShops);
        imageShopsSw = (ImageSwitcher) v.findViewById(R.id.imagesSwShops);
        imageShopsUri = new ArrayList<>();
        imageRecViewShop = v.findViewById(R.id.imageShopsRecView);
        imageShopsList = new ArrayList<>();
        imageShopsListDb = new ArrayList<>();
        txtShopImagePath = (TextView) v.findViewById(R.id.imgpathShop);

        //read passes value of ShopsId from recylserview
        Intent intent = getActivity().getIntent();
        String str = intent.getStringExtra("message_key");
        globalShopsId = str.toString();

        ///connect to dataBase////
        connecttoDB();
        Statement stmtImage = null;
        int i=0;
        try {
            stmtImage=conn.createStatement();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        System.out.println("befoooooooooooooooooooooooor"+globalShopsId);
        String sqlImageShops ="select IMAGE_PATH from SHOPS_IMAGE where SHOPS_ID='"+globalShopsId+"'";


        ResultSet rs1 = null;
        try {
            rs1 =stmtImage.executeQuery(sqlImageShops);

        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        while (true){
            try {
                if(!rs1.next()) break;
                System.out.println("afteeeeeeeeeeeeeeeeeeeeeeeeeeeer");
                imageShopsListDb.add(new ShopsImageListView(R.drawable.imgbtn_foreground,rs1.getString("IMAGE_PATH"),R.drawable.imgdelete_background));
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
            stmtImage.close();
            conn.close();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        for (i=0;i<imageShopsListDb.size();i++){
            imageShopsList.add(new ShopsImageListView(R.drawable.imgbtn_foreground,imageShopsListDb.get(i).getIMAGE_PATH(),R.drawable.imgdelete_foreground));
        }

        ShopsImageRecViewAdapter shopsImageRecViewAdapter = new ShopsImageRecViewAdapter(getContext(),imageShopsList);
        imageRecViewShop.setLayoutManager(new LinearLayoutManager(getActivity()));
        imageRecViewShop.setAdapter(shopsImageRecViewAdapter);
        /////////////////////////////////////








        ////////////////////btn previews////////////////
        btnShopsPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos>0){
                    pos--;
                    imageShopsSw.setImageURI(imageShopsUri.get(pos));
                }else {
                    Toast.makeText(getActivity(),"No previous image",Toast.LENGTH_SHORT).show();

                }
            }
        });
        ////////btn next///////////////////////////
        btnShopsNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos<imageShopsUri.size()-1){
                    pos++;
                    imageShopsSw.setImageURI(imageShopsUri.get(pos));
                }else {
                    Toast.makeText(getActivity(),"No more image",Toast.LENGTH_SHORT).show();

                }
            }
        });
        /////// btn main////////////////////////
        btnShopsMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMain = new Intent(getActivity(),MainActivity.class);
                startActivity(intentMain);
            }
        });
        //////select image///////////
        btnShopsPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickShopsImagesIntent();
            }
        });

        /////Run this function to move images to ftp server//////
        btnUploadpimagesShops.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (imageShopsSrc == null){
                    Toast.makeText(getActivity(),"Images not selected yet",Toast.LENGTH_SHORT).show();

                }else {
                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder ( ).permitAll ( ).build ( );
                        StrictMode.setThreadPolicy (policy);
                        ftpClient.connect (server, port);
                        if (FTPReply.isPositiveCompletion (ftpClient.getReplyCode ( ))) {
                            // login using username & password
                            boolean status = ftpClient.login (user, pass);
                            ftpClient.setFileType (FTP.BINARY_FILE_TYPE);
                            ftpClient.enterLocalPassiveMode ( );
                            String workingDir = ftpClient.printWorkingDirectory ( );
                            System.out.println ("OUR PWD IS " + workingDir);
                            ftpClient.changeWorkingDirectory (workingDir + "Shops");

                            //return true if the directory found///
                            System.out.println(ftpClient.changeWorkingDirectory(workingDir+"Shops"));

                            ftpClient.makeDirectory(globalShopsId);
                            ftpClient.changeWorkingDirectory(workingDir+"Shops"+"/"+globalShopsId);
                            // check if the directory correctly created
                            System.out.println(ftpClient.changeWorkingDirectory(workingDir+"Shops"+"/"+globalShopsId));
                            ftpClient.setFileType (FTP.BINARY_FILE_TYPE);
                            workingDir = ftpClient.printWorkingDirectory ( );

                            System.out.println("Directory: "+workingDir);
                            ///validate shops id folder if not found created else change directory

                            /// /Sites/Shops_id/Image_name
                            // upload file

                            try {
                                for (int i=0;i<imageShopsSrc.length;i++){
                                    String srcFilePath = imageShopsSrc[i];
                                    //System.out.println ("READ SRC" + srcFilePath);
                                    // System.out.println ("READ DEST " + workingDir);
                                    String destination1 = srcFilePath;

                                    //Get Image name from selection

                                    if (destination1.contains("/")) {
                                        String[] data1 ;
                                        data1=destination1.split("/",-1);
                                        destination1=data1[(data1.length) -1];
                                        System.out.println("Image Name:"+destination1);
                                    }
                                    //get the image path in the ftp
                                    String path = workingDir+"/"+destination1;
                                    System.out.println("path"+path);

                                    //srcFilePath is source to read
                                    // workingDir  is destination to upload
                                    FileInputStream srcFileStream = new FileInputStream (srcFilePath);

                                    //System.out.println ("OPEN STREAM ");
                                    //status = ftpClient.storeFile(workingDir, srcFileStream);

                                    // upload function
                                    System.out.println ("upload " + srcFileStream + "Destination " + workingDir);
                                    ftpClient.storeFile (destination1, srcFileStream);


                                    srcFileStream.close ( );

                                    connecttoDB();
                                    PreparedStatement stmtInsertImageShops = null;
                                    try {
                                        stmtInsertImageShops = conn.prepareStatement("insert into SHOPS_IMAGE(SHOPS_ID,IMAGE_PATH,UPLOAD_DATE) values"+
                                                "('"+globalShopsId+"','"+path+"',sysdate)");
                                    }catch (SQLException throwables){
                                        throwables.printStackTrace();
                                    }
                                    try {
                                        stmtInsertImageShops.executeQuery();
                                        Toast.makeText (getActivity (),"Saving Completed",Toast.LENGTH_SHORT).show ();
                                    }catch (SQLException throwables){
                                        throwables.printStackTrace();
                                    }
                                    try {
                                        stmtInsertImageShops.close();
                                        conn.close();
                                    }catch (SQLException throwables){
                                        throwables.printStackTrace();
                                    }

                                }
                                Toast.makeText (getActivity (),"upload Completed",Toast.LENGTH_SHORT).show ();
                                imageShopsSrc=null;

                            }catch (Exception e){

                                Toast.makeText (getActivity (),e.toString (),Toast.LENGTH_SHORT).show ();
                                e.printStackTrace ( );
                            }
                            }

                        }catch (IOException e){

                        Toast.makeText (getActivity (),e.toString (),Toast.LENGTH_SHORT).show ();
                        e.printStackTrace ( );
                    }
                    try {
                        ftpClient.login(user,pass);
                    }catch (IOException e){
                        Toast.makeText (getActivity (),e.toString (),Toast.LENGTH_SHORT).show ();
                        e.printStackTrace ( );
                    }
                    ftpClient.enterLocalPassiveMode();
                    try {
                        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                    }catch (IOException e){
                        Toast.makeText (getActivity (),e.toString (),Toast.LENGTH_SHORT).show ();
                        e.printStackTrace ( );
                    }
                }
                /////end if selection

            }
        });

        ///download image from ftp by clicking on the camera icon///

        ///setup Image Switcher///
        imageShopsSw.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {

                ImageView imageShopsView = new ImageView(getActivity().getApplicationContext());
                return imageShopsView;
            }
        });

        return v;


    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtShopsId = view.findViewById(R.id.txtshopsId);
    }

    ///added for pass data in fragment

    void displayReceivedData(String message){
        txtShopsId.setText(message);
        globalShopsId = txtShopsId.getText().toString();
        System.out.println("Global Shops ID:"+globalShopsId);
    }
    public void pickShopsImagesIntent() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.putExtra(intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select image(s)"),PICK_IMAGES_CODE);
        imageShopsUri=new ArrayList<>();


    }

    public String getShopsImagePath(Uri uri){
        Cursor shopCursor = getActivity().getContentResolver().query(uri,null,null,null,null);
        shopCursor.moveToFirst();
        String documentShop_id = shopCursor.getString(0);
        documentShop_id = documentShop_id.substring(documentShop_id.lastIndexOf(":")+1);

        //cursor.close ( );
        //Cursor cursor = getContentResolver ( ).query (
        //  MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        //     null, MediaStore.Images.Media._ID + " =? ", new String[]{document_id}, null
        // );
        //cursor.moveToFirst ( );
        String shopPath =shopCursor.getString(shopCursor.getColumnIndex(MediaStore.Images.Media.DATA));
        shopCursor.close();

    return shopPath;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_CODE){
            if(resultCode == Activity.RESULT_OK){
                if(data.getClipData()!= null){
                    //pick multiple image//
                    int cout = data.getClipData().getItemCount();
                    System.out.println("ALiiiiiiiiiiiiiiiiiiiiiiiiii "+cout);
                    imageShopsSrc = new String[cout];
                    for (int i = 0 ;i<cout;i++){
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageShopsUri.add(imageUri);

                        //add image int the recyclerView
                        imageShopsList.add(new ShopsImageListView(R.drawable.imgbtn_foreground,getShopsImagePath(imageUri),R.drawable.imgdelete_background));
                        // get path of image of all in one Shot
                        imageShopsSrc[i]= getShopsImagePath(imageUri);

                        System.out.println("Double print"+imageShopsSrc[i]);


                    }
                    imageShopsSw.setImageURI(imageShopsUri.get(0));
                    pos=0;
                }else {
                    Uri imageUri = data.getData();
                    imageShopsUri.add(imageUri);
                    //set image to our main screen image////
                    imageShopsSw.setImageURI(imageShopsUri.get(0));
                    pos=0;
                }
            }
        }
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
}
