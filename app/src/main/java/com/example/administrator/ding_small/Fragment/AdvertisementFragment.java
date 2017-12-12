package com.example.administrator.ding_small.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.ding_small.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/9/29.
 */
public class AdvertisementFragment extends android.support.v4.app.Fragment {

    private View view;
    @ViewInject(R.id.advertisement_img)
    private ImageView advertisement_img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.advertisement_fragment_lyt, container, false);
        ViewUtils.inject(this, view);
        if (getArguments() != null) {
            advertisement_img.setBackgroundResource(getArguments().getInt("image", R.mipmap.banner));
        }
        return view;
    }


}



