package com.example.aliatnetwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;

public class SiteInfoActivity extends AppCompatActivity {
    TextView txtwareid;
    Button previousBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.siteinfo);
        txtwareid=findViewById (R.id.txtwareid);
         previousBtn = findViewById (R.id.previousBtn);

        TabLayout tabLayout=findViewById(R.id.tabBar);
        TabItem tabChats=findViewById(R.id.tabInfo);
        TabItem tabStatus=findViewById(R.id.tabImage);
        TabItem tabCalls=findViewById(R.id.tabScan);
        ViewPager viewPager=findViewById(R.id.viewPager);

        PagerAdapter pagerAdapter=new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //viewPager.setCurrentItem(tab.getPosition());
                //to focus on TAB name after selecting it like focus on Info or image or Scan
                System.out.println ( "tabs is : "+ tab.getPosition() );
                switch(tab.getPosition()) {
                    case 0:
                        viewPager.setCurrentItem (0);
                        break;
                    case 1:
                        viewPager.setCurrentItem (1);
                        break;
                    case 2:
                        viewPager.setCurrentItem (2);
                          break;
                    default:
                        viewPager.setCurrentItem(tab.getPosition());
                          break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }



        });


        //read passes value of ware_id from recylserview
        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");
        txtwareid.setText (str);



    }
}
