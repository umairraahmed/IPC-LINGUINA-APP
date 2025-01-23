package com.example.ipc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ipc.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivityLg extends AppCompatActivity {

    private static final String TAG = "MainActivityLg";
    private Button btnnxt;
    private Button btnskip;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lg);

        btnnxt = findViewById(R.id.btnnxt);
        btnskip = findViewById(R.id.btnskip);
        viewPager = findViewById(R.id.viewPager);


        int layouts[] = {
                R.layout.activity_intro,
                R.layout.activity_intro2,
                R.layout.activity_intro3
        };

        viewPager.setAdapter(new PagerAdapter() {
            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = layoutInflater.inflate(layouts[position],container, false);
                container.addView(view);
                return view;
            }

            @Override
            public int getCount() {
                return layouts.length;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                View view = (View) object;
                container.removeView(view);

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected: "+position );
                if (position ==2)
                {
                    btnnxt.setText("Login");
                }

                else {
                    btnnxt.setText("Next");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnnxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = viewPager.getCurrentItem();
                if (current < layouts.length - 1)
                {
                    viewPager.setCurrentItem(current + 1);
                }
                else {
                    launchLogin();
                }
            }
        });

        btnskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            launchLogin();
            }
        });

    }

    private void launchLogin() {
        startActivity(new Intent(MainActivityLg.this, LoginActivity.class));
    }
}