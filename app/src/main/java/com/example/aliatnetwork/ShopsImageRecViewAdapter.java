package com.example.aliatnetwork;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShopsImageRecViewAdapter  extends RecyclerView.Adapter<ShopsImageRecViewAdapter.ShopsViewHolder> {


    private Context context;
    private ArrayList<ShopsImageListView> ShopsImages;
    String server = null;
    int port = 0;
    String user =null;
    String pass = null;
    private Connection conn;
    private ImageView imageIs;


    FTPClient ftpClient = new FTPClient();
    public ShopsImageRecViewAdapter(Context context,ArrayList<ShopsImageListView> ShopsImages){
        this.context=context;
        this.ShopsImages=ShopsImages;
    }
    @NonNull
    @Override
    public ShopsImageRecViewAdapter.ShopsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v;
       v= LayoutInflater.from(context).inflate(R.layout.image_shops_list_item,parent,false);
        ShopsViewHolder shopsViewHolder = new ShopsViewHolder(v);
        return shopsViewHolder;
    }

    @Override
    public void onBindViewHolder(ShopsImageRecViewAdapter.ShopsViewHolder holder, int position) {

        holder.imgIconShops.setImageResource(ShopsImages.get(position).getIMAGE_ICON());
        holder.imgPathShops.setText(ShopsImages.get(position).getIMAGE_PATH());
        holder.imgDeleteShops.setImageResource(ShopsImages.get(position).getDELETE());

        holder.imgDeleteShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) holder.imgDeleteShops.getTag();
                Toast.makeText(context,ShopsImages.get(position).getIMAGE_PATH()+"Clicked!",Toast.LENGTH_SHORT).show();
                String str = ShopsImages.get(position).getIMAGE_PATH();
                System.out.println("str str str str   "+str);

                new AlertDialog.Builder(context)
                        .setTitle("Are You Sure You Want To Delete This Photo ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                connecttoDB();
                                PreparedStatement stmtDeleteImageShops=null;
                                try {
                                    stmtDeleteImageShops=conn.prepareStatement("delete SHOPS_IMAGE where IMAGE_PATH='"+str+"'");
                                }catch (SQLException throwables){
                                    throwables.printStackTrace();
                                }
                                try {
                                    stmtDeleteImageShops.executeQuery();
                                }catch (SQLException throwables){
                                    throwables.printStackTrace();
                                }
                                try {
                                    stmtDeleteImageShops.close();
                                    conn.close();
                                }catch (SQLException throwables){
                                    throwables.printStackTrace();
                                }
                                //calling FTP cradantials
                                com.example.aliatnetwork.FTP ftp = new com.example.aliatnetwork.FTP();
                                server =ftp.getServer();
                                port=ftp.getPort();
                                user = ftp.getUser();
                                pass=ftp.getPass();

                                try {
                                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                    StrictMode.setThreadPolicy(policy);
                                    ftpClient.connect(server,port);
                                    if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){
                                        boolean status = ftpClient.login(user,pass);
                                        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                                        ftpClient.enterLocalPassiveMode();
                                        ftpClient.deleteFile(str);
                                    }
                                }catch (IOException e){
                                    Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();

                                }
                                try {
                                    ftpClient.login(user,pass);
                                }catch (IOException e){
                                    Toast.makeText (context,e.toString (),Toast.LENGTH_SHORT).show ();
                                    e.printStackTrace();
                                }
                                ftpClient.enterLocalPassiveMode();
                                try {
                                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                                }catch (IOException e){
                                    Toast.makeText (context,e.toString (),Toast.LENGTH_SHORT).show ();
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(context,shopslist.class);
                                context.startActivity(intent);
                            }
                        }).setNegativeButton("NO",null)
                        .show();

            }
        });

        holder.imgIconShops.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                holder.imgIconShops.setColorFilter(Color.GREEN);

                Intent intent = new Intent(context,ShopsImageDisplay.class);
                intent.putExtra("message_key",ShopsImages.get(position).getIMAGE_PATH());
                Toast.makeText(context,"Display Image",Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.imgIconShops.setColorFilter(Color.YELLOW);
                    }
                },5000);

            }
        });
    }

    @Override
    public int getItemCount() {
        return ShopsImages.size();
    }

    public class ShopsViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgIconShops;
        private TextView imgPathShops;
        private ImageView imgDeleteShops;
        private ImageView imageIs;


        public ShopsViewHolder(@NonNull View itemView) {
            super(itemView);

            imgIconShops= itemView.findViewById(R.id.imgShopsIconItem);
            imgPathShops=itemView.findViewById(R.id.imgpathShop);
            imgDeleteShops=itemView.findViewById(R.id.imgdeleteShopsItem);


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

}


