package com.example.administrator.ding_small.Fragment;

import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
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


/**
 * Created by Administrator on 2017/11/30.
 */

public class Fragment2 extends Fragment {
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;
    private LinearLayout layout;
    LinearLayout ll,two;
    RelativeLayout action;
    private String atTime,at_action;
    //图标
    int icno[] = null;
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
                    at_action=dataList.get(arg2).get("text").toString();
                    action_text.setText(at_action);
                    /*该方法待考虑*/
                    switch (arg2){
                        case 0:
                            action.setBackgroundColor(getResources().getColor(R.color.bg1));
                            break;
                        case 1:
                            action.setBackgroundColor(getResources().getColor(R.color.bg2));

                            break;
                        case 2:
                            action.setBackgroundColor(getResources().getColor(R.color.bg3));
                            break;
                        case 3:
                            action.setBackgroundColor(getResources().getColor(R.color.bg4));

                            break;
                        case 4:
                            action.setBackgroundColor(getResources().getColor(R.color.bg5));

                            break;
                        case 5:
                            action.setBackgroundColor(getResources().getColor(R.color.bg6));

                            break;
                        case 6:
                            action.setBackgroundColor(getResources().getColor(R.color.bg7));

                            break;
                        case 7:
                            action.setBackgroundColor(getResources().getColor(R.color.bg8));

                            break;
                        case 8:
                            action.setBackgroundColor(getResources().getColor(R.color.bg9));
                            break;
                        case 9:
                            action.setBackgroundColor(getResources().getColor(R.color.bg10));
                            break;
                        case 10:
                            action.setBackgroundColor(getResources().getColor(R.color.bg11));
                            break;
                        case 11:
                            action.setBackgroundColor(getResources().getColor(R.color.bg12));
                            break;
                        case 12:
                            action.setBackgroundColor(getResources().getColor(R.color.bg13));
                            break;
                        case 13:
                            action.setBackgroundColor(getResources().getColor(R.color.bg14));
                            break;
                        case 14:
                            action.setBackgroundColor(getResources().getColor(R.color.bg1));
                            break;
                        case 15:
                            action.setBackgroundColor(getResources().getColor(R.color.bg2));
                            break;
                        case 16:
                            action.setBackgroundColor(getResources().getColor(R.color.bg3));
                            break;
                        case 17:
                            action.setBackgroundColor(getResources().getColor(R.color.bg4));
                            break;
                        case 18:
                            action.setBackgroundColor(getResources().getColor(R.color.bg5));
                            break;
                        case 19:
                            action.setBackgroundColor(getResources().getColor(R.color.bg6));
                            break;
                        case 20:
                            action.setBackgroundColor(getResources().getColor(R.color.bg7));
                            break;
                        case 21:
                            action.setBackgroundColor(getResources().getColor(R.color.bg8));
                            break;
                        case 22:
                            action.setBackgroundColor(getResources().getColor(R.color.bg9));
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
