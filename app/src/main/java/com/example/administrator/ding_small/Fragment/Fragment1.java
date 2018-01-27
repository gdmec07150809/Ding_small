package com.example.administrator.ding_small.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.NotepadActivity;
import com.example.administrator.ding_small.R;
import com.example.administrator.ding_small.Title.TitleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.administrator.ding_small.R.id.textView1;
import static com.example.administrator.ding_small.R.id.zxing_viewfinder_view;


/**
 * Created by CZK on 2017/11/30.
 */
    /*用于报修*/
public class Fragment1 extends Fragment {
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;
    private LinearLayout layout;
    LinearLayout ll,two;
    RelativeLayout action;
    ImageView title_img,img;
    private String atTime,at_action;


    //图标
    int icno[] =null;
    //图标下的文字
    String name[]=null;
    /**
     * 当fragment被创建的时候，调用的方法，返回当前fragment显示的内容
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null){
            icno=bundle.getIntArray("icon");
            name=bundle.getStringArray("name");
        }

        View view=inflater.inflate(R.layout.fragment1, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        //初始化数据
        initData();
        String[] from={"img","text"};

        int[] to={R.id.img,R.id.text};

        adapter=new SimpleAdapter(getActivity(), dataList, R.layout.gride_view_item, from, to);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent;
                if(dataList.get(arg2).get("text").toString().equals("编辑")){
                    intent=new Intent(getActivity(),TitleActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }else{

                   TextView action_text=getActivity().findViewById(R.id.action_text);
                    action=getActivity().findViewById(R.id.action);
                    title_img=getActivity().findViewById(R.id.title_img);
                    at_action=dataList.get(arg2).get("text").toString();
                    action_text.setText(at_action);
                    /*该方法待考虑*/
                    switch (arg2){
                        case 0:
                           // icno[arg2]=R.mipmap.fix_icon_noele_active;

                            action.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.repair_bg1));
                            title_img.setImageResource(R.mipmap.fix_icon_noele_active);

                            img=arg0.getChildAt(arg2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noele_active);

                            img=arg0.getChildAt(1).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocharge_normal);

                            img=arg0.getChildAt(2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nolight_normal);

                            img=arg0.getChildAt(3).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocold_normal);

                            img=arg0.getChildAt(4).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paiqi_normal);

                            img=arg0.getChildAt(5).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_umbrella_normal);

                            img=arg0.getChildAt(6).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paishui_normal);

                            img=arg0.getChildAt(7).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loushui_normal);

                            img=arg0.getChildAt(8).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loudian_normal);

                            img=arg0.getChildAt(9).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(10).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(11).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_surface_normal);

                            img=arg0.getChildAt(12).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_print_normal);

                            img=arg0.getChildAt(13).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_others_normal);

                            break;
                        case 1:
                            action.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.repair_bg8));
                            title_img.setImageResource(R.mipmap.fix_icon_nocharge_active);

                            img=arg0.getChildAt(arg2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocharge_active);

                            img=arg0.getChildAt(0).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noele_normal);

                            img=arg0.getChildAt(2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nolight_normal);

                            img=arg0.getChildAt(3).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocold_normal);

                            img=arg0.getChildAt(4).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paiqi_normal);

                            img=arg0.getChildAt(5).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_umbrella_normal);

                            img=arg0.getChildAt(6).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paishui_normal);

                            img=arg0.getChildAt(7).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loushui_normal);

                            img=arg0.getChildAt(8).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loudian_normal);

                            img=arg0.getChildAt(9).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(10).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(11).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_surface_normal);

                            img=arg0.getChildAt(12).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_print_normal);

                            img=arg0.getChildAt(13).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_others_normal);

                            break;
                        case 2:
                            action.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.repair_bg6));
                            title_img.setImageResource(R.mipmap.fix_icon_nolight_active);

                            img=arg0.getChildAt(arg2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nolight_active);

                            img=arg0.getChildAt(0).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noele_normal);

                            img=arg0.getChildAt(1).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocharge_normal);

                            img=arg0.getChildAt(3).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocold_normal);

                            img=arg0.getChildAt(4).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paiqi_normal);

                            img=arg0.getChildAt(5).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_umbrella_normal);

                            img=arg0.getChildAt(6).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paishui_normal);

                            img=arg0.getChildAt(7).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loushui_normal);

                            img=arg0.getChildAt(8).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loudian_normal);

                            img=arg0.getChildAt(9).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(10).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(11).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_surface_normal);

                            img=arg0.getChildAt(12).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_print_normal);

                            img=arg0.getChildAt(13).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_others_normal);

                            break;
                        case 3:
                            action.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.repair_bg5));
                            title_img.setImageResource(R.mipmap.fix_icon_nocold_active);

                            img=arg0.getChildAt(arg2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocold_active);

                            img=arg0.getChildAt(0).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noele_normal);

                            img=arg0.getChildAt(1).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocharge_normal);

                            img=arg0.getChildAt(2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nolight_normal);

                            img=arg0.getChildAt(4).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paiqi_normal);

                            img=arg0.getChildAt(5).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_umbrella_normal);

                            img=arg0.getChildAt(6).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paishui_normal);

                            img=arg0.getChildAt(7).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loushui_normal);

                            img=arg0.getChildAt(8).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loudian_normal);

                            img=arg0.getChildAt(9).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(10).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(11).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_surface_normal);

                            img=arg0.getChildAt(12).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_print_normal);

                            img=arg0.getChildAt(13).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_others_normal);
                            break;
                        case 4:
                            action.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.repair_bg7));
                            title_img.setImageResource(R.mipmap.fix_icon_paiqi_active);

                            img=arg0.getChildAt(arg2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paiqi_active);

                            img=arg0.getChildAt(0).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noele_normal);

                            img=arg0.getChildAt(1).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocharge_normal);

                            img=arg0.getChildAt(2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nolight_normal);

                            img=arg0.getChildAt(3).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocold_normal);

                            img=arg0.getChildAt(5).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_umbrella_normal);

                            img=arg0.getChildAt(6).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paishui_normal);

                            img=arg0.getChildAt(7).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loushui_normal);

                            img=arg0.getChildAt(8).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loudian_normal);

                            img=arg0.getChildAt(9).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(10).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(11).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_surface_normal);

                            img=arg0.getChildAt(12).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_print_normal);

                            img=arg0.getChildAt(13).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_others_normal);
                            break;
                        case 5:
                            action.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.repair_bg10));
                            title_img.setImageResource(R.mipmap.fix_icon_umbrella_active);

                            img=arg0.getChildAt(arg2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_umbrella_active);

                            img=arg0.getChildAt(0).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noele_normal);

                            img=arg0.getChildAt(1).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocharge_normal);

                            img=arg0.getChildAt(2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nolight_normal);

                            img=arg0.getChildAt(3).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocold_normal);

                            img=arg0.getChildAt(4).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paiqi_normal);

                            img=arg0.getChildAt(6).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paishui_normal);

                            img=arg0.getChildAt(7).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loushui_normal);

                            img=arg0.getChildAt(8).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loudian_normal);

                            img=arg0.getChildAt(9).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(10).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(11).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_surface_normal);

                            img=arg0.getChildAt(12).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_print_normal);

                            img=arg0.getChildAt(13).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_others_normal);
                            break;
                        case 6:
                            action.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.repair_bg8));
                            title_img.setImageResource(R.mipmap.fix_icon_paishui_active);
                           // arg0.findViewById(R.id.img).setBackgroundResource(R.mipmap.fix_icon_paishui_active);
                            img=arg0.getChildAt(arg2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paishui_active);

                            img=arg0.getChildAt(0).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noele_normal);

                            img=arg0.getChildAt(1).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocharge_normal);

                            img=arg0.getChildAt(2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nolight_normal);

                            img=arg0.getChildAt(3).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocold_normal);

                            img=arg0.getChildAt(4).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paiqi_normal);

                            img=arg0.getChildAt(5).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_umbrella_normal);

                            img=arg0.getChildAt(7).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loushui_normal);

                            img=arg0.getChildAt(8).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loudian_normal);

                            img=arg0.getChildAt(9).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(10).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(11).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_surface_normal);

                            img=arg0.getChildAt(12).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_print_normal);

                            img=arg0.getChildAt(13).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_others_normal);

                            break;
                        case 7:
                            action.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.repair_bg4));
                            title_img.setImageResource(R.mipmap.fix_icon_loushui_active);
                            //arg0.findViewById(R.id.img).setBackgroundResource(R.mipmap.fix_icon_loushui_active);

                            img=arg0.getChildAt(arg2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loushui_active);

                            img=arg0.getChildAt(0).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noele_normal);

                            img=arg0.getChildAt(1).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocharge_normal);

                            img=arg0.getChildAt(2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nolight_normal);

                            img=arg0.getChildAt(3).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocold_normal);

                            img=arg0.getChildAt(4).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paiqi_normal);

                            img=arg0.getChildAt(5).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_umbrella_normal);

                            img=arg0.getChildAt(6).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paishui_normal);

                            img=arg0.getChildAt(8).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loudian_normal);

                            img=arg0.getChildAt(9).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(10).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(11).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_surface_normal);

                            img=arg0.getChildAt(12).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_print_normal);

                            img=arg0.getChildAt(13).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_others_normal);
                            break;
                        case 8:
                            action.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.repair_bg2));
                            title_img.setImageResource(R.mipmap.fix_icon_loudian_active);
                            //arg0.findViewById(R.id.img).setBackgroundResource(R.mipmap.fix_icon_loudian_active);
                            img=arg0.getChildAt(arg2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loudian_active);

                            img=arg0.getChildAt(0).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noele_normal);

                            img=arg0.getChildAt(1).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocharge_normal);

                            img=arg0.getChildAt(2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nolight_normal);

                            img=arg0.getChildAt(3).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocold_normal);

                            img=arg0.getChildAt(4).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paiqi_normal);

                            img=arg0.getChildAt(5).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_umbrella_normal);

                            img=arg0.getChildAt(6).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paishui_normal);

                            img=arg0.getChildAt(7).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loushui_normal);

                            img=arg0.getChildAt(9).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(10).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(11).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_surface_normal);

                            img=arg0.getChildAt(12).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_print_normal);

                            img=arg0.getChildAt(13).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_others_normal);
                            break;
                        case 9:
                            action.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.repair_bg3));
                            title_img.setImageResource(R.mipmap.fix_icon_louqi_active);
                            //arg0.findViewById(R.id.img).setBackgroundResource(R.mipmap.fix_icon_louqi_active);
                            img=arg0.getChildAt(arg2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_active);

                            img=arg0.getChildAt(0).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noele_normal);

                            img=arg0.getChildAt(1).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocharge_normal);

                            img=arg0.getChildAt(2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nolight_normal);

                            img=arg0.getChildAt(3).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocold_normal);

                            img=arg0.getChildAt(4).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paiqi_normal);

                            img=arg0.getChildAt(5).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_umbrella_normal);

                            img=arg0.getChildAt(6).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paishui_normal);

                            img=arg0.getChildAt(7).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loushui_normal);

                            img=arg0.getChildAt(8).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loudian_normal);

                            img=arg0.getChildAt(10).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(11).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_surface_normal);

                            img=arg0.getChildAt(12).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_print_normal);

                            img=arg0.getChildAt(13).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_others_normal);
                            break;
                        case 10:
                            action.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.repair_bg1));
                            title_img.setImageResource(R.mipmap.fix_icon_noise_active);
                            //arg0.findViewById(R.id.img).setBackgroundResource(R.mipmap.fix_icon_noise_active);

                            img=arg0.getChildAt(arg2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noise_active);

                            img=arg0.getChildAt(0).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noele_normal);

                            img=arg0.getChildAt(1).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocharge_normal);

                            img=arg0.getChildAt(2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nolight_normal);

                            img=arg0.getChildAt(3).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocold_normal);

                            img=arg0.getChildAt(4).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paiqi_normal);

                            img=arg0.getChildAt(5).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_umbrella_normal);

                            img=arg0.getChildAt(6).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paishui_normal);

                            img=arg0.getChildAt(7).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loushui_normal);

                            img=arg0.getChildAt(8).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loudian_normal);

                            img=arg0.getChildAt(9).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(11).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_surface_normal);

                            img=arg0.getChildAt(12).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_print_normal);

                            img=arg0.getChildAt(13).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_others_normal);
                            break;
                        case 11:
                            action.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.repair_bg10));
                            title_img.setImageResource(R.mipmap.fix_icon_surface_active);
                            //arg0.findViewById(R.id.img).setBackgroundResource(R.mipmap.fix_icon_surface_active);

                            img=arg0.getChildAt(arg2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_surface_active);

                            img=arg0.getChildAt(0).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noele_normal);

                            img=arg0.getChildAt(1).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocharge_normal);

                            img=arg0.getChildAt(2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nolight_normal);

                            img=arg0.getChildAt(3).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocold_normal);

                            img=arg0.getChildAt(4).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paiqi_normal);

                            img=arg0.getChildAt(5).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_umbrella_normal);

                            img=arg0.getChildAt(6).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paishui_normal);

                            img=arg0.getChildAt(7).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loushui_normal);

                            img=arg0.getChildAt(8).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loudian_normal);

                            img=arg0.getChildAt(9).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(10).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noise_normal);

                            img=arg0.getChildAt(12).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_print_normal);

                            img=arg0.getChildAt(13).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_others_normal);
                            break;
                        case 12:
                            action.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.repair_bg9));
                            title_img.setImageResource(R.mipmap.fix_icon_print_active);//标题图片
                            //arg0.findViewById(R.id.img).setBackgroundResource(R.mipmap.fix_icon_print_active);//item图片
                            img=arg0.getChildAt(arg2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_print_active);
                            /*设置未激活item*/
                            img=arg0.getChildAt(0).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noele_normal);

                            img=arg0.getChildAt(1).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocharge_normal);

                            img=arg0.getChildAt(2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nolight_normal);

                            img=arg0.getChildAt(3).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocold_normal);

                            img=arg0.getChildAt(4).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paiqi_normal);

                            img=arg0.getChildAt(5).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_umbrella_normal);

                            img=arg0.getChildAt(6).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paishui_normal);

                            img=arg0.getChildAt(7).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loushui_normal);

                            img=arg0.getChildAt(8).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loudian_normal);

                            img=arg0.getChildAt(9).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(10).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noise_normal);

                            img=arg0.getChildAt(11).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_surface_normal);

                            img=arg0.getChildAt(13).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_others_normal);
                            break;
                        case 13:
                            action.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.repair_bg11));
                            title_img.setImageResource(R.mipmap.fix_icon_others_active);
                            //arg0.findViewById(R.id.img).setBackgroundResource(R.mipmap.fix_icon_others_active);

                            img=arg0.getChildAt(arg2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_others_active);

                            img=arg0.getChildAt(0).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noele_normal);

                            img=arg0.getChildAt(1).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocharge_normal);

                            img=arg0.getChildAt(2).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nolight_normal);

                            img=arg0.getChildAt(3).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_nocold_normal);

                            img=arg0.getChildAt(4).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paiqi_normal);

                            img=arg0.getChildAt(5).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_umbrella_normal);

                            img=arg0.getChildAt(6).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_paishui_normal);

                            img=arg0.getChildAt(7).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loushui_normal);

                            img=arg0.getChildAt(8).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_loudian_normal);

                            img=arg0.getChildAt(9).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_louqi_normal);

                            img=arg0.getChildAt(10).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_noise_normal);

                            img=arg0.getChildAt(11).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_surface_normal);

                            img=arg0.getChildAt(12).findViewById(R.id.img);
                            img.setImageResource(R.mipmap.fix_icon_print_normal);
                            break;
                    }
                }
            }
        });
        return view;
    }
    void initData() {
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <icno.length; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("img", icno[i]);
            map.put("text",name[i]);
            dataList.add(map);
        }
    }
}
