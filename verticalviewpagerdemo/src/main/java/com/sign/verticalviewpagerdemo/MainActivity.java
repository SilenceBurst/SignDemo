package com.sign.verticalviewpagerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.vertical_viewpager)
    VerticalViewPager verticalViewpager;

    private List<Fragment> fragmentList;
    private TimerTask mTimerTask;
    private Timer mTimer;
    private Handler mHandler;
    private int currentPage = 0;
    //true 正序滑动
    private boolean isPositive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViewPager();

        initTimer();
    }

    private void initTimer() {
        mTimer = new Timer();
        //要做的事情的一个方法
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                //最后一个直接到第一个
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        currentPage++;
//                        if (currentPage > fragmentList.size() - 1) {
//                            currentPage = 0;
//                        }
//                        verticalViewpager.setCurrentItem(currentPage, true);
//                    }
//                });
                //反序返回
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isPositive) {
                            currentPage++;
                            if (currentPage == fragmentList.size() - 1) {
                                isPositive = false;
                            }
                        } else {
                            currentPage--;
                            if (currentPage == 0) {
                                isPositive = true;
                            }
                        }
                        verticalViewpager.setCurrentItem(currentPage, true);
                    }
                });
            }
        };
        mTimer.schedule(mTimerTask, 3000, 3000);
    }

    private void initViewPager() {
        mHandler = new Handler();
        fragmentList = new ArrayList<>();
        fragmentList.add(MyFragment.newInstance(0));
        fragmentList.add(MyFragment.newInstance(1));
        fragmentList.add(MyFragment.newInstance(2));
        fragmentList.add(MyFragment.newInstance(3));
        fragmentList.add(MyFragment.newInstance(4));

        ViewPagerScroller scroller = new ViewPagerScroller(this);
        scroller.setScrollDuration(2000);
        scroller.initViewPagerScroll(verticalViewpager);//这个是设置切换过渡时间为2秒

        verticalViewpager.setAdapter(new TvFragmentPagerAdapter());
        verticalViewpager.setPageTransformer(true, new MyPageTransformer());
        verticalViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                if (currentPage == fragmentList.size() - 1) {
                    isPositive = false;
                }
                if (currentPage == 0) {
                    isPositive = true;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class TvFragmentPagerAdapter extends FragmentPagerAdapter {

        public TvFragmentPagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }
}
