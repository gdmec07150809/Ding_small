package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.Adapter.CalendarViewAdapter;
import com.example.administrator.ding_small.Adapter.NotepadSearchByCalendarAdapter;
import com.example.administrator.ding_small.HelpTool.CalendarCard;
import com.example.administrator.ding_small.HelpTool.CalendarCard.OnCellClickListener;
import com.example.administrator.ding_small.HelpTool.CustomDate;
import com.example.administrator.ding_small.HelpTool.MylistView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/6.
 */

public class NotepadSearchByCalendarActivity extends Activity implements OnClickListener,OnCellClickListener {
    private MylistView search_results;
    private ArrayList<String> arrayList;
    private ViewPager mViewPager;
    private int mCurrentIndex = 498;
    private CalendarCard[] mShowViews;
    private CalendarViewAdapter<CalendarCard> adapter;
    private SildeDirection mDirection = SildeDirection.NO_SILDE;
    enum SildeDirection {
        RIGHT, LEFT, NO_SILDE;
    }

    private ImageButton preImgBtn, nextImgBtn;
    private TextView monthText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.notepad_search_by_calendar);
        mViewPager = (ViewPager) this.findViewById(R.id.vp_calendar);
        preImgBtn = (ImageButton) this.findViewById(R.id.btnPreMonth);
        nextImgBtn = (ImageButton) this.findViewById(R.id.btnNextMonth);
        monthText = (TextView) this.findViewById(R.id.tvCurrentMonth);
        search_results=findViewById(R.id.calendar_results);
        preImgBtn.setOnClickListener(this);
        nextImgBtn.setOnClickListener(this);
        monthText.setOnClickListener(this);
        CalendarCard[] views = new CalendarCard[3];
        for (int i = 0; i < 3; i++) {
            views[i] = new CalendarCard(this, this);
        }
        adapter = new CalendarViewAdapter<>(views);
        setViewPager();
    }

    private void setViewPager() {
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(498);
        arrayList=new ArrayList<String>();
        for (int i=0;i<10;i++){
            arrayList.add(i+"");
        }
        search_results.setAdapter(new NotepadSearchByCalendarAdapter(NotepadSearchByCalendarActivity.this,arrayList));
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                measureDirection(position);
                updateCalendarView(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });


    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnPreMonth:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                break;
            case R.id.btnNextMonth:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                break;
            case R.id.tvCurrentMonth:
                intent=new Intent(NotepadSearchByCalendarActivity.this,SearchTimeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            default:
                break;
        }
    }

    @Override
    public void clickDate(CustomDate date) {
        Toast.makeText(this,"点击了："+date.year+"年"+date.month + "月"+date.day+"日",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeDate(CustomDate date) {
        monthText.setText(date.year+"年"+date.month + "月"+date.day+"日");
    }

    /**
     * 计算方向
     *
     * @param arg0
     */
    private void measureDirection(int arg0) {

        if (arg0 > mCurrentIndex) {
            mDirection = SildeDirection.RIGHT;

        } else if (arg0 < mCurrentIndex) {
            mDirection = SildeDirection.LEFT;
        }
        mCurrentIndex = arg0;
    }

    // 更新日历视图
    private void updateCalendarView(int arg0) {
        mShowViews = adapter.getAllItems();
        if (mDirection == SildeDirection.RIGHT) {
            mShowViews[arg0 % mShowViews.length].rightSlide();
        } else if (mDirection == SildeDirection.LEFT) {
            mShowViews[arg0 % mShowViews.length].leftSlide();
        }
        mDirection = SildeDirection.NO_SILDE;
    }



}
