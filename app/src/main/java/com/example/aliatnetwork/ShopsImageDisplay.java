package com.example.aliatnetwork;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;

public class ShopsImageDisplay extends AppCompatActivity {

    private TextView imageShopsPath;
    private ImageView imageShopsSet;
    private Button btnShopsBack;
    String server = null;
    int port = 0;
    String user = null;
    String pass = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shops_activity_image_display);
        FTPClient ftpClient = new FTPClient();
        imageShopsPath=findViewById(R.id.imageShopPath);
        imageShopsSet=findViewById(R.id.imageShopSet);
        btnShopsBack=findViewById(R.id.btnShopsBack);
        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");
        System.out.println(str);
        imageShopsPath.setText(str);

        //calling ftp cradentials
        FTP ftp =new FTP();
        server = ftp.getServer().toString();
        port=ftp.getPort();
        user=ftp.getUser().toString();
        pass=ftp.getPass().toString();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder ( ).permitAll ( ).build ( );
            StrictMode.setThreadPolicy (policy);
            ftpClient.connect (server, port);
            if (FTPReply.isPositiveCompletion (ftpClient.getReplyCode ( ))) {
                // login using username & password
                boolean success = ftpClient.login(user, pass);
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();



                Bitmap bitmap = null;
                InputStream inStream = ftpClient
                        .retrieveFileStream(str);
                // ftpClient.storeFile(fileName, buffIn);
                if(inStream != null) {
                    System.out.println("Success");
                    bitmap = BitmapFactory.decodeStream(inStream);
                    System.out.println(bitmap);
                    imageShopsSet.setImageBitmap(bitmap);


                    inStream.close();
                }
                else
                {
                    System.out.println("Empty");
                }

            }



        } catch (IOException e) {
            Toast.makeText (getApplicationContext(),e.toString (),Toast.LENGTH_SHORT).show ();
            e.printStackTrace ( );
        }
        try {
            ftpClient.login (user, pass);
        } catch (IOException e) {
            Toast.makeText (getApplicationContext(),e.toString (),Toast.LENGTH_SHORT).show ();
            e.printStackTrace ( );
        }
        ftpClient.enterLocalPassiveMode ( );

        try {
            ftpClient.setFileType (FTPClient.BINARY_FILE_TYPE);
        } catch (IOException e) {
            Toast.makeText (getApplicationContext(),e.toString (),Toast.LENGTH_SHORT).show ();
            e.printStackTrace ( );
        }

        btnShopsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }

}

