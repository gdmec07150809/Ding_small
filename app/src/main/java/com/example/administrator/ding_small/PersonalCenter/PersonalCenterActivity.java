package com.example.administrator.ding_small.PersonalCenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ding_small.AccountBookActivity;
import com.example.administrator.ding_small.ContactsActivity;
import com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity;
import com.example.administrator.ding_small.MainLayoutActivity;
import com.example.administrator.ding_small.NotepadActivity;
import com.example.administrator.ding_small.NotepadBtnActivity;
import com.example.administrator.ding_small.R;

import org.w3c.dom.Text;

import java.util.Locale;

/**
 * Created by Administrator on 2017/11/6.
 */

public class PersonalCenterActivity extends Activity implements View.OnClickListener{
    private ImageView f_account,f_contacts,f_center,f_notepad;
    //更改语言所要更改的控件
    private TextView security_text,setting_text,about_text,custom_text,feedback_text,home_text,my_text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_personal_center);
        findViewById(R.id.perfect).setOnClickListener(this);
  //      findViewById(R.id.instructions).setOnClickListener(this);
        findViewById(R.id.about).setOnClickListener(this);
        findViewById(R.id.setting).setOnClickListener(this);
        findViewById(R.id.security).setOnClickListener(this);
        findViewById(R.id.home_layout).setOnClickListener(this);

        security_text=findViewById(R.id.security_text);
        setting_text=findViewById(R.id.setting_text);
        about_text=findViewById(R.id.about_text);
        custom_text=findViewById(R.id.custom_text);
        feedback_text=findViewById(R.id.feedback_text);
        home_text=findViewById(R.id.home_text);
        my_text=findViewById(R.id.my_text);
        changeTextView();//更改语言
    }

    private void changeTextView(){
        if(Locale.getDefault().getLanguage().equals("en")){
            security_text.setText("Account Security");
            setting_text.setText("Setting");
            about_text.setText("About Us");
            custom_text.setText("Contact Customer Service");
            feedback_text.setText("Feedback");
            home_text.setText("Home");
            my_text.setText("My");
        }
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.perfect:
                intent=new Intent(PersonalCenterActivity.this,PersonalCenterPerfectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
//            case R.id.instructions:
//                intent=new Intent(PersonalCenterActivity.this,InstructionsActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                break;
            case R.id.setting:
                intent=new Intent(PersonalCenterActivity.this,SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
//            case R.id.about:
//                intent=new Intent(PersonalCenterActivity.this,AboutActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                break;
            case R.id.security:
                intent=new Intent(PersonalCenterActivity.this,AccountSecurityActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.home_layout:
                intent=new Intent(PersonalCenterActivity.this,MainLayoutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
//            case R.id.binding_company:
//                intent=new Intent(PersonalCenterActivity.this,BindingCompanyActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                break;
//            case R.id.add:
//                intent=new Intent(PersonalCenterActivity.this,NotepadActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                break;
//            case R.id.f_account:
//                intent=new Intent(PersonalCenterActivity.this,AccountBookActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                break;
//            case R.id.f_notepad:
//                intent=new Intent(PersonalCenterActivity.this,NotepadBtnActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                break;
//            case R.id.f_contacts:
//                intent=new Intent(PersonalCenterActivity.this,ContactsActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                break;
//            case R.id.exit:
//                intent=new Intent(PersonalCenterActivity.this, LoginAcitivity.class);
//                startActivity(intent);
//                System.exit(0);
//                break;
//            case R.id.edit_pass:
//                intent=new Intent(PersonalCenterActivity.this,EditPassWordActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                break;
            default:
                break;
        }
    }
}
