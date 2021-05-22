package com.example.aliatnetwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;

public class TicketInfoActivity extends AppCompatActivity {

    TextView txtTicketId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.ticketinfo);
        txtTicketId=findViewById (R.id.txtTicketId);


        TabLayout tabLayout=findViewById(R.id.tabBarTicket);
        TabItem tabChats=findViewById(R.id.tabInfoTicket);
        TabItem tabStatus=findViewById(R.id.tabAct);
        TabItem tabCalls=findViewById(R.id.tabAsto);
        ViewPager viewPager=findViewById(R.id.viewPagerTicket);

        TicketPagerAdapter pagerAdapter=new TicketPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
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
                    case 3:
                        viewPager.setCurrentItem (3);
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
        txtTicketId.setText (str);
        System.out.println("alooooooooooo"+str);


    }
    // to read data coming from fragment
    public void getTicketFragment(String test)
    {
        try {
            txtTicketId.setText (test);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
