package com.example.aliatnetwork;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.BinaryOperator;

public class SiteActivity extends AppCompatActivity {
    private ArrayList<Uri> imagesUris;
    private static final int PICK_IMAGES_CODE=0;
    private String[] imagesource;
    int position =0;

    String server = "ftp.ipage.com";
    int port = 21;
    String user = "beid";
    String pass = "10th@Loop";
    FTPClient ftpClient = new FTPClient();

    private ImageSwitcher imagesIs;
    private Button previousBtn,nextBtn,btnpickimages,btnftpimages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.siteslist);

        imagesIs=findViewById(R.id.imagesIs);
        Button previousBtn = findViewById (R.id.previousBtn);
        Button nextBtn = findViewById (R.id.nextBtn);
        Button btnpickimages = findViewById (R.id.btnpickimages);
        Button btnftpimages = findViewById (R.id.btnftpimages);
        imagesUris=new ArrayList<>();


        // Run previous button
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position >0){
                    position--;
                    imagesIs.setImageURI(imagesUris.get(position));
                }else {
                    Toast.makeText(SiteActivity.this,"No previous image",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Run Next button
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < imagesUris.size()-1){
                    position++;
                    imagesIs.setImageURI(imagesUris.get(position));

                }else {
                    Toast.makeText(SiteActivity.this,"No more image",Toast.LENGTH_SHORT).show();
                }
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
                    Toast.makeText (SiteActivity.this,"Images not selected yet",Toast.LENGTH_SHORT).show ();
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
                            ftpClient.changeWorkingDirectory (workingDir + "bassam");
                            ftpClient.setFileType (FTP.BINARY_FILE_TYPE);
                            workingDir = ftpClient.printWorkingDirectory ( );

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
                                    }



                                    //srcFilePath is source to read
                                    // workingDir  is destination to upload
                                    FileInputStream srcFileStream = new FileInputStream (srcFilePath);
                                    //System.out.println ("OPEN STREAM ");
                                    //status = ftpClient.storeFile(workingDir, srcFileStream);

                                    // upload function
                                    //System.out.println ("upload " + srcFileStream + "Destination " + workingDir);
                                    ftpClient.storeFile (destination1, srcFileStream);
                                    srcFileStream.close ( );
                                }
                                Toast.makeText (SiteActivity.this,"upload Completed",Toast.LENGTH_SHORT).show ();
                                imagesource=null;

                            } catch (Exception e) {
                                Toast.makeText (SiteActivity.this,e.toString (),Toast.LENGTH_SHORT).show ();
                                e.printStackTrace ( );
                            }

                        }

                    } catch (IOException e) {
                        Toast.makeText (SiteActivity.this,e.toString (),Toast.LENGTH_SHORT).show ();
                        e.printStackTrace ( );
                    }
                try {
                    ftpClient.login (user, pass);
                } catch (IOException e) {
                    Toast.makeText (SiteActivity.this,e.toString (),Toast.LENGTH_SHORT).show ();
                    e.printStackTrace ( );
                }
                ftpClient.enterLocalPassiveMode ( );

                try {
                    ftpClient.setFileType (FTP.BINARY_FILE_TYPE);
                } catch (IOException e) {
                    Toast.makeText (SiteActivity.this,e.toString (),Toast.LENGTH_SHORT).show ();
                    e.printStackTrace ( );
                }
              } // end if selection array not null
            }
        });

        //setup Image Swircher
        imagesIs.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView=new ImageView(getApplicationContext());
                return imageView;
            }
        });


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
        Cursor cursor = getContentResolver ( ).query (uri, null, null, null, null);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

}
