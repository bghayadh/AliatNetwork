package com.example.aliatnetwork;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ImageRecViewAdapter extends RecyclerView.Adapter<ImageRecViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ImageListView> images;
    String server = null;//"ftp.ipage.com";
    int port = 0;//21;
    String user =null;// "beid";
    String pass = null;//"10th@Loop";
    private Connection conn;



    FTPClient ftpClient = new FTPClient();
    public ImageRecViewAdapter(Context context, ArrayList<ImageListView> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ImageRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(context).inflate(R.layout.image_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageRecViewAdapter.ViewHolder holder, int position) {

        holder.imgIcon.setImageResource(images.get(position).getImageIcon());
        holder.imgpath.setText(images.get(position).getImagePath());
        holder.imgdelete.setImageResource(images.get(position).getDelete());





        holder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) holder.imgdelete.getTag();
                Toast.makeText(context, images.get(position).getImagePath() + " clicked!", Toast.LENGTH_SHORT).show();
                String str = images.get(position).getImagePath().toString();
                System.out.println(str);

                new AlertDialog.Builder(context)
                        .setTitle("Are You Sure You Want To Delete This Photo ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                connecttoDB();

                                PreparedStatement stmtinsert1 = null;
                                try {
                                    // Delete Ware_id

                                    stmtinsert1 = conn.prepareStatement("delete  WAREHOUSE_IMAGE   where IMAGE_PATH ='" + str + "' ");

                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }

                                try {
                                    stmtinsert1.executeUpdate();
                                   // Toast.makeText(context, "Delete Completed", Toast.LENGTH_SHORT).show();
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }


                                try {
                                    stmtinsert1.close();
                                    conn.close();
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }

                                //calling FTP cradantials
                             com.example.aliatnetwork.FTP ftp =new com.example.aliatnetwork.FTP();
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
                                        boolean status = ftpClient.login (user, pass);
                                        ftpClient.setFileType (FTP.BINARY_FILE_TYPE);
                                        ftpClient.enterLocalPassiveMode ( );

                                        ftpClient.deleteFile(str);
                                        // Toast.makeText (context,"DELETED",Toast.LENGTH_SHORT).show ();
                                    }


                                } catch (IOException e) {
                                    Toast.makeText (context,e.toString (),Toast.LENGTH_SHORT).show ();
                                    e.printStackTrace ( );
                                }
                                try {
                                    ftpClient.login (user, pass);
                                } catch (IOException e) {
                                    Toast.makeText (context,e.toString (),Toast.LENGTH_SHORT).show ();
                                    e.printStackTrace ( );
                                }
                                ftpClient.enterLocalPassiveMode ( );

                                try {
                                    ftpClient.setFileType (FTP.BINARY_FILE_TYPE);
                                } catch (IOException e) {
                                    Toast.makeText (context,e.toString (),Toast.LENGTH_SHORT).show ();
                                    e.printStackTrace ( );
                                }

                                Intent intent = new Intent(context, SiteListViewActivity.class);
                                context.startActivity(intent);

                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();



            }
        });

        holder.imgIcon.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // pass on click camera icon value to new activity ImageDisplay

                holder.imgIcon.setColorFilter(Color.GREEN);

                Intent intent =  new Intent(context, ImageDisplay.class);
                intent.putExtra("message_key", images.get(position).getImagePath ());
                context.startActivity(intent);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.imgIcon.setColorFilter(Color.YELLOW);
                    }
                },5000);


            }
        });

    }


    @Override
    public int getItemCount() {
        return images.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgIcon;
        private TextView imgpath;
        private ImageView imgdelete;
        private ImageView imageIs;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgIcon= itemView.findViewById(R.id.imgIcon);
            imgpath=itemView.findViewById(R.id.imgpath);
            imgdelete=itemView.findViewById(R.id.imgdelete);
            imageIs=itemView.findViewById(R.id.imagesIs);

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
            //Toast.makeText (context,"Connected to the database",Toast.LENGTH_SHORT).show ();
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println("error is: " +e.toString());
            Toast.makeText (context,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (context,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (java.lang.InstantiationException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (context,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }
    }
    Timer timer;





}


