package com.sign.nestedscrollingandrefreshdemo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SpringView springView1 = findViewById(R.id.spring_view1);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new ListViewFragment();
                }
                return new RecyclerFragment();
            }

            @Override
            public int getCount() {
                return 6;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return "ListView Tab";
                }
                return "分类" + position;
            }
        });
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        springView1.setGive(SpringView.Give.TOP);
        springView1.setType(SpringView.Type.FOLLOW);

        springView1.setHeader(new DefaultHeader(this));

        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                Log.d("test", "verticalOffset==============" + verticalOffset);
                //当顶部已完全可见
                if (verticalOffset >= 0) {
                    Log.d("test", "setEnable               true");
                    springView1.setEnable(true);
                } else {
                    Log.d("test", "setEnable               false");
                    springView1.setEnable(false);
                }
                Log.d("test", "isEnable               " + springView1.isEnable());
            }
        });

    }

}
