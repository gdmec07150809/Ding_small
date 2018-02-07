package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.ding_small.Fragment.Fragment1;
import com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youyou000 on 2018/start2/7.
 */

public class StartActivity extends Activity {
    private ArrayList<View> mViewList=null;
    private ViewPager in_viewpager;
    public static final int SHOW_RESPONSE = 0;
    private static final String tokeFile = "fristFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.start_layout);
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        if(sp.getString("login", "")!=null&&!sp.getString("login", "").equals("")){
            Intent intent=new Intent(StartActivity.this,NewMainLayoutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        in_viewpager=findViewById(R.id.in_viewpager);
        mViewList = new ArrayList<View>();
        LayoutInflater lf = getLayoutInflater().from(StartActivity.this);
        View view1 = lf.inflate(R.layout.start_one_layout, null);
        View view2 = lf.inflate(R.layout.start_two_layout, null);
        View view3 = lf.inflate(R.layout.start_three_layout, null);
        View view4 = lf.inflate(R.layout.start_four_layout, null);
        view4.findViewById(R.id.come_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sp = StartActivity.this.getSharedPreferences(tokeFile, MODE_PRIVATE);//实例化
                SharedPreferences.Editor editor = sp.edit(); //使处于可编辑状态
                editor.putString("login", "1");
                editor.commit();    //提交数据保存

                Intent intent=new Intent(StartActivity.this,NewMainLayoutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("loginCache","2");
                startActivity(intent);
                finish();
            }
        });
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
        mViewList.add(view4);

        in_viewpager.setAdapter(new ViewPagerAdatper(mViewList));
        in_viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
    }
    public class ViewPagerAdatper extends PagerAdapter {
        private List<View> mViewList ;

        public ViewPagerAdatper(List<View> mViewList ) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }
    }

    /**
     * 页卡切换监听
     *
     * @author CZK
     * @version start1.0
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 3:

                    break;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
