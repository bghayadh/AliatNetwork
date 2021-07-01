package com.example.aliatnetwork;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ShopInfoActivity extends AppCompatActivity  {

    TextView txtShopsId;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shops_info);

        txtShopsId=findViewById(R.id.editTxtShopsID);

        TabLayout tabLayout = findViewById(R.id.tabBarShops);
        TabItem tabChats=findViewById(R.id.tabInfoShops);
        TabItem tabStatus=findViewById(R.id.tabActShops);
        TabItem tabCalls=findViewById(R.id.tabAsto);
        ViewPager viewPager = findViewById(R.id.viewPagerShops);

        ShopsPagerAdapter shopsPagerAdapter = new ShopsPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(shopsPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("tab is :"+tab.getPosition());
                switch (tab.getPosition()){

                    case 0:
                        viewPager.setCurrentItem(0);
                        break;
                    case 1:
                        viewPager.setCurrentItem(1);
                        break;
                    case 2:
                        viewPager.setCurrentItem(2);
                        break;
                    case 3:
                        viewPager.setCurrentItem(3);
                        break;
                    default:viewPager.setCurrentItem(tab.getPosition());
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

        Intent intent=getIntent();
        String str = intent.getStringExtra("message_key");
        txtShopsId.setText(str);




    }
    public void getShopsFragment(String test)
    {
        try {
            txtShopsId.setText(test);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
