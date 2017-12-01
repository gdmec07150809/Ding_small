package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.HelpTool.FlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import static android.R.id.list;
import static com.example.administrator.ding_small.HelpTool.getContactInfo.getContactInfo;
import static com.example.administrator.ding_small.R.id.clean_text;
import static com.example.administrator.ding_small.R.id.contacts_text;
import static com.example.administrator.ding_small.R.id.found_activity_lay;
import static com.example.administrator.ding_small.R.id.search_btn;

/**
 * Created by Administrator on 2017/11/29.
 */

public class NotepadTurnToSendActivity extends Activity{
    private TextView label_text,repeat_text,location_text,photo_text,dateT,week,title,reimbursement_text,loan_text,privacy_text,at_action;
    private FlowLayout found_activity_lay;

    private ListView list;
    private JSONObject contactData;//储存第一手信息
    private JSONObject contactDatas;//储存搜索结果
    private JSONObject jsonObject;//为contactData提供对象
    private EditText search_text,remarks_text;//搜索框
    boolean isFlag=true;//用哪个JsonObject响应listVIEW点击事件
    private Button search_btn;
    private ImageView clean_text;
    private     String name="";
    private String[] ContactsStrs = new String[]{"lily/youyou", "张先生/优游", "李小龙", "郭德纲", "李维嘉", "何炅", "谢娜", "黄晓明", "张艺兴"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad_turn_to_send);
        contactsFlowLayout();
        //获取手机联系人列表
        try {
            contactData=getContactInfo(NotepadTurnToSendActivity.this);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        list=findViewById(R.id.contacts_list);
        search_btn=findViewById(R.id.search_btn);
        clean_text=findViewById(R.id.clean_text);
        search_text=findViewById(R.id.search_edittext);

        list.setAdapter(new com.example.administrator.ding_small.Adapter.ContactAdapter(NotepadTurnToSendActivity.this, contactData));
        //搜索联系人事件
        search_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isFlag=false;
                String search_val=search_text.getText().toString();
                String ss= null;
                contactDatas=new JSONObject();;
                String name="";
                int index=0;
                try {
                    for(int i=0;i<contactData.length();i++){
                        ss = contactData.getString("contact"+i);
                        JSONObject obj=new JSONObject(ss);
                        name=obj.getString("lastname").substring(obj.getString("lastname").length()-1,obj.getString("lastname").length()).toString();
                        if(search_val.equals(name)){
                            System.out.println("键："+name);
                            System.out.println("值："+search_val);
                            contactDatas.put("contact"+index, obj);
                            index++;
                        }else if(search_val.equals(obj.getString("mobile"))){
                            contactDatas.put("contact"+index, obj);
                            index++;
                        }
                    }
                    list.setAdapter(new com.example.administrator.ding_small.Adapter.ContactAdapter(NotepadTurnToSendActivity.this, contactDatas));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        //清除搜索框内容
        clean_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isFlag=true;
                search_text.setText("");
                list.setAdapter(new com.example.administrator.ding_small.Adapter.ContactAdapter(NotepadTurnToSendActivity.this, contactData));
            }
        });
        //联系人列表子事件
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ss= null;
                String phone="";
                try {
                    if(isFlag){
                        ss = contactData.getString("contact"+i);
                    }else {
                        ss = contactDatas.getString("contact"+i);
                    }
                    JSONObject obj=new JSONObject(ss);

                    name=obj.getString("lastname").substring(obj.getString("lastname").length()-1,obj.getString("lastname").length()).toString();
                    phone=obj.getString("mobile");
                    Intent intent=new Intent(NotepadTurnToSendActivity.this,NotepadRemarkDetailsActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("phone",phone);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void contactsFlowLayout() {
        if(found_activity_lay==null){
            //加载搜索记录
            for (int i = 0; i < ContactsStrs.length; i++) {
                final TextView text = new TextView(NotepadTurnToSendActivity.this);
                System.out.println("数组："+ContactsStrs[i]);
                text.setText(ContactsStrs[i]);//添加内容
                text.setTextSize(12);
                text.setTextColor(getResources().getColor(R.color.green));
                text.setBackgroundResource(R.drawable.light_button_back);
                text.setPadding(15, 10, 15, 10);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//设置宽高,第一个参数是宽,第二个参数是高
                //设置边距
                params.topMargin = 5;
                params.bottomMargin = 5;
                params.leftMargin = 8;
                params.rightMargin = 8;
                text.setLayoutParams(params);
                found_activity_lay = findViewById(R.id.found_activity_lay);
                found_activity_lay.addView(text);//将内容添加到布局中
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//添加点击事件
                        search_text.setText(text.getText().toString());
                        Toast.makeText(NotepadTurnToSendActivity.this, text.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
