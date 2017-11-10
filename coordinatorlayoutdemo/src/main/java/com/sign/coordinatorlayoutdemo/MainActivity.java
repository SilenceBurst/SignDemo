package com.sign.coordinatorlayoutdemo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private String[] titleArr = {"测试一", "测试二", "测试三"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewPager();
    }


    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        fragmentList = new ArrayList<>();
        fragmentList.add(new MyFragment());
        fragmentList.add(new MyFragment());
        fragmentList.add(new MyFragment());

        viewPager.setAdapter(new MyViewPagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
    }


    class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleArr[position];
        }
    }
}
