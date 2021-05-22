package com.example.aliatnetwork;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

import android.Manifest;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;


public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
    TelephonyManager telephonyManager;
    public Connection conn;
    //define buttons
    private Button Btncoverage, Btnspeed, Btnsites, Btnshops, Btnstartspeed, Btnstartcoverage, Btngis, Btntickets, Btncapturespeed, Btncapturecoverage, BtnExit, Btndeviceid, Btniccid;
    private TextView Notifyspeedtesttime, Notifycoveragetesttime, txtlat, txtlng, downloadTextView, uploadTextView, txtsignal, textinfo;
    private GpsTracker gpsTracker;


    // read uploadTextView and downloadTextView
    static int position = 0;
    static int lastPosition = 0;
    GetSpeedTestHostsHandler getSpeedTestHostsHandler = null;
    HashSet<String> tempBlackList;

    @Override
    public void onResume() {
        super.onResume ( );
        getSpeedTestHostsHandler = new GetSpeedTestHostsHandler ( );
        getSpeedTestHostsHandler.start ( );
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        //initialize objects on Load
        Btncoverage = findViewById (R.id.Btncoverage);
        Btnspeed = findViewById (R.id.Btnspeed);
        Btnsites = findViewById (R.id.Btnsites);
        Btnshops = findViewById (R.id.Btnshops);
        Btnstartspeed = findViewById (R.id.Btnstartspeed);
        Btnstartcoverage = findViewById (R.id.Btnstartcoverage);
        Notifyspeedtesttime = findViewById (R.id.Notifyspeedtesttime);
        Notifycoveragetesttime = findViewById (R.id.Notifycoveragetesttime);
        BtnExit = findViewById (R.id.BtnExit);
        Btngis = findViewById (R.id.Btngis);
        Btntickets = findViewById (R.id.Btntickets);
        Btncapturecoverage = findViewById (R.id.Btncapturecoverage);
        Btncapturespeed = findViewById (R.id.Btncapturespeed);
        Btndeviceid = findViewById (R.id.Btndeviceid);
        Btniccid = findViewById (R.id.Btniccid);

        // text to display latitude and longitude and Dwonload and upload
        txtlat = findViewById (R.id.txtlat);
        txtlng = findViewById (R.id.txtlng);
        downloadTextView = findViewById (R.id.downloadTextView);
        uploadTextView = findViewById (R.id.uploadTextView);
        txtsignal = findViewById (R.id.txtsignal);
        textinfo = findViewById (R.id.textinfo);

        // check if we have permission to get our location in manifest xml file
        try {
            if (ContextCompat.checkSelfPermission (getApplicationContext ( ), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions (this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace ( );
        }

        // connect to Oracle DB
        try {
            connecttoDB ( );

            //Get how many tome we run speed test today
            Statement stmt1 = null;
            int i = 0;
            try {
                stmt1 = conn.createStatement ( );
            } catch (SQLException throwables) {
                throwables.printStackTrace ( );
            }
            String sqlStmt = "select count(1) as compteur from SPEEDTEST where TO_CHAR(SPEED_DATE, 'YYYY-MM-DD')=TO_CHAR(SYSDATE, 'YYYY-MM-DD')";
            ResultSet rs1 = null;
            try {
                rs1 = stmt1.executeQuery (sqlStmt);
            } catch (SQLException throwables) {
                throwables.printStackTrace ( );
            }
            while (true) {
                try {
                    if (!rs1.next ( )) break;
                    Notifyspeedtesttime.setText (rs1.getString ("compteur"));
                    //System.out.println(rs1.getString("compteur"));

                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }
            }
            try {
                rs1.close ( );
            } catch (SQLException throwables) {
                throwables.printStackTrace ( );
            }
            try {
                stmt1.close ( );
            } catch (SQLException throwables) {
                throwables.printStackTrace ( );
            }


            //Get how many tome we run coverage test today
            Statement stmt2 = null;
            int i2 = 0;
            try {
                stmt2 = conn.createStatement ( );
            } catch (SQLException throwables) {
                throwables.printStackTrace ( );
            }
            String sqlStmt2 = "select count(1) as compteur from COVERAGETEST where TO_CHAR(COVERAGE_DATE, 'YYYY-MM-DD')=TO_CHAR(SYSDATE, 'YYYY-MM-DD')";
            ResultSet rs2 = null;
            try {
                rs2 = stmt2.executeQuery (sqlStmt2);
            } catch (SQLException throwables) {
                throwables.printStackTrace ( );
            }
            while (true) {
                try {
                    if (!rs2.next ( )) break;
                    Notifycoveragetesttime.setText (rs2.getString ("compteur"));
                    //System.out.println(rs2.getString("compteur"));

                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }
            }
            try {
                rs2.close ( );
            } catch (SQLException throwables) {
                throwables.printStackTrace ( );
            }
            try {
                stmt2.close ( );
                conn.close ( );
            } catch (SQLException throwables) {
                throwables.printStackTrace ( );
            }
        } catch (Exception e) {
            Toast.makeText (this,"Cannot access oracle "+e.toString (),Toast.LENGTH_SHORT).show ();
        }

        //GET ICCID
        Btniccid.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    SubscriptionManager sm = SubscriptionManager.from (getApplicationContext ( ));
                    if (ActivityCompat.checkSelfPermission (MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
                    List<SubscriptionInfo> sis = sm.getActiveSubscriptionInfoList ( );
                    System.out.println ("sis is :  "+sis.size()  );
                    if (sis.size() == 1)
                    {
                        SubscriptionInfo si1 = sis.get(0);
                        //String iccId1 = si1.getIccId();
                        //String phoneNum1 = si1.getNumber();
                        String res1=sis.get(0).toString ();
                        //retrieve iccid value from result
                        int n1 = res1.indexOf("simSlotIndex=");
                        int n2 = res1.indexOf("iccId=");
                        res1 = res1.substring((n2+6),n1).trim();
                        textinfo.setText ("ICCID         "+res1);

                        //get number
                       //String res=sis.get(0).toString ();
                        // n1 = res.indexOf("dataRoaming=");
                       // System.out.println (n1);
                       //  n2 = res.indexOf("number=");
                      //  System.out.println (n2);
                      //  res = res.substring((n2+6),n1).trim();
                      //  System.out.println ("value is :" +res);
                      //  texticcid.setText (texticcid.getText () +"  "+ "Number = "+res);


                    }
                    if (sis.size() == 2)
                    {
                        SubscriptionInfo si2 = sis.get(1);
                        String res1=sis.get(1).toString ();
                        //retrieve iccid value from result
                        int n1 = res1.indexOf("simSlotIndex=");
                        int n2 = res1.indexOf("iccId=");
                        res1 = res1.substring((n2+6),n1).trim();
                        textinfo.setText ("ICCID         "+res1);
                    }
                    // Get information about the number of SIM cards:
                    int count = sm.getActiveSubscriptionInfoCount();//Current actual number of cards
                    int max   = sm.getActiveSubscriptionInfoCountMax();//Current card slot number
                }
            }
        });


        //Get Device ID
        Btndeviceid.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                String ID = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                textinfo.setText("Device ID      "+ID);//displaying the information in the textView

            }
        });



        // exit button
        BtnExit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });



        // click to move to coverage List
        Btncoverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCoverageActivity();
            }
            public void openCoverageActivity(){
                Intent intent = new Intent(MainActivity.this, CoverageActivity.class);
                startActivity(intent);
            }
        });

        // click to move to speed List
        Btnspeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                //Show message to know where are we transferred
                Toast.makeText(MainActivity.this,  "Welcome to Speed page",Toast.LENGTH_SHORT).show();
                //Call Layout /form speedlist
                Intent intent =new Intent(MainActivity.this,speedlist.class);
                startActivity(intent);
                  **/
                openSpeedActivity();
            }
            public void openSpeedActivity(){
                Intent intent = new Intent(MainActivity.this, SpeedActivity.class);
                startActivity(intent);
            }
        });

        // click to move to site List
        Btnsites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show message to know where are we transferred
               // Toast.makeText(MainActivity.this,  "Welcome to Sites page",Toast.LENGTH_SHORT).show();
              //  //Call Layout /form sitelist
              //  Intent intent =new Intent(MainActivity.this,siteslist.class);
              //  startActivity(intent);

             // to call ftp image
                //openSiteActivity();

                // to call site list view
                openSitelistviewActivity();
            }
            // to call ftp image
           // public void openSiteActivity(){
             //   Intent intent = new Intent(MainActivity.this, SiteActivity.class);
              //  startActivity(intent);
           // }
            /// end call ftp image

            // to call site list view


             public void openSitelistviewActivity(){
               Intent intent = new Intent(MainActivity.this, SiteListViewActivity.class);
              startActivity(intent);
            }

        });

        // to move to ticket
        Btntickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openTicketlistviewActivity();
            }


            public void openTicketlistviewActivity(){
                Intent intent = new Intent(MainActivity.this, TicketListViewActivity.class);
                startActivity(intent);
            }

        });

        // click to move to shop List
        Btnshops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show message to know where are we transferred
                Toast.makeText(MainActivity.this,  "Welcome to Shops page",Toast.LENGTH_SHORT).show();
                //Call Layout /form shoplist
                Intent intent =new Intent(MainActivity.this,shopslist.class);
                startActivity(intent);
            }
        });

        // click to get signal coverage NO Timer only one Time
        Btncapturecoverage.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if (Btnstartcoverage.getText() != "Coverage Stop Test") {
                    Btncapturecoverage.setTextColor(Color.parseColor("#EC0D0D"));
                    GetCoverageSignal  ( );
                    Btncapturecoverage.setTextColor(Color.parseColor("#0A5EA8"));
                } else
                {
                    Toast.makeText (MainActivity.this,"Stop Coverage test before running capture",Toast.LENGTH_SHORT).show ();
                }
            }
        });



        // click to get signal coverage using Timer
        Btnstartcoverage.setOnClickListener (new View.OnClickListener ( ) {
            private CountDownTimer Timer = null;

            @Override
            public void onClick(View v) {
                /**
                 Timer timerObj = new Timer();
                 TimerTask timerTaskObj = new TimerTask() {
                 public void run() {
                 System.out.println("test timer");
                 }
                 };
                 /// 60 000 menas 1 min
                 timerObj.schedule(timerTaskObj, 0, 60000);**/


                //Run unitl 21600000 is 6 hours           run every 180000 is 3 minutes
                if (Timer != null)
                {
                    Timer.cancel ( );
                    //System.out.println ( "TIMER CLOSED NOW" );
                    Timer = null;
                    Btnstartcoverage.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    Btnstartcoverage.setText("Coverage Start Test");
                }
                    else {
                Timer = new CountDownTimer (32400000, 180000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        Btnstartcoverage.setTextColor(Color.parseColor("#F50CFA03"));
                        Btnstartcoverage.setText("Coverage Stop Test");
                        System.out.println ( "TIMER STARTED NOW" );
                        int progress = (int) ( millisUntilFinished / 150 );
                        //Call coverage Signal function
                        GetCoverageSignal ( );
                    }

                    @Override
                    public void onFinish() {
                        Timer.cancel ();
                    }
                }.start ( );
            }
                //GetCoverageSignal();
            }
        });

        //click speed test to run and get test as per Latitude and Longitude NO TIMER only one time
        Btncapturespeed.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if (Btnstartspeed.getText() != "Speed Stop Test") {
                    Btncapturespeed.setEnabled(false);
                    GetSpeedSignal ( );
                    Btncapturespeed.setEnabled (true);
                } else
                {
                    Toast.makeText (MainActivity.this,"Stop Speed test before running capture",Toast.LENGTH_SHORT).show ();
                }
            }
        });


        //click speed test to run and get test as per Latitude and Longitude uisng TIMER
        Btnstartspeed.setOnClickListener (new View.OnClickListener ( ) {
            private CountDownTimer Timer = null;
            //@SuppressLint("MissingSuperCall")
            @Override
            public void onClick(View v) {

                if (Timer != null)
                {
                    Timer.cancel ( );
                    //System.out.println ( "TIMER CLOSED NOW" );
                    Timer = null;
                    //Btnstartspeed.setTextSize (12);
                    Btnstartspeed.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    Btnstartspeed.setText("Speed Start Test");
                } else {
                    //Run unitl 21600000 is 6 hours           run every 180000 is 3 minutes
                    Timer = new CountDownTimer (32400000, 180000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Btnstartspeed.setTextColor(Color.parseColor("#F50CFA03"));
                            Btnstartspeed.setText("Speed Stop Test");
                            System.out.println ( "TIMER STARTED NOW" );
                            int progress = (int) ( millisUntilFinished / 150 );
                            //call speed signal function
                            GetSpeedSignal ( );
                        }

                        @Override
                        public void onFinish() {
                        }
                    }.start ( );
                }

                //GetSpeedSignal();

            }

        });
    }

    @SuppressLint("MissingSuperCall")
    public void GetSpeedSignal() {
        // call class Gps to get our location
        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            txtlat.setText(String.valueOf(latitude));
            txtlng.setText(String.valueOf(longitude));
        }else{
            gpsTracker.showSettingsAlert();
            txtlat.setText("Error to get latitude");
            txtlng.setText("Error to get longitude");
        }

        //Call speedtest to get download and upload value
        final DecimalFormat dec = new DecimalFormat("#.##");
        //Btnstartspeed.setText("Begin Test");
        tempBlackList = new HashSet<>();
        getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
        getSpeedTestHostsHandler.start();

        //Restart test icin eger baglanti koparsa
        if (getSpeedTestHostsHandler == null) {
            getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
            getSpeedTestHostsHandler.start();
        }

        Btnstartspeed.setEnabled(false);

        //Restart test icin eger baglanti koparsa
        if (getSpeedTestHostsHandler == null) {
            getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
            getSpeedTestHostsHandler.start();
        }

        new Thread(new Runnable() {
            RotateAnimation rotate;
            TextView downloadTextView = (TextView) findViewById(R.id.downloadTextView);
            TextView uploadTextView = (TextView) findViewById(R.id.uploadTextView);

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // Btnstartspeed.setText("Selecting best server based on ping...");
                        //Btnstartspeed.setText("Speed Start Test");
                    }
                });

                //Get egcodes.speedtest hosts
                int timeCount = 600; //1min
                while (!getSpeedTestHostsHandler.isFinished()) {
                    timeCount--;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    if (timeCount <= 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "No Connection...", Toast.LENGTH_LONG).show();
                                Btnstartspeed.setEnabled(true);
                                Btnstartspeed.setText("Restart Test");
                            }
                        });
                        getSpeedTestHostsHandler = null;
                        return;
                    }
                }

                //Find closest server
                HashMap<Integer, String> mapKey = getSpeedTestHostsHandler.getMapKey();
                HashMap<Integer, List<String>> mapValue = getSpeedTestHostsHandler.getMapValue();
                double selfLat = getSpeedTestHostsHandler.getSelfLat();
                double selfLon = getSpeedTestHostsHandler.getSelfLon();
                double tmp = 19349458;
                double dist = 0.0;
                int findServerIndex = 0;
                for (int index : mapKey.keySet()) {
                    if (tempBlackList.contains(mapValue.get(index).get(5))) {
                        continue;
                    }

                    Location source = new Location("Source");
                    source.setLatitude(selfLat);
                    source.setLongitude(selfLon);

                    List<String> ls = mapValue.get(index);
                    Location dest = new Location("Dest");
                    dest.setLatitude(Double.parseDouble(ls.get(0)));
                    dest.setLongitude(Double.parseDouble(ls.get(1)));

                    double distance = source.distanceTo(dest);
                    if (tmp > distance) {
                        tmp = distance;
                        dist = distance;
                        findServerIndex = index;
                    }
                }
                String testAddr = mapKey.get(findServerIndex).replace("http://", "https://");
                final List<String> info = mapValue.get(findServerIndex);
                final double distance = dist;

                if (info == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Btnstartspeed.setText("There was a problem in getting Host Location. Try again later.");
                        }
                    });
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Btnstartspeed.setTextSize(14);
                        //Btnstartspeed.setText(String.format("Host Location: %s [Distance: %s km]", info.get(2), new DecimalFormat("#.##").format(distance / 1000)));
                       // Btnstartspeed.setText("Speed Stop Test");
                    }
                });

                //Init Ping graphic

                XYSeriesRenderer pingRenderer = new XYSeriesRenderer();
                XYSeriesRenderer.FillOutsideLine pingFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                pingFill.setColor(Color.parseColor("#4d5a6a"));
                pingRenderer.addFillOutsideLine(pingFill);
                pingRenderer.setDisplayChartValues(false);
                pingRenderer.setShowLegendItem(false);
                pingRenderer.setColor(Color.parseColor("#4d5a6a"));
                pingRenderer.setLineWidth(5);
                final XYMultipleSeriesRenderer multiPingRenderer = new XYMultipleSeriesRenderer();
                multiPingRenderer.setXLabels(0);
                multiPingRenderer.setYLabels(0);
                multiPingRenderer.setZoomEnabled(false);
                multiPingRenderer.setXAxisColor(Color.parseColor("#647488"));
                multiPingRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                multiPingRenderer.setPanEnabled(true, true);
                multiPingRenderer.setZoomButtonsVisible(false);
                multiPingRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                multiPingRenderer.addSeriesRenderer(pingRenderer);

                //Init Download graphic
                XYSeriesRenderer downloadRenderer = new XYSeriesRenderer();
                XYSeriesRenderer.FillOutsideLine downloadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                downloadFill.setColor(Color.parseColor("#4d5a6a"));
                downloadRenderer.addFillOutsideLine(downloadFill);
                downloadRenderer.setDisplayChartValues(false);
                downloadRenderer.setColor(Color.parseColor("#4d5a6a"));
                downloadRenderer.setShowLegendItem(false);
                downloadRenderer.setLineWidth(5);
                final XYMultipleSeriesRenderer multiDownloadRenderer = new XYMultipleSeriesRenderer();
                multiDownloadRenderer.setXLabels(0);
                multiDownloadRenderer.setYLabels(0);
                multiDownloadRenderer.setZoomEnabled(false);
                multiDownloadRenderer.setXAxisColor(Color.parseColor("#647488"));
                multiDownloadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                multiDownloadRenderer.setPanEnabled(false, false);
                multiDownloadRenderer.setZoomButtonsVisible(false);
                multiDownloadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                multiDownloadRenderer.addSeriesRenderer(downloadRenderer);

                //Init Upload graphic
                XYSeriesRenderer uploadRenderer = new XYSeriesRenderer();
                XYSeriesRenderer.FillOutsideLine uploadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                uploadFill.setColor(Color.parseColor("#4d5a6a"));
                uploadRenderer.addFillOutsideLine(uploadFill);
                uploadRenderer.setDisplayChartValues(false);
                uploadRenderer.setColor(Color.parseColor("#4d5a6a"));
                uploadRenderer.setShowLegendItem(false);
                uploadRenderer.setLineWidth(5);
                final XYMultipleSeriesRenderer multiUploadRenderer = new XYMultipleSeriesRenderer();
                multiUploadRenderer.setXLabels(0);
                multiUploadRenderer.setYLabels(0);
                multiUploadRenderer.setZoomEnabled(false);
                multiUploadRenderer.setXAxisColor(Color.parseColor("#647488"));
                multiUploadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                multiUploadRenderer.setPanEnabled(false, false);
                multiUploadRenderer.setZoomButtonsVisible(false);
                multiUploadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                multiUploadRenderer.addSeriesRenderer(uploadRenderer);

                //Reset value, graphics
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        downloadTextView.setText("0");
                        uploadTextView.setText("0");
                    }
                });
                final List<Double> pingRateList = new ArrayList<> ();
                final List<Double> downloadRateList = new ArrayList<>();
                final List<Double> uploadRateList = new ArrayList<>();
                Boolean pingTestStarted = false;
                Boolean pingTestFinished = false;
                Boolean downloadTestStarted = false;
                Boolean downloadTestFinished = false;
                Boolean uploadTestStarted = false;
                Boolean uploadTestFinished = false;

                //Init Test
                final PingTest pingTest = new PingTest(info.get(6).replace(":8080", ""), 3);
                final HttpDownloadTest downloadTest = new HttpDownloadTest(testAddr.replace(testAddr.split("/")[testAddr.split("/").length - 1], ""));
                final HttpUploadTest uploadTest = new HttpUploadTest(testAddr);


                //Tests
                while (true) {
                    if (!pingTestStarted) {
                        pingTest.start();
                        pingTestStarted = true;
                    }
                    if (pingTestFinished && !downloadTestStarted) {
                        downloadTest.start();
                        downloadTestStarted = true;
                    }
                    if (downloadTestFinished && !uploadTestStarted) {
                        uploadTest.start();
                        uploadTestStarted = true;
                    }


                    //Ping Test
                    if (pingTestFinished) {
                        //Failure
                        if (pingTest.getAvgRtt() == 0) {
                            //System.out.println("Ping error...");
                        }
                    } else {
                        pingRateList.add(pingTest.getInstantRtt());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //pingTextView.setText(dec.format(pingTest.getInstantRtt()) + " ms");
                            }
                        });

                        //Update chart
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Creating an  XYSeries for Income
                                XYSeries pingSeries = new XYSeries("");
                                pingSeries.setTitle("");

                                int count = 0;
                                List<Double> tmpLs = new ArrayList<>(pingRateList);
                                for (Double val : tmpLs) {
                                    pingSeries.add(count++, val);
                                }

                                XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                dataset.addSeries(pingSeries);

                                GraphicalView chartView = ChartFactory.getLineChartView(getBaseContext(), dataset, multiPingRenderer);


                            }
                        });
                    }


                    //Download Test
                    if (pingTestFinished) {
                        if (downloadTestFinished) {
                            //Failure
                            if (downloadTest.getFinalDownloadRate() == 0) {
                                System.out.println("Download error...");
                            } else {
                                //Success
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        downloadTextView.setText(dec.format(downloadTest.getFinalDownloadRate()));
                                    }
                                });
                            }
                        } else {
                            //Calc position
                            double downloadRate = downloadTest.getInstantDownloadRate();
                            downloadRateList.add(downloadRate);
                            position = getPositionByRate(downloadRate);

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    rotate = new RotateAnimation(lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                    rotate.setInterpolator(new LinearInterpolator());
                                    rotate.setDuration(100);
                                    downloadTextView.setText(dec.format(downloadTest.getInstantDownloadRate()));

                                }

                            });
                            lastPosition = position;

                            //Update chart
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Creating an  XYSeries for Income
                                    XYSeries downloadSeries = new XYSeries("");
                                    downloadSeries.setTitle("");

                                    List<Double> tmpLs = new ArrayList<>(downloadRateList);
                                    int count = 0;
                                    for (Double val : tmpLs) {
                                        downloadSeries.add(count++, val);
                                    }

                                    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                    dataset.addSeries(downloadSeries);

                                    GraphicalView chartView = ChartFactory.getLineChartView(getBaseContext(), dataset, multiDownloadRenderer);
                                }
                            });

                        }
                    }


                    //Upload Test
                    if (downloadTestFinished) {
                        if (uploadTestFinished) {
                            //Failure
                            if (uploadTest.getFinalUploadRate() == 0) {
                                System.out.println("Upload error...");
                            } else {
                                //Success
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        uploadTextView.setText(dec.format(uploadTest.getFinalUploadRate()));
                                    }
                                });
                            }
                        } else {
                            //Calc position
                            double uploadRate = uploadTest.getInstantUploadRate();
                            uploadRateList.add(uploadRate);
                            position = getPositionByRate(uploadRate);

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    rotate = new RotateAnimation(lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                    rotate.setInterpolator(new LinearInterpolator());
                                    rotate.setDuration(100);
                                    uploadTextView.setText(dec.format(uploadTest.getInstantUploadRate()));
                                }

                            });
                            lastPosition = position;

                            //Update chart
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Creating an  XYSeries for Income
                                    XYSeries uploadSeries = new XYSeries("");
                                    uploadSeries.setTitle("");

                                    int count = 0;
                                    List<Double> tmpLs = new ArrayList<>(uploadRateList);
                                    for (Double val : tmpLs) {
                                        if (count == 0) {
                                            val = 0.0;
                                        }
                                        uploadSeries.add(count++, val);
                                    }

                                    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                    dataset.addSeries(uploadSeries);

                                    GraphicalView chartView = ChartFactory.getLineChartView(getBaseContext(), dataset, multiUploadRenderer);
                                }
                            });

                        }
                    }

                    //Test bitti
                    if (pingTestFinished && downloadTestFinished && uploadTest.isFinished()) {
                        break;
                    }

                    if (pingTest.isFinished()) {
                        pingTestFinished = true;
                    }
                    if (downloadTest.isFinished()) {
                        downloadTestFinished = true;
                    }
                    if (uploadTest.isFinished()) {
                        uploadTestFinished = true;
                    }

                    if (pingTestStarted && !pingTestFinished) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                        }
                    } else {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    }
                }

                //Thread bitiminde button yeniden aktif ediliyor
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Btnstartspeed.setEnabled(true);
                        //Btnstartspeed.setTextSize(14);
                        ////Btnstartspeed.setText("Stop Speed test");

                        // connect to Oracle DB
                        connecttoDB();

                        // ADD LAT,LONG, DOWNLOAD and UPLOAD in ALM oracle database SPEEDTEST
                        PreparedStatement stmtinsert1 = null;

                        try {
                            String vlat,vlng,vdwnload,vupload;
                            vlat= (String) txtlat.getText ();
                            vlng= (String) txtlng.getText ();
                            vdwnload=  (String) downloadTextView.getText ();
                            vupload= (String) uploadTextView.getText ();
                            stmtinsert1 = conn.prepareStatement("insert into SPEEDTEST (SPEEDTSTID,SPEED_DATE,SPEED_LAT,SPEED_LNG,SPEED_DOWNLOAD,SPEED_UPLOAD) values (SPPEDTEST_SEQ.nextval,sysdate, '"+ vlat  +"', '"+ vlng  +"', '"+ vdwnload  +"', '"+ vupload  +"')");
                        } catch (SQLException throwables) {
                            throwables.printStackTrace ( );
                        }


                        try {
                            stmtinsert1.executeUpdate();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace ( );
                        }


                        try {
                            stmtinsert1.close();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace ( );
                        }

                        //System.out.println("insert into SPEEDTEST COMPLETED");


                        //Get how many tome we run speed test today
                        Statement stmt1 = null;
                        int i=0;
                        try {
                            stmt1 = conn.createStatement();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        String sqlStmt = "select count(1) as compteur from SPEEDTEST where TO_CHAR(SPEED_DATE, 'YYYY-MM-DD')=TO_CHAR(SYSDATE, 'YYYY-MM-DD')";
                        ResultSet rs1 = null;
                        try {
                            rs1 = stmt1.executeQuery(sqlStmt);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        while (true) {
                            try {
                                if (!rs1.next()) break;
                                Notifyspeedtesttime.setText (rs1.getString("compteur") );
                                System.out.println(rs1.getString("compteur"));

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
                            conn.close ();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    public int getPositionByRate(double rate) {
        if (rate <= 1) {
            return (int) (rate * 30);

        } else if (rate <= 10) {
            return (int) (rate * 6) + 30;

        } else if (rate <= 30) {
            return (int) ((rate - 10) * 3) + 90;

        } else if (rate <= 50) {
            return (int) ((rate - 30) * 1.5) + 150;

        } else if (rate <= 100) {
            return (int) ((rate - 50) * 1.2) + 180;
        }

        return 0;
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
            Toast.makeText (MainActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (MainActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (InstantiationException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (MainActivity.this,"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void GetCoverageSignal() {
        // call class Gps to get our location
        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            txtlat.setText(String.valueOf(latitude));
            txtlng.setText(String.valueOf(longitude));
        }else{
            gpsTracker.showSettingsAlert();
            txtlat.setText("Error to get latitude");
            txtlng.setText("Error to get longitude");
        }


        // call class coverage signal test
        telephonyManager = (TelephonyManager) MainActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE,ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            //txtsignal.setText(""+telephonyManager.getSignalStrength());

            //Get GSM signal from result
            String vsignal="0";
            String valsignal=telephonyManager.getSignalStrength().toString ();
            String varsignal1=valsignal;
            String varsignal2=null;
            int n = varsignal1.indexOf("mGsm=");
            varsignal1 = varsignal1.substring((n+1), valsignal.length ()).trim();
            varsignal2=varsignal1;
            n = varsignal1.indexOf("mWcdma=");
            varsignal2= varsignal2.substring(0, n-1).trim();
            String varsignal3=varsignal2;
            n = varsignal3.indexOf("mLevel=");
            varsignal3= varsignal3.substring((n+7), varsignal3.length ()).trim();
            txtsignal.setText(""+varsignal3);


            // connect to Oracle DB
            connecttoDB();
            // ADD LAT,LONG, DOWNLOAD and UPLOAD in ALM oracle database COVERAGETEST
            PreparedStatement stmtinsert1 = null;

            try {
                String vlat,vlng,vdwnload,vupload;
                vlat= (String) txtlat.getText ();
                vlng= (String) txtlng.getText ();
                vsignal=  (String) txtsignal.getText ();
                stmtinsert1 = conn.prepareStatement("insert into COVERAGETEST (COVERAGETSTID,COVERAGE_DATE,COVERAGE_LAT,COVERAGE_LNG,COVERAGE_SIGNAL) values (COVERAGEEST_SEQ.nextval,sysdate, '"+ vlat  +"', '"+ vlng  +"', '"+ vsignal  +"')");
            } catch (SQLException throwables) {
                throwables.printStackTrace ( );
            }


            try {
                stmtinsert1.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace ( );
            }


            try {
                stmtinsert1.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace ( );
            }

            System.out.println("insert into COVERAGETEST COMPLETED");


            //Get how many tome we run coverage test today
            Statement stmt1 = null;
            int i=0;
            try {
                stmt1 = conn.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String sqlStmt = "select count(1) as compteur from COVERAGETEST where TO_CHAR(COVERAGE_DATE, 'YYYY-MM-DD')=TO_CHAR(SYSDATE, 'YYYY-MM-DD')";
            ResultSet rs1 = null;
            try {
                rs1 = stmt1.executeQuery(sqlStmt);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            while (true) {
                try {
                    if (!rs1.next()) break;
                    Notifycoveragetesttime.setText (rs1.getString("compteur") +"");
                    //System.out.println(rs1.getString("compteur"));

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
                conn.close ();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }






        }
    }
   }