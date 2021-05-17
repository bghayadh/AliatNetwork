package com.example.aliatnetwork;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Activity;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import java.io.FileInputStream;
import java.io.IOException;

import static com.example.aliatnetwork.R.layout.fragment_imagefragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Imagefragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Imagefragment extends  Fragment  {
    private String globalwareid;
    private ImageSwitcher imagesIs;
    private ArrayList<Uri> imagesUris;
    private static final int PICK_IMAGES_CODE=0;
    private String[] imagesource;
    private TextView txtimgpath;
    private ImageButton imgbtn;
    private TextView txtwareid;
    int position =0;
    String server = "ftp.ipage.com";
    int port = 21;
    String user = "beid";
    String pass = "10th@Loop";
    FTPClient ftpClient = new FTPClient();
    private RecyclerView imagerecView;
    private ArrayList<ImageListView> images,imagesdb;
    public Connection conn;
    private View rootView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Imagefragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Imagefragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Imagefragment newInstance(String param1, String param2) {
        Imagefragment fragment = new Imagefragment ( );
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate (fragment_imagefragment, container, false);
        View V = inflater.inflate (fragment_imagefragment, container, false);
        Button previousBtn = (Button) V.findViewById(R.id.previousBtn);
        Button btnmain = (Button) V.findViewById(R.id.btnmain);
        Button nextBtn = (Button) V.findViewById(R.id.nextBtn);
        Button btnpickimages = (Button) V.findViewById(R.id.btnpickimages);
        Button btnftpimages = (Button) V.findViewById(R.id.btnftpimages);
        imagesIs = (ImageSwitcher) V.findViewById(R.id.imagesIs);
        imagesUris=new ArrayList<>();
        imagerecView = V.findViewById(R.id.imageRecView);
        images = new ArrayList<>();
        imagesdb = new ArrayList<>();
        txtimgpath = (TextView) V.findViewById(R.id.imgpath);



     /*   ///receiver
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            String data = bundle.getString("key1");
            System.out.println("receiving data : "+data);
            globalwareid=data;
        }*/
        //System.out.println ("here is the received data : "+((SiteInfoActivity)getActivity()).settofragment().toString());



        //read passes value of ware_id from recylserview
        Intent intent = getActivity().getIntent();
        String str = intent.getStringExtra("message_key");
        globalwareid = str.toString();



        /////////////////////////////////////////////////////////////////////////////////////////////////////////

//connect to database
        connecttoDB();
        Statement stmt1 = null;
        int i=0;
        try {
            stmt1 = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String  sqlStmt = "Select IMAGE_PATH FROM WAREHOUSE_IMAGE WHERE WARE_ID='"+globalwareid+"'";

        ResultSet rs1 = null;
        try {
            rs1 = stmt1.executeQuery(sqlStmt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        while (true) {
            try {
                if (!rs1.next()) break;
                imagesdb.add(new ImageListView (R.drawable.imgdelete_foreground,R.drawable.imgbtn_foreground,rs1.getString("IMAGE_PATH")));
            }
            catch (SQLException throwables) {
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
        for(i=0;i<imagesdb.size();i++) {
            images.add(new ImageListView(R.drawable.imgdelete_foreground,R.drawable.imgbtn_foreground, imagesdb.get(i).getImagePath()));
        }
        ///fill the recyclerview with imagespath
        ImageRecViewAdapter imageRecViewAdapter = new ImageRecViewAdapter(getContext(),images);
        imagerecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        imagerecView.setAdapter(imageRecViewAdapter);
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position >0){
                    position--;
                    imagesIs.setImageURI(imagesUris.get(position));
                }else {
                    Toast.makeText(getActivity (),"No previous image",Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < imagesUris.size()-1){
                    position++;
                    imagesIs.setImageURI(imagesUris.get(position));


                }else {
                    Toast.makeText(getActivity (),"No more image",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ///return to main page
        btnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });

        /// Select images from gallery
        btnpickimages.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                pickImagesIntent();
            }
        });

        /// Run this function to move images to ftp server
        btnftpimages.setOnClickListener (new View.OnClickListener ( ) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (imagesource == null) {
                    Toast.makeText (getActivity (),"Images not selected yet",Toast.LENGTH_SHORT).show ();
                } else {
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
                            ftpClient.changeWorkingDirectory (workingDir + "Sites");

                            //return true if the directory found
                            System.out.println(ftpClient.changeWorkingDirectory (workingDir + "Sites"));
                            //System.out.println(ftpClient.changeWorkingDirectory (workingDir + "Sites"+workingDir+"WARE_2021_211"));


                            ftpClient.makeDirectory(globalwareid);
                            ftpClient.changeWorkingDirectory(workingDir+"Sites"+"/"+globalwareid);
                            // check if the directory correctly created
                            System.out.println(ftpClient.changeWorkingDirectory(workingDir+"Sites"+"/"+globalwareid));
                            ftpClient.setFileType (FTP.BINARY_FILE_TYPE);
                            workingDir = ftpClient.printWorkingDirectory ( );

                            System.out.println("Directory: "+workingDir);

                            ///validate warehouse id folder if not found created else change directory

                            /// /Sites/WARE_ID/Image_name
                            // upload file
                            try {
                                for (int i = 0; i < imagesource.length; i++) {
                                    String srcFilePath = imagesource[i];
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
                                    PreparedStatement stmtinsert1 = null;


                                    try {
                                        // System.out.println("id:"+globalwareid);

                              /*  System.out.println("insert into WAREHOUSE_IMAGE (WARE_ID,IMAGE_PATH,UPLOAD_DATE) values " +
                                        "('"+globalwareid +"','"+getpath(imageUri)  +"',sysdate)");*/

                                        stmtinsert1 = conn.prepareStatement("insert into WAREHOUSE_IMAGE (WARE_ID,IMAGE_PATH,UPLOAD_DATE) values " +
                                                "('"+globalwareid +"','"+path  +"', sysdate)");

                                    }
                                    catch (SQLException throwables) {
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

                                }
                                Toast.makeText (getActivity (),"upload Completed",Toast.LENGTH_SHORT).show ();
                                imagesource=null;

                            } catch (Exception e) {
                                Toast.makeText (getActivity (),e.toString (),Toast.LENGTH_SHORT).show ();
                                e.printStackTrace ( );
                            }

                        }


                    } catch (IOException e) {
                        Toast.makeText (getActivity (),e.toString (),Toast.LENGTH_SHORT).show ();
                        e.printStackTrace ( );
                    }
                    try {
                        ftpClient.login (user, pass);
                    } catch (IOException e) {
                        Toast.makeText (getActivity (),e.toString (),Toast.LENGTH_SHORT).show ();
                        e.printStackTrace ( );
                    }
                    ftpClient.enterLocalPassiveMode ( );

                    try {
                        ftpClient.setFileType (FTP.BINARY_FILE_TYPE);
                    } catch (IOException e) {
                        Toast.makeText (getActivity (),e.toString (),Toast.LENGTH_SHORT).show ();
                        e.printStackTrace ( );
                    }

                } // end if selection array not null

            }
        });

        // download image from ftp by clicking on the camera icon



        //setup Image Swircher
        imagesIs.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView=new ImageView(getActivity ().getApplicationContext());
                return imageView;
            }
        });


        // after you've done all your manipulation, return your layout to be shown
        return V;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ///added for pass data in fragment
        txtwareid = view.findViewById(R.id.txtwareid);
    }

    ///added for pass data in fragment
    void displayReceivedData(String message) {
        //txtwareid.setText("Data received: " + message);
        txtwareid.setText(message);
        globalwareid = txtwareid.getText().toString();
        System.out.println("Global ware ID: "+globalwareid);
    }

    public void pickImagesIntent() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.putExtra(intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select image(s)"),PICK_IMAGES_CODE);
        imagesUris=new ArrayList<>();
    }

    public String getpath(Uri uri) {
        Cursor cursor = getActivity ().getContentResolver ( ).query (uri, null, null, null, null);
        cursor.moveToFirst ( );
        String document_id = cursor.getString (0);
        document_id = document_id.substring (document_id.lastIndexOf (":") + 1);

        //cursor.close ( );
        //Cursor cursor = getContentResolver ( ).query (
        //  MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        //     null, MediaStore.Images.Media._ID + " =? ", new String[]{document_id}, null
        // );
        //cursor.moveToFirst ( );

        String path = cursor.getString (cursor.getColumnIndex (MediaStore.Images.Media.DATA));
        cursor.close ( );
        return path;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                if (data.getClipData() != null) {
                    // pick multiple images
                    int cout=data.getClipData().getItemCount();
                    imagesource = new String[cout];
                    for (int i=0;i<cout; i++) {
                        //get URI of images per index
                        Uri imageUri=data.getClipData().getItemAt(i).getUri();
                        imagesUris.add(imageUri);

                        ///add images in the recyclerview
                         images.add(new ImageListView(R.drawable.imgdelete_foreground,R.drawable.imgbtn_foreground,getpath(imageUri)));
                        // get path of image of all in one shot
                        //System.out.println(getpath(imageUri));
                        imagesource[i]=getpath(imageUri);
                        System.out.println("Double print "+ imagesource[i]);
                        // get path of image one by one
                        /** Uri selectedImageUri = data.getData ();
                         String[] projection = {MediaStore.Images.Media.DATA};

                         try {
                         Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
                         cursor.moveToFirst();
                         int columnIndex = cursor.getColumnIndex(projection[0]);
                         String picturePath = cursor.getString(columnIndex);
                         cursor.close();
                         System.out.println("Picture Path "+ picturePath);

                         }
                         catch(Exception e) {
                         System.out.println("Path Error"+ e.toString());
                         } **/


                    }

                    // set first image to our main screen image
                    imagesIs.setImageURI(imagesUris.get(0));
                    position=0;

                } else {
                    //pick single image
                    Uri imageUri=data.getData();
                    imagesUris.add(imageUri);
                    // set  image to our main screen image
                    imagesIs.setImageURI(imagesUris.get(0));
                    position=0;
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

