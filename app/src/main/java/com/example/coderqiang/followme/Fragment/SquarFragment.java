package com.example.coderqiang.followme.Fragment;

import android.animation.ArgbEvaluator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coderqiang.followme.Activity.MainActivity;
import com.example.coderqiang.followme.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/11/4.
 */

public class SquarFragment extends android.app.Fragment {
    private static final String TAG="SquareFragment";
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.square_bg_tv)
    TextView textView;
    List<android.support.v4.app.Fragment> fragments;
    PagerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_square, container, false);
        ButterKnife.bind(this, v);
        initViewPager();
        InitTabLayout();
        return v;
    }

    private void initViewPager() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ArgbEvaluator evaluator = new ArgbEvaluator();
                if (position == 0) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_1_green));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_2_blue), getResources().getColor(R.color.tab_1_green));
                    setBg(evaluate);
                }
                if (0 < position && position < 1) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_1_green));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_1_green), getResources().getColor(R.color.tab_2_blue));
                    setBg(evaluate);
                }

                if (position == 1) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_3_purple));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_1_green), getResources().getColor(R.color.tab_3_purple));
                    setBg(evaluate);
                }

                if (1 < position && position < 2) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_3_purple));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_3_purple), getResources().getColor(R.color.tab_1_green));
                    mTabLayout.setBackgroundColor(evaluate);
                }


                if (position == 2) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_pink));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_3_purple), getResources().getColor(R.color.tab_pink));
                    setBg(evaluate);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        fragments = new ArrayList<android.support.v4.app.Fragment>();
        fragments.add(new DynamicFragment());
        fragments.add(new ScenicFragment());
        fragments.add(new TestFragment());
        mAdapter = new FragAdapter(((MainActivity)getActivity()).getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
    }

    private void setBg(int evaluate){
        mTabLayout.setBackgroundColor(evaluate);
        textView.setBackgroundColor(evaluate);
    }

    private void InitTabLayout() {
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(viewPager);
    }

    class FragAdapter extends FragmentPagerAdapter {
        private List<android.support.v4.app.Fragment> fragments;

        public FragAdapter(android.support.v4.app.FragmentManager fm, List<android.support.v4.app.Fragment> fragments) {
            super(fm);
            this.fragments=fragments;
        }



        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "动态";
            else if (position == 1) return "景点";
            else if (position == 2) return "攻略";
            else return "其它";
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragments.get(position);
        }
    }
}
